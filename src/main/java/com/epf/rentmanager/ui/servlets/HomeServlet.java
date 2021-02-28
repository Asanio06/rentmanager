package com.epf.rentmanager.ui.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;


@WebServlet("/home")
public class HomeServlet extends HttpServlet{
	private VehicleService vehicleService = VehicleService.getInstance();
	private ClientService clientService = ClientService.getInstance();
	private ReservationService reservationService = ReservationService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/home.jsp");
			request.setAttribute("nbOfVehicles", vehicleService.nbOfVehicle());
			request.setAttribute("nbOfUtilisateurs", clientService.nbOfClient());
			request.setAttribute("nbOfReservations", reservationService.nbOfResa());
			
			requestDispatcher.forward(request, response);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
		
	}
	

}
