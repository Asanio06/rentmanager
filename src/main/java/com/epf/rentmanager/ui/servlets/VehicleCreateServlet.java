package com.epf.rentmanager.ui.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;

@WebServlet("/cars/create")
public class VehicleCreateServlet extends HttpServlet{
	
	@Autowired
	private VehicleService vehicleService;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher requestDispacher = request.getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp");
		requestDispacher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean success = false;
		String errorMessage = "";
		
		
		try {
			Vehicle vehicle = new Vehicle();
			vehicle.setConstructeur(request.getParameter("manufacturer"));
			vehicle.setModele(request.getParameter("modele"));
			vehicle.setNb_places(Short.parseShort(request.getParameter("seats")));
			vehicleService.create(vehicle);
			success = true;
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			errorMessage =  e.getMessage();
		}finally {
			if(success) {
				response.sendRedirect("http://localhost:8080/rentmanager/cars");
			}else {
				request.setAttribute("error_message", errorMessage);
				doGet(request, response);
			}
		}
		
		
	}
}
