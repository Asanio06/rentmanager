package com.epf.rentmanager.ui.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {
	private static ClientService clientService = ClientService.getInstance();
	private static VehicleService vehicleService = VehicleService.getInstance();
	private static ReservationService reservationService = ReservationService.getInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/rents/create.jsp");
			request.setAttribute("Vehicles", vehicleService.findAll());
			request.setAttribute("Clients", clientService.findAll());
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Reservation reservation = new Reservation();
			
			Client client = new Client();
			client.setId(Long.parseLong(request.getParameter("client")));
			reservation.setClient(client);
			
			
			Vehicle vehicle = new Vehicle();
			vehicle.setId(Long.parseLong(request.getParameter("car")));
			reservation.setVehicle(vehicle);
			
			

			java.util.Date beginDate = format.parse(request.getParameter("begin"));
			java.util.Date endDate = format.parse(request.getParameter("end"));

			reservation.setDebut(new java.sql.Date(beginDate.getTime())) ;
			reservation.setFin(new java.sql.Date(endDate.getTime()));
			reservationService.create(reservation);
			response.sendRedirect("http://localhost:8080/rentmanager/rents");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
		
	
		
		
	}
}
