package com.epf.rentmanager.ui.servlets;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;

@WebServlet("/clients/create")
public class ClientCreateServlet extends HttpServlet{
	private static ClientService clientService = ClientService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher requestDispacher = request.getRequestDispatcher("/WEB-INF/views/clients/create.jsp");
		requestDispacher.forward(request, response);
		
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			Client client = new Client();
			client.setNom(request.getParameter("last_name"));
			client.setPrenom(request.getParameter("first_name"));
			client.setEmail(request.getParameter("email"));
			client.setNaissance(Date.valueOf("2001-01-01"));
			clientService.create(client);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
		
		response.sendRedirect(request.getServerName()+":"+request.getServerPort()+ request.getContextPath()+ "/clients");
		
	}

}
