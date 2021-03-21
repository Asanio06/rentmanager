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

import org.springframework.stereotype.Repository;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;

@Repository
public class ClientDao {

	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String UPDATE_CLIENT_QUERY = "UPDATE Client Set nom=?,prenom=?,email=?,naissance=? WHERE id=?";
	private static final String COUNT_CLIENT_QUERY = "SELECT COUNT(id) AS count FROM Client;";
	private static final String FIND_DISTINCT_CLIENTS__BY_VEHICLE_USED_QUERY = "SELECT  DISTINCT ON (Client.id) Client.id,Client.nom, Client.prenom, Client.email, Client.naissance "
			+ "FROM Reservation " + "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id " + "WHERE Reservation.vehicle_id = ?";
	private static final String FIND_CLIENT_BY_EMAIL_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client WHERE Client.email = ?";

	/**
	 * Permet d'utiliser une requête SQL afin d'insérer un client dans la base de données
	 * @param client l'objet client représentant le client qu'on souhaite ajouter à la base de données
	 * @return l'identifiant du client qui vient d'être insérer dans la base de données
	 * @throws DaoException 
	 */
	public long create(Client client) throws DaoException {
		long id = 0;
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, client.getNaissance());

			id = ps.executeUpdate();
			ResultSet resultSet = ps.getGeneratedKeys();
			if (resultSet.next()) {
				id = resultSet.getLong(1);
			}
			ps.close();
			connection.close();

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());

		}

		return id;

	}

	/**
	 * Permet d'utiliser une requête SQL afin de supprimer un client dans la base de données
	 * @param client objet de la classe Client représentant le client qu'on souhaite ajouter à la base de données
	 * @return nombre de ligne modifié dans la base de données
	 * @throws DaoException 
	 */
	public long delete(Client client) throws DaoException {
		long nb_ligne_modifie = 2; 
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY);
			ps.setLong(1, client.getId());

			nb_ligne_modifie = ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

		return nb_ligne_modifie;
	}
	
	/**
	 * Permet de récupérer un Objet Optional<Client> en fonction de l'id du client
	 * @param id l'identifiant du client qu'on souhaite récupérer dans la base de données
	 * @return Un Optional<Client> contenant le client s'il existe dans la base de données et null sinon
	 * @throws DaoException
	 */
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
			throw new DaoException(e.getMessage());

		}

	}
	
	/**
	 * Permet d'utiliser une requête SQL afin de mettre à jour un client dans la base de données
	 * @param client l'objet client représentant le client qu'on souhaite ajouter à la base de données
	 * @return true si la mise à jour à eu lieu; false sinon
	 * @throws DaoException 
	 */
	public boolean update(Client client) throws DaoException {

		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(UPDATE_CLIENT_QUERY);
			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, client.getNaissance());
			ps.setLong(5, client.getId());

			int nb_ligne_update = ps.executeUpdate();
			ps.close();
			connection.close();
			if (nb_ligne_update >= 1) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());

		}

	}

	/**
	 * Permet d'obtenir la liste des clients présents dans la base de donnés
	 * @return La liste des clients présents dans la base de données
	 * @throws DaoException
	 */
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
			throw new DaoException(e.getMessage());
		}

		return list_client;

	}
	
	/**
	 * Permet de récupérer la liste des clients distinct ayant réserver un véhicule particulier
	 * @param vehicle le vehicule sur lequel on souhaite appliquer la recherche
	 * @return la liste des clients distinct ayant réserver un véhicule particulier
	 * @throws DaoException
	 */
	public List<Client> findDistinctClientByVehicleUsed(Vehicle vehicle) throws DaoException {
		List<Client> list_client = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_DISTINCT_CLIENTS__BY_VEHICLE_USED_QUERY);
			ps.setLong(1, vehicle.getId());
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				Client client = new Client();
				client.setId(resultSet.getLong("id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));
				list_client.add(client);

			}
			resultSet.close();
			ps.close();
			connection.close();

			return list_client;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * @return le nombre de client présent dans la base de données
	 * @throws DaoException
	 */
	public int nbOfClient() throws DaoException {
		try {
			int nbOfClient = 0;

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(COUNT_CLIENT_QUERY);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				nbOfClient = resultSet.getInt("count");
			}

			resultSet.close();
			ps.close();
			connection.close();

			return nbOfClient;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Permet de récupérer un Optional<Client> de la base de données grâce à son email
	 * @param email L'email du client qu'on veut retrouver
	 * @return Un Optional<Client> contenant le client s'il existe dans la base de données et null sinon
	 * @throws DaoException
	 */
	public Optional<Client> findByEmail(String email) throws DaoException {
		Optional<Client> opt_client;

		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_BY_EMAIL_QUERY);
			ps.setString(1, email);

			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				Client client = new Client();
				client.setId(resultSet.getInt("id"));
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
			throw new DaoException(e.getMessage());

		}

	}

}
