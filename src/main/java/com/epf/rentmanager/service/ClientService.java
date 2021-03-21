package com.epf.rentmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.ClientDao;

@Service
public class ClientService {

	private int nombreDeCaractereMinimalInName = 3;
	private int ageMinimalDuClient = 18;
	private ClientDao clientDao;
	private ReservationService reservationService;

	@Autowired
	public ClientService(ClientDao clientDao, ReservationService reservationService) {
		this.clientDao = clientDao;
		this.reservationService = reservationService;
	}

	/**
	 * Permet d'utiliser une requête SQL afin d'insérer un client dans la base de
	 * données
	 * 
	 * @param client l'objet client représentant le client qu'on souhaite ajouter à
	 *               la base de données
	 * @return l'identifiant du client qui vient d'être insérer dans la base de
	 *         données
	 * @throws ServiceException
	 */
	public long create(Client client) throws ServiceException {

		if (client.getNom().length() < nombreDeCaractereMinimalInName) {
			throw new ServiceException("Le nom doit contenir au moins 3 caractères");
		}

		if (client.getPrenom().length() < nombreDeCaractereMinimalInName) {
			throw new ServiceException("Le prénom doit contenir au moins 3 caractères");
		}

		if (client.getAge() <= ageMinimalDuClient) {
			throw new ServiceException("Vous devez avoir 18 ans ou plus");
		}

		if (verifyIfEmailIsUse(client)) {
			throw new ServiceException("L'adresse mail est déjà utilisé");
		}

		client.setNom(client.getNom().toUpperCase());

		try {

			return clientDao.create(client);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Permet d'utiliser une requête SQL afin de supprimer un client dans la base de
	 * données
	 * 
	 * @param client objet de la classe Client représentant le client qu'on souhaite
	 *               ajouter à la base de données
	 * @return nombre de ligne modifiés dans la base de données
	 * @throws ServiceException
	 */
	public long deleteClient(Client client) throws ServiceException {

		try {
			List<Reservation> list_reservation_of_client = reservationService.findResaByClientId(client.getId());
			for (Reservation reservation : list_reservation_of_client) {
				reservationService.delete(reservation);
			}

			return clientDao.delete(client);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}

	}

	/**
	 * Permet d'utiliser une requête SQL afin de mettre à jour un client dans la
	 * base de données
	 * 
	 * @param client l'objet client représentant le client qu'on souhaite ajouter à
	 *               la base de données
	 * @return true si la mise à jour à eu lieu; false sinon
	 * @throws ServiceException
	 */
	public boolean updateClient(Client client) throws ServiceException {

		if (client.getNom().length() < nombreDeCaractereMinimalInName) {
			throw new ServiceException("Le nom doit contenir au moins 3 caractères");
		}

		if (client.getPrenom().length() < nombreDeCaractereMinimalInName) {
			throw new ServiceException("Le prénom doit contenir au moins 3 caractères");
		}

		if (client.getAge() <= ageMinimalDuClient) {
			throw new ServiceException("Vous devez avoir 18 ans ou plus");
		}

		if (verifyIfEmailIsUse(client)) {
			throw new ServiceException("L'adresse mail est déjà utilisé");
		}
		System.out.print(client);
		client.setNom(client.getNom().toUpperCase());

		try {
			clientDao.update(client);
			return true;

		} catch (DaoException e) {

			throw new ServiceException(e.getMessage());
		}

	}

	/**
	 * Permet de récupérer un client de la base de données en fonction de son id
	 * 
	 * @param id l'identifiant du client qu'on souhaite récupérer dans la base de
	 *           données
	 * @return le client récupéré de la base de données
	 * @throws ServiceException
	 */
	public Client findById(long id) throws ServiceException {

		Client client;
		try {
			Optional<Client> opt_client = clientDao.findById(id);

			if (opt_client.isPresent()) {
				client = opt_client.get();
			} else {
				throw new ServiceException("L'utilisateur n'existe pas");

			}

			return client;

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());

		}

	}

	/**
	 * Permet de vérifier si une adresse mail est déjà utiliser par un autre client
	 * 
	 * @param clientATester le client dont on veut vérifier la base de données
	 * @return true si l'email existe déjà et est utilisé par quelqu'un ayant un
	 *         identifiant différent; false sinon
	 * @throws ServiceException
	 */
	public boolean verifyIfEmailIsUse(Client clientATester) throws ServiceException {
		Client client;
		try {
			Optional<Client> opt_client = clientDao.findByEmail(clientATester.getEmail());

			if (opt_client.isPresent()) {
				client = opt_client.get();
				if (clientATester.getId() == client.getId()) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());

		}

	}

	/**
	 * Permet d'obtenir la liste des clients présents dans la base de donnés
	 * 
	 * @return La liste des clients présents dans la base de données
	 * @throws ServiceException
	 */
	public List<Client> findAll() throws ServiceException {

		try {

			return clientDao.findAll();

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());

		}
	}

	/**
	 * Permet de récupérer la liste des clients distinct ayant réserver un véhicule
	 * particulier
	 * 
	 * @param vehicle le vehicule sur lequel on souhaite appliquer la recherche
	 * @return la liste des clients distinct ayant réserver un véhicule particulier
	 * @throws ServiceException
	 */
	public List<Client> findDistinctClientByVehicleUsed(Vehicle vehicle) throws ServiceException {

		try {

			return clientDao.findDistinctClientByVehicleUsed(vehicle);

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());

		}

	}

	/**
	 * @return le nombre de client présent dans la base de données
	 * @throws ServiceException
	 */
	public int nbOfClient() throws ServiceException {
		try {
			return clientDao.nbOfClient();
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
