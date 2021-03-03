package com.epf.rentmanager.ui.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;

@WebServlet("/cars/create")
public class VehicleCreateServlet extends HttpServlet{
	private VehicleService vehicleService = VehicleService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher requestDispacher = request.getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp");
		requestDispacher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Vehicle vehicle = new Vehicle();
		vehicle.setConstructeur(request.getParameter("manufacturer"));
		vehicle.setModele(request.getParameter("modele"));
		vehicle.setNb_places(Short.parseShort(request.getParameter("seats")));
		try {
			vehicleService.create(vehicle);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
		response.sendRedirect(request.getServerName()+":"+request.getServerPort()+ request.getContextPath()+"/cars");
		
	}
}
