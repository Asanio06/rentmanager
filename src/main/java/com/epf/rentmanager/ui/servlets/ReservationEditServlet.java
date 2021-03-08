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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;

@WebServlet("/rents/edit")
public class ReservationEditServlet extends HttpServlet {
	
	@Autowired
	private  ReservationService reservationService;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/rents/edit.jsp");
			long reservationId = Long.parseLong(request.getParameter("id"));
			Reservation reservation = reservationService.findById(reservationId);
			request.setAttribute("reservation", reservation);
			requestDispatcher.forward(request, response);
		}  catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		} 
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean success = false;
		String errorMessage = "";
		try {
			long reservationId = Long.parseLong(request.getParameter("id"));
			Reservation reservation = reservationService.findById(reservationId);
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date beginDate = format.parse(request.getParameter("begin"));
			java.util.Date endDate = format.parse(request.getParameter("end"));

			reservation.setDebut(new java.sql.Date(beginDate.getTime())) ;
			reservation.setFin(new java.sql.Date(endDate.getTime()));
			reservationService.update(reservation);
			success = true;
		}  catch (ServiceException e) {
			// TODO Auto-generated catch block
			errorMessage = e.getMessage();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			errorMessage = e.getMessage();
		}finally {
			if(success) {
				response.sendRedirect("http://localhost:8080/rentmanager/rents");
			}else {
				request.setAttribute("error_message", errorMessage);
				doGet(request, response);
			}
		}
		
		
		
	}

}
