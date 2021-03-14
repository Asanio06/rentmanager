package com.epf.rentmanager.ui;

import java.sql.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.utils.IOUtils;

public class UiReservation {
	private static ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);

	private static ReservationService reservation_service = context.getBean(ReservationService.class) ;

	public static void action_reservation() {
		int choix;
		choix = IOUtils.readInt("Veuillez saisir le chiffre correspondant à votre action:\n"
				+ "1. Créer une réservation\n" + "2. Supprimer une réservation\n" + "3. Lister les réservations d'un client\n"
				+ "4. Lister les réservations d'un véhicule\n" + "5. Afficher toutes les réservations\n"
						+ "6.Lister les réservations des 30 derniers jours pour un vehicule\n");
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
			
		case 6:
			ui_find30LastResaByVehicleId();
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
			Client client = new Client();
			client.setId(clientId);
			reservation.setClient(client);
			
			Vehicle vehicle = new Vehicle();
			vehicle.setId(vehicleId);
			reservation.setVehicle(vehicle);
			
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
	
	public static void ui_find30LastResaByVehicleId() {
		//try {

			long vehicleId = IOUtils.readInt("Saississez l'identifiant du véhicules dont vous souhaitez lister les réservations: ");
			Reservation reservationATester = new Reservation();
			reservationATester.setDebut(Date.valueOf("2021-03-08"));
			reservationATester.setFin(Date.valueOf("2021-03-09"));
			Vehicle vehicle = new Vehicle();
			vehicle.setId(vehicleId);
			reservationATester.setVehicle(vehicle);
			reservationATester.setClient(new Client());
			try {
				reservation_service.nbOfDaysInARowTheVehicleIsReserved(reservationATester);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				System.out.print(e.getMessage());
			}
			
			/*for(Reservation reservation : listResaOfVehicle) {
				IOUtils.print(reservation.toString());
			}*

		} catch (ServiceException e) {
			System.out.print(e.getMessage());
		}*/
		
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
