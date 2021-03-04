package com.epf.rentmanager.ui.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.VehicleService;

@WebServlet("/cars/edit")
public class VehicleEditServlet extends HttpServlet{
	
	private static VehicleService vehicleService = VehicleService.getInstance();
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/vehicles/edit.jsp");
			long vehicleId = Long.parseLong(request.getParameter("id"));
			request.setAttribute("vehicle", vehicleService.findById(vehicleId));
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		} 
	}

}
