package com.epf.rentmanager.service;

import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;

public class ReservationService {
	private ReservationDao reservationDao;
	public static ReservationService instance;

	private ReservationService() {
		this.reservationDao = reservationDao.getInstance();
	}

	public static ReservationService getInstance() {
		if (instance == null) {
			instance = new ReservationService();
		}

		return instance;
	}

	public long create(Reservation reservation) throws ServiceException {

		try {
			return reservationDao.create(reservation);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public int delete(Reservation reservation) throws ServiceException {

		try {
			return reservationDao.delete(reservation);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(e.getMessage());
		}

	}

	public List<Reservation> findResaByClientId(long clientId) throws ServiceException {

		try {
			return reservationDao.findResaByClientId(clientId);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(e.getMessage());
		}

	}

	public List<Reservation> findResaByVehicleId(long vehiculeId) throws ServiceException {

		try {
			return reservationDao.findResaByVehicleId(vehiculeId);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(e.getMessage());
		}

	}

	public List<Reservation> findAll() throws ServiceException {
		// TODO: récupérer tous les clients

		try {
			return reservationDao.findAll();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(e.getMessage());
		}

	}
	
	public long nbOfResa() throws ServiceException {
		try {
			return reservationDao.nbOfResa();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			throw new ServiceException("Erreur");
		}
	}

}
