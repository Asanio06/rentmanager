package com.epf.rentmanager.ui.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;


@WebServlet("/cars/delete")
public class VehicleDeleteVehicle extends HttpServlet{

	private static VehicleService vehicleService = VehicleService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			long vehicleId = Long.parseLong(request.getParameter("id"));
			Vehicle vehicle = new Vehicle();
			vehicle.setId(vehicleId);
			vehicleService.delete(vehicle);
			response.sendRedirect("http://localhost:8080/rentmanager/cars");
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
