package com.epf.rentmanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceTest {
	
	@InjectMocks
	private VehicleService vehicleService;
	
	@Mock
	private VehicleDao vehicleDao;

	@BeforeClass
	public static void before() {
		System.out.print("***************************** Test of Vehicle service *********************** \n");

	}
	
	@Test
	public void getVehicleByIdTest() {
		try {
			Mockito.when(vehicleDao.findById(1)).thenReturn(
					Optional.of(new Vehicle(1, "Peugeot", "206", Short.parseShort("4") )));
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			fail("Erreur DaoException" + e.getMessage());
		}

		Vehicle vehicle;
		try {
			vehicle = vehicleService.findById(1);
			assertEquals("Peugeot",vehicle.getConstructeur());
			assertEquals("206", vehicle.getModele());
			assertEquals(Short.parseShort("4") , vehicle.getNb_places());
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			fail("Erreur ServiceException" + e.getMessage());
		}

		
	}
	
	
}
