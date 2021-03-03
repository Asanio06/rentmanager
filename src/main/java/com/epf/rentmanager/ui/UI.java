package com.epf.rentmanager.ui;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.epf.rentmanager.utils.IOUtils;

import com.epf.rentmanager.dao.*;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;

public class UI {
	

	public static void main(String[] args) throws DaoException {
		// TODO Auto-generated method stub
		int choix; 
		choix = IOUtils.readInt("Veuillez saisir le chiffre correspondant à votre action:\n"
				+ "1. Agir sur les clients\n" + "2. Agir sur les véhicules\n" + "3. Agir sur les réservations\n");
		switch (choix) {

		case 1:
			UiClient.action_client();
			break;
		case 2:
			UiVehicle.action_vehicle();
			break;
		case 3: 
			//UiReservation.action_reservation();
			break;
			
		default:
			break;
		}
	}

	
}
