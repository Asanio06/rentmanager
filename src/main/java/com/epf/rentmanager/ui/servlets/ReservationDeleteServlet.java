package com.epf.rentmanager.ui.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;

@WebServlet("/rents/delete")
public class ReservationDeleteServlet extends HttpServlet{
	
	private static ReservationService reservationService = ReservationService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			long reservationId = Long.parseLong(request.getParameter("id"));
			Reservation reservation = reservationService.findById(reservationId);
			reservation.setId(reservationId);
			reservationService.delete(reservation);
			response.sendRedirect("http://localhost:8080/rentmanager/rents");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
	}

}
