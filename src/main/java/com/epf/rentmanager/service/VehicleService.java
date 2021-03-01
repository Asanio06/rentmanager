package com.epf.rentmanager.service;

import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;

public class VehicleService {

	private VehicleDao vehicleDao;
	public static VehicleService instance;

	private VehicleService() {
		this.vehicleDao = VehicleDao.getInstance();
	}

	public static VehicleService getInstance() {
		if (instance == null) {
			instance = new VehicleService();
		}

		return instance;
	}

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

	public int delete(Vehicle vehicle) throws ServiceException {

		try {
			return vehicleDao.delete(vehicle);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public int update(Vehicle vehicle) throws ServiceException {
		try {
			return vehicleDao.update(vehicle);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public Vehicle findById(long id) throws ServiceException {
		// TODO: récupérer un véhicule par son id

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
			throw new ServiceException("Erreur au niveau de la recherche dans la BDD");

		}

	}

	public List<Vehicle> findAll() throws ServiceException {
		// TODO: récupérer tous les clients

		try {

			return vehicleDao.findAll();

		} catch (DaoException e) {
			throw new ServiceException("Erreur au niveau de la recherche dans la BDD");

		}

	}
	
	public int nbOfVehicle() throws ServiceException {
		try {
			return vehicleDao.nbOfVehicle();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			throw new ServiceException("Erreur");
		}
	}

}
