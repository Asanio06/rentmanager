package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;

public class ClientDao {

	private static ClientDao instance = null;

	private ClientDao() {
	}

	public static ClientDao getInstance() {
		if (instance == null) {
			instance = new ClientDao();
		}
		return instance;
	}

	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String UPDATE_CLIENT_QUERY = "UPDATE Client Set nom=?,prenom=?,email=?,naissance=? WHERE id=?";
	private static final String COUNT_CLIENT_QUERY = "SELECT COUNT(id) AS count FROM Client;";
	public long create(Client client) throws DaoException {
		long id = 0;
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, client.getNaissance());

			ps.executeUpdate();
			ResultSet resultSet = ps.getGeneratedKeys();
			if (resultSet.next()) {
				id = resultSet.getLong(1);
			}
			ps.close();
			connection.close();

		} catch (SQLException e) {
			throw new DaoException();

		}

		return id;

	}

	public long delete(Client client) throws DaoException {
		// long id = 2; // A modifier
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY);
			ps.setLong(1, client.getId());

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (SQLException e) {
			throw new DaoException();
		}

		return 1;
	}

	public Optional<Client> findById(long id) throws DaoException {
		Optional<Client> opt_client;

		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_QUERY);
			ps.setLong(1, id);

			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				Client client = new Client();
				client.setId(id);
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));

				opt_client = Optional.of(client);

			} else {
				opt_client = Optional.empty();
			}
			resultSet.close();
			ps.close();
			connection.close();

			return opt_client;
		} catch (SQLException e) {
			// System.out.print(e.getMessage());
			throw new DaoException();

		}

	}
	
	public boolean update(Client client) throws DaoException{
		
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(UPDATE_CLIENT_QUERY);
			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, client.getNaissance());
			ps.setLong(5, client.getId());

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (SQLException e) {
			throw new DaoException();

		}
		return true;
	}

	public List<Client> findAll() throws DaoException {
		List<Client> list_client = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_CLIENTS_QUERY);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				Client client = new Client();
				client.setId(resultSet.getInt("id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));
				list_client.add(client);

			}
			resultSet.close();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			throw new DaoException();
		}

		return list_client;

	}
	
	public long nbOfClient() throws DaoException {
		try {
			long nbOfClient = 0;

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(COUNT_CLIENT_QUERY);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				nbOfClient = resultSet.getLong("count");
			}
			
			resultSet.close();
			ps.close();
			connection.close();

			return nbOfClient;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
			throw new DaoException();
		}
	}

}
