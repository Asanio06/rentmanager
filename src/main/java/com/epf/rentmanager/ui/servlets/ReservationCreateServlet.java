package com.epf.rentmanager.ui.servlets;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {

	@Autowired
	private ClientService clientService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private ReservationService reservationService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean success = false;
		String errorMessage = "";
		try {

			if (Long.parseLong(request.getParameter("client")) == 0) {
				throw new ServiceException("Selection du client incorrecte");
			}

			if (Long.parseLong(request.getParameter("car")) == 0) {
				throw new ServiceException("Selection du vehicule incorrecte");
			}

			if (request.getParameter("begin").isEmpty() || request.getParameter("begin").isEmpty()) {
				throw new ServiceException("Les dates de début et de fin de réservation semblent incorrectes");
			}

			Reservation reservation = new Reservation();
			reservation.setId(0);
			Client client = new Client();
			client.setId(Long.parseLong(request.getParameter("client")));
			reservation.setClient(client);

			Vehicle vehicle = new Vehicle();
			vehicle.setId(Long.parseLong(request.getParameter("car")));
			reservation.setVehicle(vehicle);

			reservation.setDebut(Date.valueOf(request.getParameter("begin")));
			reservation.setFin(Date.valueOf(request.getParameter("end")));
			reservationService.create(reservation);
			success = true;
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			errorMessage = e.getMessage();
		} finally {
			if (success) {
				response.sendRedirect("http://localhost:8080/rentmanager/rents");
			} else {
				request.setAttribute("error_message", errorMessage);
				doGet(request, response);
			}
		}

	}
}
