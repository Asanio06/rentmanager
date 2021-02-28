package com.epf.rentmanager.ui;

import java.sql.Date;
import java.util.List;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.utils.IOUtils;

public class UiReservation {
	private static ReservationService reservation_service = ReservationService.getInstance();

	public static void action_reservation() {
		int choix;
		choix = IOUtils.readInt("Veuillez saisir le chiffre correspondant à votre action:\n"
				+ "1. Créer une réservation\n" + "2. Supprimer une réservation\n" + "3. Lister les réservations d'un client\n"
				+ "4. Lister les réservations d'un véhicule\n" + "5. Afficher toutes les réservations\n");
		switch (choix) {
		case 1:
			ui_createReservation();
			break;

		case 2:
			ui_deleteReservation();
			break;

		case 3:
			ui_findResaByClientId();
			break;

		case 4:
			ui_findResaByVehicleId();
			break;

		case 5:
			ui_findAllReservation();
			break;

		default:
			System.out.println("Choix non valide");
			// action_client();
			break;
		}
	}

	public static void ui_createReservation() {

		int vehicleId = IOUtils.readInt("Identifiant du véhicule:");
		int clientId = IOUtils.readInt("Identifiant du client: ");
		Date dateDebut = Date.valueOf(IOUtils.readDate("Date du début de la réservation:", true));
		Date dateFin = Date.valueOf(IOUtils.readDate("Date de fin", true));

		try {

			Reservation reservation = new Reservation();
			reservation.setVehicle_id(vehicleId);
			reservation.setClient_id(clientId);
			reservation.setDebut(dateDebut);
			reservation.setFin(dateFin);

			long id = reservation_service.create(reservation);

			System.out.println("Le réservation a été créé avec l'id :" + id);

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
	}



	public static void ui_deleteReservation() {
		try {

			long id = IOUtils.readInt("Saississez l'identifiant de la réservation: ");
			Reservation reservation = new Reservation();
			reservation.setId(id);
			reservation_service.delete(reservation);
			System.out.print("L'utilisateur a été supprimé");

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
	}
	
	public static void ui_findResaByClientId() {
		try {

			long clientId = IOUtils.readInt("Saississez l'identifiant du client pour qui vous souhaitez voir les réservations : ");
			List<Reservation> listResaOfClient = reservation_service.findResaByClientId(clientId);
			for(Reservation reservation : listResaOfClient) {
				IOUtils.print(reservation.toString());
			}

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
		
	}
	
	public static void ui_findResaByVehicleId() {
		try {

			long vehicleId = IOUtils.readInt("Saississez l'identifiant du véhicules dont vous souhaitez lister les réservations: ");
			List<Reservation> listResaOfVehicle = reservation_service.findResaByClientId(vehicleId);
			for(Reservation reservation : listResaOfVehicle) {
				IOUtils.print(reservation.toString());
			}

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
		
	}

	public static void ui_findAllReservation() {

		try {

			List<Reservation> list_reservation = reservation_service.findAll();

			for (Reservation reservation : list_reservation) {

				IOUtils.print(reservation.toString());
			}

		} catch (ServiceException e) {
			System.out.print(e.getMessage());
		}

	}

}
