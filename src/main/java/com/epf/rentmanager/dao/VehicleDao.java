package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;

@Repository
public class VehicleDao {

	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur,modele, nb_places) VALUES(?, ?,?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, modele, constructeur, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	private static final String UPDATE_VEHICLE_QUERY = "UPDATE Vehicle SET constructeur = ?, modele = ?, nb_places = ? WHERE id = ?;";
	private static final String COUNT_VEHICLE_QUERY = "SELECT COUNT(id) AS count FROM Vehicle;";
	private static final String FIND_DISTINCT_VEHICLES_RESA_BY_CLIENT_QUERY = "SELECT  DISTINCT ON (Vehicle.id) Vehicle.id,Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places "
			+ "FROM Reservation " + "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id " + "WHERE Reservation.client_id = ?";

	/**
	 * Permet de créer un véhicule dans la base de données
	 * 
	 * @param vehicle
	 * @return L'identifiant du véhicule qui vient d'être créer dans la base de
	 *         données
	 * @throws DaoException
	 */
	public long create(Vehicle vehicle) throws DaoException {

		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS);
			long id = 0;
			ps.setString(1, vehicle.getConstructeur());
			ps.setString(2, vehicle.getModele());
			ps.setShort(3, vehicle.getNb_places());

			ps.executeUpdate();
			ResultSet resultSet = ps.getGeneratedKeys();
			if (resultSet.next()) {
				id = resultSet.getLong(1);
			}

			ps.close();
			connection.close();
			return id;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());

		}

	}

	/**
	 * Permet de mettre à jour un véhicules dans la base de données
	 * 
	 * @param vehicle le véhicule qu'on souhaite mettre à jour dans la base de
	 *                données
	 * @return Le nombre de ligne impacté dans la base de données
	 * @throws DaoException
	 */
	public int update(Vehicle vehicle) throws DaoException {

		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(UPDATE_VEHICLE_QUERY);
			ps.setString(1, vehicle.getConstructeur());
			ps.setString(2, vehicle.getModele());
			ps.setShort(3, vehicle.getNb_places());
			ps.setLong(4, vehicle.getId());

			int nb_ligne_update = ps.executeUpdate();
			ps.close();
			connection.close();

			return nb_ligne_update;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());

		}

	}

	/**
	 * Permet de supprimer un véhicule dans la base de données
	 * 
	 * @param vehicle Le véhicule qu'on souhaite retirer de la base de données
	 * @return Le nombre de ligne impacté par la suppression
	 * @throws DaoException
	 */
	public int delete(Vehicle vehicle) throws DaoException {

		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_VEHICLE_QUERY);
			ps.setLong(1, vehicle.getId());

			int nb_ligne_delete = ps.executeUpdate();
			ps.close();
			connection.close();
			return nb_ligne_delete;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());

		}

	}

	/**
	 * Permet de récupérer un Objet Optional<Vehicle> en fonction de l'id du
	 * véhicule
	 * 
	 * @param id L'identifiant du véhicule
	 * @return Un Optional <Vehicle>
	 * @throws DaoException
	 */
	public Optional<Vehicle> findById(long id) throws DaoException {

		Optional<Vehicle> opt_vehicle;

		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_VEHICLE_QUERY);
			ps.setLong(1, id);

			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {

				Vehicle vehicle = new Vehicle();
				vehicle.setId(id);
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));

				opt_vehicle = Optional.of(vehicle);

			} else {
				opt_vehicle = Optional.empty();
			}

			resultSet.close();
			ps.close();
			connection.close();

			return opt_vehicle;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/**
	 * Permet de récupérer la liste des véhicules présents dans la base de données
	 * 
	 * @return La liste des véhicules présents dans la base de données
	 * @throws DaoException
	 */
	public List<Vehicle> findAll() throws DaoException {

		List<Vehicle> list_vehicle = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_VEHICLES_QUERY);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("id"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));

				list_vehicle.add(vehicle);

			}
			resultSet.close();
			ps.close();
			connection.close();
			return list_vehicle;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/**
	 * Permet de récupérer la liste des véhicules distinct réservé par un client
	 * particulier
	 * 
	 * @param client Le client pour qui on souhaite obtenir les véhicules réservés
	 * @return La liste des véhicules distincts réservés par le client
	 * @throws DaoException
	 */
	public List<Vehicle> findDistinctVehiclesReservedByClient(Client client) throws DaoException {
		List<Vehicle> list_vehicle = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_DISTINCT_VEHICLES_RESA_BY_CLIENT_QUERY);
			ps.setLong(1, client.getId());
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("id"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));

				list_vehicle.add(vehicle);

			}
			resultSet.close();
			ps.close();
			connection.close();

			return list_vehicle;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Permet de récupérer le nombre de véhicule présent dans la base de données
	 * 
	 * @return Le nombre de véhicule présent dans la base de données
	 * @throws DaoException
	 */
	public int nbOfVehicle() throws DaoException {
		try {
			int nbOfVehicle = 0;

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(COUNT_VEHICLE_QUERY);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				nbOfVehicle = resultSet.getInt("count");
			}

			resultSet.close();
			ps.close();
			connection.close();

			return nbOfVehicle;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
	}

}
