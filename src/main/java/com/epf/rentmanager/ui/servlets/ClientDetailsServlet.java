package com.epf.rentmanager.ui.servlets;

import java.io.IOException;
import java.util.List;

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

@WebServlet("/clients/details")
public class ClientDetailsServlet extends HttpServlet{
	
	private static ReservationService reservationService = ReservationService.getInstance();
	private static ClientService clientService = ClientService.getInstance();
	private static VehicleService vehicleService = VehicleService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/clients/details.jsp");
			long clientId = Long.parseLong(request.getParameter("id"));
			Client client = clientService.findById(clientId);
			List<Reservation> list_reservation = reservationService.findResaByClientId(clientId);
			List<Vehicle> list_vehicle = vehicleService.findDistinctVehiclesReservedByClient(client);
			
			request.setAttribute("client", client);
			request.setAttribute("Reservations", list_reservation);
			request.setAttribute("Vehicles", list_vehicle);
			request.setAttribute("nbOfReservation", list_reservation.size());
			request.setAttribute("nbOfVehicle", list_vehicle.size());
			
			requestDispatcher.forward(request, response);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
