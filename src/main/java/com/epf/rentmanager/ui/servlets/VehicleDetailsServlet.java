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

@WebServlet("/cars/details")
public class VehicleDetailsServlet extends HttpServlet {
	private static VehicleService vehicleService = VehicleService.getInstance();
	private static ReservationService reservationService = ReservationService.getInstance();
	private static ClientService clientService = ClientService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/vehicles/details.jsp");
			long vehicleId = Long.parseLong(request.getParameter("id"));
			Vehicle vehicle = vehicleService.findById(vehicleId);
			List<Client> list_client = clientService.findDistinctClientByVehicleUsed(vehicle);
			List<Reservation> list_reservation = reservationService.findResaByVehicleId(vehicleId);
			request.setAttribute("Clients", list_client);
			request.setAttribute("Reservations", list_reservation);
			request.setAttribute("vehicle", vehicle);
			request.setAttribute("nbOfReservation", list_reservation.size());
			request.setAttribute("nbOfClient", list_client.size());
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
