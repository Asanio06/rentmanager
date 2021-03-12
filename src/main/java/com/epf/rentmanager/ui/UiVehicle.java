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
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.utils.IOUtils;

public class UiVehicle {

	static ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);

	private static VehicleService vehicle_service = context.getBean(VehicleService.class);

	public static void action_vehicle() {
		int choix;
		
		choix = IOUtils.readInt("Veuillez saisir le chiffre correspondant à votre action:\n" + "1. Créer un vehicule\n"
				+ "2. Modifier un vehicule\n" + "3. Supprimer un vehicule\n"
				+ "4. Trouver un vehicule à partir de son id\n" + "5. Afficher tout les vehicules\n"
						+ "6. Afficher le nombre de véhicules");
		switch (choix) {
		case 1:
			ui_createVehicle();
			break;

		case 2:
			ui_updateVehicle();
			break;

		case 3:
			ui_deleteVehicule();
			break;

		case 4:
			ui_findVehicle();
			break;

		case 5:
			ui_findAllVehicle();
			break;
			
		case 6:
			ui_displayNbOfVehicles();
			break;

		default:
			System.out.println("Choix non valide");
			//action_client();
			break;
		}
	}

	public static void ui_createVehicle() {

		String constructeur = IOUtils.readString("Constructeur:", true);
		String modele = IOUtils.readString("Modèle:", true);
		short nb_places = IOUtils.readShort("Nombre de place:");


		try {

			Vehicle vehicle = new Vehicle();
			vehicle.setConstructeur(constructeur);
			vehicle.setModele(modele);
			vehicle.setNb_places(nb_places);
			

			long id = vehicle_service.create(vehicle) ;

			System.out.println("Le véhicule a été créé avec l'id :" + id);

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
	}
	
	public static void ui_updateVehicle() {
		long id = IOUtils.readInt(
				"Saississez l'identifiant du véhicule" + " sur lequel vous souhaitez appliquer des modifications");
		try {

			Vehicle vehicle = vehicle_service.findById(id);
			
			IOUtils.print(vehicle.toString());
			IOUtils.print("Appuyez sur entrer à chaque fois que vous ne " + "souhaitez pas modifier un élément");
			String constructeur = IOUtils.readString("Nouveau constructeur:", false);
			
			if (!(vehicle.getConstructeur().equals(constructeur)) && !constructeur.isEmpty()) {
				vehicle.setConstructeur(constructeur);
			}
			
			String modele = IOUtils.readString("Modele:", false);
			if (!(vehicle.getModele().equals(modele)) && !constructeur.isEmpty()) {
				vehicle.setModele(modele);
			}
			
			
			short nb_places = IOUtils.readShort("Nombre de place:");
			
			int modification = IOUtils
					.readInt("Saisissez le chiffre " + "1 si vous souhaitez modifier le nombre de places");

			if (modification == 1) {

				vehicle.setNb_places(nb_places);
			}
			
			vehicle_service.update(vehicle);
			

			


		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());

		}
	}
	
	public static void ui_deleteVehicule() {
		try {
			long id = IOUtils.readInt(
					"Saississez l'identifiant du vehicule" + " que vous souhaitez supprimer");
			Vehicle vehicle = new Vehicle();
			vehicle.setId(id);
			vehicle_service.delete(vehicle);
			System.out.print("L'utilisateur a été supprimé");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
		
	}
	
	public static void ui_findVehicle() {
		try {

			long id = IOUtils.readInt("Saississez l'identifiant du véhicule");
			Vehicle vehicle = vehicle_service.findById(id);

			IOUtils.print(vehicle.toString());

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
	}
	
	public static void ui_findAllVehicle() {
		
		try {

			List<Vehicle> list_vehicle = vehicle_service.findAll();

			for (Vehicle vehicle : list_vehicle) {
				
				IOUtils.print(vehicle.toString());
			}

		} catch (ServiceException e) {
			System.out.print(e.getMessage());
		}
		
	}
	
	public static void ui_displayNbOfVehicles() {
		try {
			System.out.print(vehicle_service.nbOfVehicle());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			IOUtils.print(e.getMessage());
		}
	}

}
