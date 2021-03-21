package com.epf.rentmanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.utils.IOUtils;
import org.mockito.Mock;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {

	@InjectMocks
	private ClientService clientService;

	@Mock
	private ClientDao clientDao;

	@BeforeClass
	public static void before() {
		System.out.print("*****************************Test of Client service*********************** \n");

	}

	@Test
	public void createClientShouldThrowExceptionWhenClientIsMinorTest() {
		Client client = new Client();
		client.setNom("DIOMANDE");
		client.setPrenom("Lansana");
		client.setEmail("asane@gmail.com");
		client.setNaissance(IOUtils.subtractYear(new Date(Calendar.getInstance().getTime().getTime()), 17));
		assertThrows(ServiceException.class, () -> clientService.create(client));
	}

	@Test
	public void createClientShouldNotThrowExceptionWhenClientIsMajorTest() {
		Client client = new Client();
		client.setNom("DIOMANDE");
		client.setPrenom("Lansana");
		client.setEmail("asane@gmail.com");
		client.setNaissance(IOUtils.subtractYear(new Date(Calendar.getInstance().getTime().getTime()), 19));
		// When
		try {
			Mockito.when(this.clientDao.create(client)).thenReturn((long) 1);

		} catch (DaoException e) {
			// TODO Auto-generated catch block
			fail("Erreur DaoException" + e.getMessage());
		}
		try {
			clientService.create(client);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			fail("Erreur ServiceException" + e.getMessage());
		}
	}

	@Test
	public void getClientByIdTest() {
		try {
			Mockito.when(clientDao.findById(1)).thenReturn(
					Optional.of(new Client(1, "DIOMANDE", "Lansana", "asane@gmail.com", Date.valueOf("2015-05-05"))));
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			fail("Erreur DaoException" + e.getMessage());
		}

		Client client;
		try {
			client = clientService.findById(1);
			assertEquals("DIOMANDE", client.getNom());
			assertEquals("Lansana", client.getPrenom());
			assertEquals("asane@gmail.com", client.getEmail());
			assertEquals(Date.valueOf("2015-05-05"), client.getNaissance());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			fail("Erreur ServiceException" + e.getMessage());
		}

		
	}

}
