package com.epf.rentmanager.ui;

import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.utils.IOUtils;

public class UiClient {
	
	private static ClientService client_service = ClientService.getInstance();

	public static void action_client() {
		int choix;
		choix = IOUtils.readInt("Veuillez saisir le chiffre correspondant à votre action:\n" + "1. Créer un client\n"
				+ "2. Modifier un client\n" + "3. Supprimer un client\n" + "4. Trouver un client à partir de son id\n"
				+ "5. Afficher tout les clients\n");
		switch (choix) {
		case 1:
			ui_createClient();
			break;

		case 2:
			ui_updateClient();
			break;

		case 3:
			ui_deleteClient();
			break;

		case 4:
			ui_findClient();
			break;

		case 5:
			ui_findAllClient();
			break;

		default:
			System.out.println("Choix non valide");
			action_client();
			break;
		}
	}

	public static void ui_createClient() {

		String nom_client = IOUtils.readString("Nom du client:", true);
		String prenom_client = IOUtils.readString("Prenom du client:", true);

		String email_client = "";

		while (!isValidMail(email_client)) {

			email_client = IOUtils.readString("Email: ", true);
		}

		Date naissance_client = Date.valueOf(IOUtils.readDate("Date de naissance:", true));

		try {

			Client client = new Client();
			client.setNom(nom_client);
			client.setPrenom(prenom_client);
			client.setEmail(email_client);
			client.setNaissance(naissance_client);

			long id = client_service.create(client);

			System.out.println("Le client a été créé avec l'id :" + id);

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
	}

	public static void ui_updateClient() {
		long id = IOUtils.readInt(
				"Saississez l'identifiant du client" + " sur lequel vous souhaitez appliquer des modifications");
		try {

			Client client = client_service.findById(id);
			IOUtils.print(client.toString());
			IOUtils.print("Appuyez sur entrer à chaque fois que vous ne " + "souhaitez pas modifier un élément");
			String nom = IOUtils.readString("Nouveau nom:", false);

			if (!(client.getNom().equals(nom)) && !nom.isEmpty()) {
				client.setNom(nom);

			}

			String prenom = IOUtils.readString("Nouveau prénom:", false);

			if (!(client.getPrenom().equals(prenom)) && !prenom.isEmpty()) {
				client.setPrenom(prenom);

			}

			String email = "NaN";
			while (!isValidMail(email) && !email.isEmpty()) {
				email = IOUtils.readString("Nouveau email:", false);
			}

			if (!email.isEmpty()) {

				client.setEmail(email);

			}

			int modification_date = IOUtils
					.readInt("Saisissez le chiffre " + "1 si vous souhaitez modifier la date de naissance");

			if (modification_date == 1) {

				Date naissance = Date.valueOf(IOUtils.readDate("Nouvelle date de naissance", false));
				client.setNaissance(naissance);
			}

			client_service.updateClient(client);

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());

		}

	}

	public static void ui_deleteClient() {
		try {
			long id = IOUtils.readInt(
					"Saississez l'identifiant du client" + " sur lequel vous souhaitez appliquer des modifications");
			Client client = new Client();
			client.setId(id);
			client_service.deleteClient(client);
			System.out.print("L'utilisateur a été supprimé");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
	}

	public static void ui_findClient() {

		try {

			long id = IOUtils.readInt("Saississez l'identifiant du client");
			Client client = client_service.findById(id);

			IOUtils.print(client.toString());

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
	}

	public static void ui_findAllClient() {

		try {

			List<Client> list_client = client_service.findAll();

			for (Client client : list_client) {
				
				IOUtils.print(client.toString());
			}

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}

	}

	public static boolean isValidMail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

}
