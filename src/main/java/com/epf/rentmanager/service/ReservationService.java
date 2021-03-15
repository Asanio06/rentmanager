package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.utils.IOUtils;

@Service
public class ReservationService {
	private ReservationDao reservationDao;

	@Autowired
	private ReservationService(ReservationDao reservationDao) {
		this.reservationDao = reservationDao;
	}

	public long create(Reservation reservation) throws ServiceException {

		try {
			if (vehicleIsAlreadyBookeddAtTheSameTime(reservation)) {
				throw new ServiceException("Le véhicule est déjà réservé sur la même période");
			}
			
			if(nbOfDaysInARowTheVehicleIsReservedBySameClient(reservation) > 7) {
				throw new ServiceException("Le véhicule ne dois pas être réservé plus de 7 jours de suite par le même client");
			}

			if (nbOfDaysInARowTheVehicleIsReserved(reservation) >= 30) {
				throw new ServiceException("Le véhicule ne doit pas être réservé plus de 30 jours de suite");
			}

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

	public int update(Reservation reservation) throws ServiceException {

		try {
			if (vehicleIsAlreadyBookeddAtTheSameTime(reservation)) {
				throw new ServiceException("Le véhicule est déjà réservé sur la même période");
			}

			if(nbOfDaysInARowTheVehicleIsReservedBySameClient(reservation) > 7) {
				throw new ServiceException("Le véhicule ne dois pas être réservé plus de 7 jours de suite par le même client");
			}
			
			if (nbOfDaysInARowTheVehicleIsReserved(reservation) >= 30) {
				throw new ServiceException("Le véhicule ne doit pas être réservé plus de 30 jours de suite");
			}
			
			return reservationDao.update(reservation);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		} 
	}

	public Reservation findById(long id) throws ServiceException {
		// TODO: récupérer un client par son id
		Reservation reservation;
		try {
			Optional<Reservation> opt_reservation = reservationDao.findById(id);

			if (opt_reservation.isPresent()) {
				reservation = opt_reservation.get();
			} else {
				throw new ServiceException("La réservation n'existe pas");

			}

			return reservation;

		} catch (DaoException e) {
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

	public List<Reservation> findResaOf30LastDayByVehicle(Reservation reservation) throws ServiceException {

		try {
			return reservationDao.findResaOf30LastDayByVehicle(reservation);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(e.getMessage());
		}

	}

	public List<Reservation> findResaOf30DayAfterByVehicle(Reservation reservation) throws ServiceException {

		try {
			return reservationDao.findResaOf30DayAfterByVehicle(reservation);
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

	public int nbOfResa() throws ServiceException {
		try {
			return reservationDao.nbOfResa();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			throw new ServiceException("Erreur");
		}
	}

	public long nbOfDaysInARowTheVehicleIsReserved(Reservation reservationATester) throws ServiceException {
		long nb_afile_30daysBefore = 0;
		long nb_afile_30daysAfter = 0;
		long nb_jour_resa_consecutive = 0;
		try {

			List<Reservation> list_resa_qui_se_suive_30_daysBefore = new ArrayList<Reservation>();
			List<Reservation> list_resa_qui_se_suive_30_daysAfter = new ArrayList<Reservation>();
			List<Reservation> list_resa_30daysBefore = reservationDao.findResaOf30LastDayByVehicle(reservationATester);
			List<Reservation> list_resa_30daysAfter = reservationDao.findResaOf30DayAfterByVehicle(reservationATester);
			list_resa_30daysBefore.add(0, reservationATester);
			list_resa_30daysAfter.add(0, reservationATester);
			long nb_jour_reservation = IOUtils.dateDiff(reservationATester.getDebut(), reservationATester.getFin()) + 1;

			for (int i = 0; i < list_resa_30daysBefore.size() - 1; i++) {

				if (list_resa_30daysBefore.get(i).getDebut()
						.equals(IOUtils.addDays(list_resa_30daysBefore.get(i + 1).getFin(), 1))) {
					list_resa_qui_se_suive_30_daysBefore.add(list_resa_30daysBefore.get(i + 1));
				} else {
					break;
				}
			}

			for (int i = 0; i < list_resa_30daysAfter.size() - 1; i++) {
				if (list_resa_30daysAfter.get(i).getFin()
						.equals(IOUtils.subtractDays(list_resa_30daysAfter.get(i + 1).getDebut(), 1))) {

					list_resa_qui_se_suive_30_daysAfter.add(list_resa_30daysAfter.get(i + 1));
				} else {
					break;
				}

			}

			for (Reservation reservation : list_resa_qui_se_suive_30_daysAfter) {
				nb_afile_30daysAfter += IOUtils.dateDiff(reservation.getDebut(), reservation.getFin()) + 1;

			}

			for (Reservation reservation : list_resa_qui_se_suive_30_daysBefore) {
				nb_afile_30daysBefore += IOUtils.dateDiff(reservation.getDebut(), reservation.getFin()) + 1;

			}

			nb_jour_resa_consecutive = nb_afile_30daysBefore + nb_afile_30daysAfter + nb_jour_reservation;

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
		return nb_jour_resa_consecutive;

	}
	
	public long nbOfDaysInARowTheVehicleIsReservedBySameClient(Reservation reservationATester) throws ServiceException {
		long nb_afile_7daysBefore = 0;
		long nb_afile_7daysAfter = 0;
		long nb_jour_resa_consecutive = 0;
		try {

			List<Reservation> list_resa_qui_se_suive_7_daysBefore = new ArrayList<Reservation>();
			List<Reservation> list_resa_qui_se_suive_7_daysAfter = new ArrayList<Reservation>();
			List<Reservation> list_resa_7daysBefore = reservationDao.findResaOf7LastDayByVehicleAndClient(reservationATester);
			List<Reservation> list_resa_7daysAfter = reservationDao.findResaOf7DayAfterByVehicleAndClient(reservationATester);
			list_resa_7daysBefore.add(0, reservationATester);
			list_resa_7daysAfter.add(0, reservationATester);
			long nb_jour_reservation = IOUtils.dateDiff(reservationATester.getDebut(), reservationATester.getFin()) + 1;

			for (int i = 0; i < list_resa_7daysBefore.size() - 1; i++) {

				if (list_resa_7daysBefore.get(i).getDebut()
						.equals(IOUtils.addDays(list_resa_7daysBefore.get(i + 1).getFin(), 1))) {
					list_resa_qui_se_suive_7_daysBefore.add(list_resa_7daysBefore.get(i + 1));
				} else {
					break;
				}
			}

			for (int i = 0; i < list_resa_7daysAfter.size() - 1; i++) {
				if (list_resa_7daysAfter.get(i).getFin()
						.equals(IOUtils.subtractDays(list_resa_7daysAfter.get(i + 1).getDebut(), 1))) {

					list_resa_qui_se_suive_7_daysAfter.add(list_resa_7daysAfter.get(i + 1));
				} else {
					break;
				}

			}

			for (Reservation reservation : list_resa_qui_se_suive_7_daysAfter) {
				nb_afile_7daysAfter += IOUtils.dateDiff(reservation.getDebut(), reservation.getFin()) + 1;

			}

			for (Reservation reservation : list_resa_qui_se_suive_7_daysBefore) {
				nb_afile_7daysBefore += IOUtils.dateDiff(reservation.getDebut(), reservation.getFin()) + 1;

			}

			nb_jour_resa_consecutive = nb_afile_7daysBefore + nb_afile_7daysAfter + nb_jour_reservation;

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
		return nb_jour_resa_consecutive;

	}

	public boolean vehicleIsAlreadyBookeddAtTheSameTime(Reservation reservationATester) throws ServiceException {
		List<Reservation> list_resa_of_vehicle_in_same_period;
		try {
			list_resa_of_vehicle_in_same_period = reservationDao
					.findResaOfVehicleInPeriodOfOtherReservation(reservationATester);
			if (list_resa_of_vehicle_in_same_period.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}

	}

}
