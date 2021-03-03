package com.epf.rentmanager.ui.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.ReservationService;

public class ReservationListServlet extends HttpServlet {
	
	private static ReservationService reservationService = ReservationService.getInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/rents/list.jsp");
			request.setAttribute("Reservations", reservationService.findAll());
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		} 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
