package com.epf.rentmanager.ui.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;


@WebServlet("/clients/edit")
public class ClientEditServlet extends HttpServlet{
	
	private static ClientService clientService = ClientService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/clients/edit.jsp");
			long clientId = Long.parseLong(request.getParameter("id"));
			Client client = clientService.findById(clientId);
			request.setAttribute("client",client );
			requestDispatcher.forward(request, response);
		}catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
		
	}

}
