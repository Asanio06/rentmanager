package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.h2.command.dml.Update;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.dao.ClientDao;

public class ClientService {

	private ClientDao clientDao;
	public static ClientService instance;

	private ClientService() {
		this.clientDao = ClientDao.getInstance();
	}

	public static ClientService getInstance() {
		if (instance == null) {
			instance = new ClientService();
		}

		return instance;
	}

	public long create(Client client) throws ServiceException {
		// TODO: créer un client ; Verification du nom et du prenom ; Le nom doit être
		// en majuscule

		if (client.getNom().isEmpty()) {
			throw new ServiceException("Veuillez Saisir un nom");
		}

		if (client.getPrenom().isEmpty()) {
			throw new ServiceException("Veuillez Saisir un prenom");
		}

		client.setNom(client.getNom().toUpperCase());

		try {

			return clientDao.create(client);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public long deleteClient(Client client) throws ServiceException {
		
		try {
			return clientDao.delete(client);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}

	}

	public boolean updateClient(Client client) throws ServiceException {

		try {

			clientDao.update(client);
			return true;

		} catch (DaoException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(e.getMessage());
		}

	}

	public Client findById(long id) throws ServiceException {
		// TODO: récupérer un client par son id
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

	public List<Client> findAll() throws ServiceException {
		// TODO: récupérer tous les clients

		try {

			return clientDao.findAll();

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());

		}
	}
	
	public int nbOfClient() throws ServiceException {
		try {
			return clientDao.nbOfClient();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			throw new ServiceException("Erreur");
		}
	}

}
