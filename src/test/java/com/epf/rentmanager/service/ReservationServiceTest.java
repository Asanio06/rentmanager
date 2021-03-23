package com.epf.rentmanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.h2.command.ddl.Analyze;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.MockitoJUnitRunner;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {
	@InjectMocks
	private ReservationService reservationService;

	@Mock
	private ReservationDao reservationDao;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void before() {
		System.out.print("***************************** Test of Reservation service *********************** \n");

	}

	@Test
	public void createReservationShouldThrowExceptionIfVehicleIsAlreadyReservedTest() throws ServiceException {
		Client client = new Client();
		client.setNom("DIOMANDE");
		client.setPrenom("Lansana");
		client.setEmail("asane@gmail.com");

		Vehicle vehicle = new Vehicle(1, "Peugeot", "206", Short.parseShort("4"));

		try {
			mockFIndResaOfVehicleInPeriodOfOtherReservationReturnSomeThing(client, vehicle);
		} catch (DaoException e) {
			fail(e.getMessage());

		}
		Reservation reservationATester = new Reservation();
		reservationATester.setClient(client);
		reservationATester.setVehicle(vehicle);
		reservationATester.setDebut(Date.valueOf("2021-03-06"));
		reservationATester.setFin(Date.valueOf("2021-03-06"));
		thrown.expect(ServiceException.class);
		thrown.expectMessage("Le véhicule est déjà réservé sur la même période");
		reservationService.create(reservationATester);

	}

	@Test
	public void createReservationShouldThrowExceptionIfVehicleIsReserved7DaysInAROwByTheSameClientTest()
			throws ServiceException {
		Client client = new Client();
		client.setNom("DIOMANDE");
		client.setPrenom("Lansana");
		client.setEmail("asane@gmail.com");

		Vehicle vehicle = new Vehicle(1, "Peugeot", "206", Short.parseShort("4"));
		try {
			mockFIndResaOfVehicleInPeriodOfOtherReservationReturnNothing(client, vehicle);
			mockFindResaOf7DaysByVehicleAndClient(client, vehicle);
		} catch (DaoException e) {
			fail(e.getMessage());
		}
		Reservation reservationATester = new Reservation();
		reservationATester.setClient(client);
		reservationATester.setVehicle(vehicle);
		reservationATester.setDebut(Date.valueOf("2021-03-06"));
		reservationATester.setFin(Date.valueOf("2021-03-08"));
		thrown.expect(ServiceException.class);
		thrown.expectMessage("Le véhicule ne dois pas être réservé plus de "
				+ reservationService.nbDeJourMaxSimultanePourResaVehiculeParUnUtilisateur
				+ " jours de suite par le même client");
		reservationService.create(reservationATester);

	}

	@Test
	public void createReservationShouldThrowExceptionIfVehicleIsReserved30DaysInARowTest() throws ServiceException {
		Client client = new Client();
		client.setNom("DIOMANDE");
		client.setPrenom("Lansana");
		client.setEmail("asane@gmail.com");

		Vehicle vehicle = new Vehicle(1, "Peugeot", "206", Short.parseShort("4"));

		try {
			mockfindResa30DaysByVehicle(client, vehicle);
			mockFindResaOf7DaysByVehicleAndClient(client, vehicle);
			mockFIndResaOfVehicleInPeriodOfOtherReservationReturnNothing(client, vehicle);
		} catch (DaoException e) {
			fail(e.getMessage());
		}

		Reservation reservationATester = new Reservation();
		reservationATester.setClient(client);
		reservationATester.setVehicle(vehicle);
		reservationATester.setDebut(Date.valueOf("2021-03-07"));
		reservationATester.setFin(Date.valueOf("2021-03-09"));
		thrown.expect(ServiceException.class);
		thrown.expectMessage("Le véhicule ne doit pas être réservé plus de "
				+ reservationService.nbDeJourMaxSimultanePourResaVehiculeSansPause + " jours de suite");
		reservationService.create(reservationATester);
	}

	@Test
	public void createReservationShouldNotThrowsExceptionTest() {
		Client client = new Client();
		client.setNom("DIOMANDE");
		client.setPrenom("Lansana");
		client.setEmail("asane@gmail.com");

		Vehicle vehicle = new Vehicle(1, "Peugeot", "206", Short.parseShort("4"));
		

		try {
			Mockito.when(reservationDao.create(Mockito.any(Reservation.class))).thenReturn(Long.parseLong("1"));
			mockfindResa30DaysByVehicle(client, vehicle);
			mockFindResaOf7DaysByVehicleAndClient(client, vehicle);
			mockFIndResaOfVehicleInPeriodOfOtherReservationReturnNothing(client, vehicle);
		} catch (DaoException e) {
			fail(e.getMessage());
		}
		Reservation reservationATester = new Reservation();
		reservationATester.setClient(client);
		reservationATester.setVehicle(vehicle);
		reservationATester.setDebut(Date.valueOf("2021-03-08"));
		reservationATester.setFin(Date.valueOf("2021-03-08"));
		try {
			assertEquals(1, reservationService.create(reservationATester)); ;
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
		
		
	}

	private void mockfindResa30DaysByVehicle(Client client, Vehicle vehicle) throws DaoException {
		List<Reservation> list_resa_30daysBefore = new ArrayList<Reservation>();
		List<Reservation> list_resa_30daysAfter = new ArrayList<Reservation>();

		Reservation reservation30DayBeforeForSameVehicle = new Reservation();
		reservation30DayBeforeForSameVehicle.setClient(client);
		reservation30DayBeforeForSameVehicle.setVehicle(vehicle);
		reservation30DayBeforeForSameVehicle.setDebut(Date.valueOf("2021-02-19"));
		reservation30DayBeforeForSameVehicle.setFin(Date.valueOf("2021-03-06"));

		Reservation reservation30DayAfterForSameVehicle = new Reservation();
		reservation30DayAfterForSameVehicle.setClient(client);
		reservation30DayAfterForSameVehicle.setVehicle(vehicle);
		reservation30DayAfterForSameVehicle.setDebut(Date.valueOf("2021-03-10"));
		reservation30DayAfterForSameVehicle.setFin(Date.valueOf("2021-03-20"));

		list_resa_30daysBefore.add(reservation30DayBeforeForSameVehicle);
		list_resa_30daysAfter.add(reservation30DayAfterForSameVehicle);

		Mockito.when(reservationDao.findResaOf30LastDayByVehicle(Mockito.any(Reservation.class)))
				.thenReturn(list_resa_30daysBefore);
		Mockito.when(reservationDao.findResaOf30DayAfterByVehicle(Mockito.any(Reservation.class)))
				.thenReturn(list_resa_30daysAfter);
	}

	private void mockFindResaOf7DaysByVehicleAndClient(Client client, Vehicle vehicle) throws DaoException {
		List<Reservation> list_resa_7daysBefore = new ArrayList<Reservation>();
		List<Reservation> list_resa_7daysAfter = new ArrayList<Reservation>();

		Reservation reservation7DayBeforeForSameVehicleAndClient = new Reservation();

		reservation7DayBeforeForSameVehicleAndClient.setClient(client);
		reservation7DayBeforeForSameVehicleAndClient.setVehicle(vehicle);
		reservation7DayBeforeForSameVehicleAndClient.setDebut(Date.valueOf("2021-03-01"));
		reservation7DayBeforeForSameVehicleAndClient.setFin(Date.valueOf("2021-03-05"));

		Reservation reservation7DayAfterForSameVehicleAndClient = new Reservation();
		reservation7DayAfterForSameVehicleAndClient.setClient(client);
		reservation7DayAfterForSameVehicleAndClient.setVehicle(vehicle);
		reservation7DayAfterForSameVehicleAndClient.setDebut(Date.valueOf("2021-03-10"));
		reservation7DayAfterForSameVehicleAndClient.setFin(Date.valueOf("2021-03-11"));
		list_resa_7daysBefore.add(reservation7DayBeforeForSameVehicleAndClient);
		list_resa_7daysAfter.add(reservation7DayAfterForSameVehicleAndClient);
		Mockito.when(reservationDao.findResaOf7LastDayByVehicleAndClient(Mockito.any(Reservation.class)))
				.thenReturn(list_resa_7daysBefore);
		Mockito.when(reservationDao.findResaOf7DayAfterByVehicleAndClient(Mockito.any(Reservation.class)))
				.thenReturn(list_resa_7daysAfter);
	}

	private void mockFIndResaOfVehicleInPeriodOfOtherReservationReturnSomeThing(Client client, Vehicle vehicle)
			throws DaoException {
		List<Reservation> list_resa_in_same_time = new ArrayList<Reservation>();
		Reservation reservationInSameTime = new Reservation();
		reservationInSameTime.setClient(client);
		reservationInSameTime.setVehicle(vehicle);
		reservationInSameTime.setDebut(Date.valueOf("2021-03-02"));
		reservationInSameTime.setFin(Date.valueOf("2021-03-20"));
		list_resa_in_same_time.add(reservationInSameTime);

		Mockito.when(reservationDao.findResaOfVehicleInPeriodOfOtherReservation(Mockito.any(Reservation.class)))
				.thenReturn(list_resa_in_same_time);
	}

	private void mockFIndResaOfVehicleInPeriodOfOtherReservationReturnNothing(Client client, Vehicle vehicle)
			throws DaoException {
		List<Reservation> list_resa_in_same_time = new ArrayList<Reservation>();

		Mockito.when(reservationDao.findResaOfVehicleInPeriodOfOtherReservation(Mockito.any(Reservation.class)))
				.thenReturn(list_resa_in_same_time);
	}

}
