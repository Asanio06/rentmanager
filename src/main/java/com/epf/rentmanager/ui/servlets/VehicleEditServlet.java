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

@WebServlet("/cars/edit")
public class VehicleEditServlet extends HttpServlet {

	@Autowired
	private VehicleService vehicleService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean success = false;
		String errorMessage = "";
		try {
			if (request.getParameter("manufacturer").isEmpty()) {
				throw new ServiceException("Veuillez saisir un constructeur");
			}

			if (request.getParameter("modele").isEmpty()) {
				throw new ServiceException("Veuillez saisir un modele");
			}

			if (Short.parseShort(request.getParameter("seats")) < 2
					|| Short.parseShort(request.getParameter("seats")) > 9) {
				throw new ServiceException("Le nombre de place doit être compris en 2 et 9");
			}
			Vehicle vehicle = new Vehicle();
			vehicle.setId(Long.parseLong(request.getParameter("id")));
			vehicle.setConstructeur(request.getParameter("manufacturer"));
			vehicle.setModele(request.getParameter("modele"));
			vehicle.setNb_places(Short.parseShort(request.getParameter("seats")));
			vehicleService.update(vehicle);
			success = true;
		} catch (ServiceException e) {
			errorMessage = e.getMessage();
		} finally {
			if (success) {
				response.sendRedirect("http://localhost:8080/rentmanager/cars");
			} else {
				request.setAttribute("error_message", errorMessage);
				doGet(request, response);
			}
		}

	}

}
