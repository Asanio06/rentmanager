package com.epf.rentmanager.ui.servlets;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.utils.IOUtils;

@WebServlet("/clients/create")
public class ClientCreateServlet extends HttpServlet {
	@Autowired
	private ClientService clientService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher requestDispacher = request.getRequestDispatcher("/WEB-INF/views/clients/create.jsp");
		requestDispacher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean success = false;
		String errorMessage = "";
		try {

			if (request.getParameter("last_name").isEmpty() || request.getParameter("first_name").isEmpty()) {
				throw new ServiceException("Nom ou prénom incorrect");
			}
			if (request.getParameter("naissance").isEmpty()) {
				throw new ServiceException("Veuillez saisir une date de naissance");
			}
			if (!IOUtils.isValidMail(request.getParameter("email"))) {
				throw new ServiceException("Adresse mail non valide");
			}

			Client client = new Client();
			client.setNom(request.getParameter("last_name"));
			client.setPrenom(request.getParameter("first_name"));
			client.setEmail(request.getParameter("email"));
			client.setNaissance(Date.valueOf(request.getParameter("naissance")));

			clientService.create(client);
			success = true;

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			errorMessage = e.getMessage();

		} finally {
			if (success) {
				response.sendRedirect("http://localhost:8080/rentmanager/clients");
			} else {
				request.setAttribute("error_message", errorMessage);
				doGet(request, response);
			}
		}

	}

}
