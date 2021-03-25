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
import com.epf.rentmanager.dao.VehicleDao;

@Service
public class VehicleService {

	private VehicleDao vehicleDao;
	private static ReservationService reservationService;

	@Autowired
	private VehicleService(VehicleDao vehicleDao, ReservationService reservationService) {
		this.vehicleDao = vehicleDao;
		this.reservationService = reservationService;
	}

	/**
	 * Permet de créer un véhicule dans la base de données si les contraintes métier
	 * sont validés
	 * 
	 * @param vehicle Le véhicule qui sera créé dans la base de données
	 * @return l'identifiant du véhicule créé dans la base de données
	 * @throws ServiceException
	 */
	public long create(Vehicle vehicle) throws ServiceException {
		// TODO: créer un véhicule; Verification du constructeur; Verification que
		// nb_places >1
		if (vehicle.getConstructeur().isEmpty()) {
			throw new ServiceException("Veuillez Saisir un constructeur");
		}

		try {

			return vehicleDao.create(vehicle);
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la création de l'utilisateur dans la BDD");
		}
	}

	/**
	 * Permet de supprimer un véhicule de la base de données
	 * 
	 * @param vehicle Le véhicule qui doit être supprimé de la base de données
	 * @return le nombre de ligne affecté par la requête SQL
	 * @throws ServiceException
	 */
	public int delete(Vehicle vehicle) throws ServiceException {

		try {
			List<Reservation> list_reservation_of_vehicle = reservationService.findResaByVehicleId(vehicle.getId());
			for (Reservation reservation : list_reservation_of_vehicle) {
				reservationService.delete(reservation);
			}
			return vehicleDao.delete(vehicle);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Permet de mettre à jour un véhicule de la base de données
	 * 
	 * @param vehicle Le véhicule qui doit être mis à jour dans la base de données
	 * @return le nombre de ligne affecté par la requête SQL
	 * @throws ServiceException
	 */
	public int update(Vehicle vehicle) throws ServiceException {
		try {
			return vehicleDao.update(vehicle);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Permet de récupérer un véhicule de la base de données en fonction de son id
	 * 
	 * @param id l'identifiant du véhicule qu'on souhaite récupérer dans la base de
	 *           données
	 * @return le véhicule qu'on souhaite récupéré de la base de données
	 * @throws ServiceException
	 */
	public Vehicle findById(long id) throws ServiceException {

		Vehicle vehicle;

		try {
			Optional<Vehicle> opt_vehicle = vehicleDao.findById(id);

			if (opt_vehicle.isPresent()) {
				vehicle = opt_vehicle.get();
			} else {
				throw new ServiceException("Le véhicule n'existe pas");

			}

			return vehicle;

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());

		}

	}

	/**
	 * Permet de récupérer la liste des véhicules présents dans la base de données
	 * 
	 * @return La liste des véhicules présents dans la base de données
	 * @throws ServiceException
	 */
	public List<Vehicle> findAll() throws ServiceException {

		try {

			return vehicleDao.findAll();

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());

		}

	}

	
	/**
	 * Permet de récupérer la liste des véhicules distinct réservé par un client
	 * particulier
	 * 
	 * @param client Le client pour qui on souhaite obtenir les véhicules réservés
	 * @return La liste des véhicules distincts réservés par le client
	 * @throws ServiceException
	 */
	public List<Vehicle> findDistinctVehiclesReservedByClient(Client client) throws ServiceException {

		try {

			return vehicleDao.findDistinctVehiclesReservedByClient(client);

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());

		}

	}

	/**
	 * Permet de récupérer le nombre de véhicule présent dans la base de données
	 * 
	 * @return Le nombre de véhicule présent dans la base de données
	 * @throws ServiceException
	 */
	public int nbOfVehicle() throws ServiceException {
		try {
			return vehicleDao.nbOfVehicle();
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
