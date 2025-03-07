package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.utils.IOUtils;

@Repository
public class ReservationDao {

	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String UPDATE_RESERVATION_QUERY = "UPDATE Reservation SET debut=?, fin=? WHERE id=?;";

	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance, "
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places " + "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id " + "WHERE Reservation.client_id = ?;";

	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance,"
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places " + "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id " + "WHERE Reservation.vehicle_id=?;";

	private static final String FIND_RESERVATIONS_QUERY = "SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance,"
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places " + "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id ";

	private static final String FIND_RESERVATION_QUERY = "SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance,"
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places " + "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id " + "WHERE Reservation.id = ?";

	private static final String COUNT_RESERVATION_QUERY = "SELECT COUNT(id) AS count FROM Reservation;";

	private static final String FIND_RESERVATION_30_DAYS_BEFORE_BY_VEHICLE_QUERY = "SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance, "
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places " + "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id " + "WHERE Reservation.vehicle_id = ? "
			+ "AND Reservation.fin BETWEEN ? AND ? " + "AND Reservation.id != ? " + "ORDER BY Reservation.fin DESC;";

	private static final String FIND_RESERVATION_30_DAYS_AFTER_BY_VEHICLE_QUERY = "SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance, "
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places " + "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id "
			+ "WHERE Reservation.vehicle_id = ? AND Reservation.debut BETWEEN ? AND ? " + "AND Reservation.id != ? "
			+ "ORDER BY Reservation.debut ASC;";

	private static final String FIND_RESERVATION_7_DAYS_BEFORE_BY_VEHICLE_AND_CLIENT_QUERY = "SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance, "
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places " + "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id "
			+ "WHERE Reservation.vehicle_id = ?  AND Reservation.client_id = ? "
			+ "AND Reservation.fin BETWEEN ? AND ? " + "AND Reservation.id != ? " + "ORDER BY Reservation.fin DESC;";

	private static final String FIND_RESERVATION_7_DAYS_AFTER_BY_VEHICLE_AND_CLIENT_QUERY = "SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance, "
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places " + "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id "
			+ "WHERE Reservation.vehicle_id = ? AND Reservation.client_id = ? AND Reservation.debut BETWEEN ? AND ? "
			+ "AND Reservation.id != ? " + "ORDER BY Reservation.debut ASC;";

	private static final String FIND_RESERVATION_OF_VEHICLE_IN_PERIOD_OF_OTHER_RESERVATION = "SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance, "
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places " + "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id " + "WHERE Reservation.vehicle_id = ? "
			+ "AND ( ? BETWEEN Reservation.debut AND Reservation.fin OR ? BETWEEN Reservation.debut AND Reservation.fin ) "
			+ "AND Reservation.id != ? " + "ORDER BY Reservation.debut ASC;";

	/**
	 * Permet de créer une réservation dans la base de données
	 * 
	 * @param reservation la réservation qu'on souhaite insérer dans la base de
	 *                    données
	 * @return l'identifiant de la réservation créé dans la base de données
	 * @throws DaoException
	 */
	public long create(Reservation reservation) throws DaoException {

		long id = 0;

		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(CREATE_RESERVATION_QUERY,
					Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, reservation.getClient().getId());
			ps.setLong(2, reservation.getVehicle().getId());
			ps.setDate(3, reservation.getDebut());
			ps.setDate(4, reservation.getFin());

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
	 * Permet de supprimer une réservation de la base de données
	 * 
	 * @param reservation la réservation qu'on souhaite supprimer de la base de
	 *                    données
	 * @return le nombre de ligne affecté par la requête SQL
	 * @throws ServiceException
	 */
	public int delete(Reservation reservation) throws DaoException {

		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION_QUERY);
			ps.setLong(1, reservation.getId());

			int nb_ligne_delete = ps.executeUpdate();
			ps.close();
			connection.close();
			return nb_ligne_delete;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());

		}

	}

	/**
	 * Permet de mettre à jour une réservation dans la base de données
	 * 
	 * @param reservation La réservation qu'on souhaite mettre à jour
	 * @return le nombre de ligne affecté par la requête sql
	 * @throws DaoException
	 */
	public int update(Reservation reservation) throws DaoException {

		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(UPDATE_RESERVATION_QUERY);
			ps.setDate(1, reservation.getDebut());
			ps.setDate(2, reservation.getFin());
			ps.setLong(3, reservation.getId());

			int nb_ligne_update = ps.executeUpdate();
			ps.close();
			connection.close();

			return nb_ligne_update;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());

		}

	}

	/**
	 * Permet de récupérer la liste des réservation d'un client
	 * 
	 * @param clientId L'identifiant du client
	 * @return La liste des réservation du client
	 * @throws DaoException
	 */
	public List<Reservation> findResaByClientId(long clientId) throws DaoException {

		List<Reservation> list_reservation = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);
			ps.setLong(1, clientId);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(resultSet.getLong("id"));

				Client client = new Client();
				client.setId(resultSet.getInt("client_id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));

				reservation.setClient(client);

				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("vehicle_id"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));
				reservation.setVehicle(vehicle);

				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));

				list_reservation.add(reservation);

			}
			resultSet.close();
			ps.close();
			connection.close();
			return list_reservation;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/**
	 * Permet de récupérer la liste des réservations faites pour un véhicule
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return La liste des réservations faites pour le véhicule
	 * @throws DaoException
	 */
	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {

		List<Reservation> list_reservation = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY);
			ps.setLong(1, vehicleId);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(resultSet.getLong("id"));

				Client client = new Client();
				client.setId(resultSet.getInt("client_id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));

				reservation.setClient(client);

				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("vehicle_id"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));
				reservation.setVehicle(vehicle);

				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));

				list_reservation.add(reservation);

			}
			resultSet.close();
			ps.close();
			connection.close();
			return list_reservation;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Cette fonction permet de récupérer la liste des réservations qui ont eu lieu
	 * sur un véhicule les 30jours avant une réservation donner en entré
	 * 
	 * @param reservationATester La réservation sur laquelle on se base
	 * @return La liste des réservations qui ont eu lieu sur le véhicule les 30jours
	 *         avant la réservation en entré
	 * @throws DaoException
	 */
	public List<Reservation> findResaOf30LastDayByVehicle(Reservation reservationATester) throws DaoException {

		List<Reservation> list_reservation = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATION_30_DAYS_BEFORE_BY_VEHICLE_QUERY);
			ps.setLong(1, reservationATester.getVehicle().getId());
			ps.setDate(2, IOUtils.subtractDays(reservationATester.getFin(), 30));
			ps.setDate(3, reservationATester.getFin());
			ps.setLong(4, reservationATester.getId());
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(resultSet.getLong("id"));

				Client client = new Client();
				client.setId(resultSet.getInt("client_id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));

				reservation.setClient(client);

				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("vehicle_id"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));
				reservation.setVehicle(vehicle);

				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));

				list_reservation.add(reservation);

			}

			resultSet.close();
			ps.close();
			connection.close();
			return list_reservation;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/**
	 * Cette fonction permet de récupérer la liste des réservations qui ont eu lieu
	 * sur un véhicule les 30jours après une réservation donner en entré
	 * 
	 * @param reservationATester La réservation sur laquelle on se base
	 * @return La liste des réservations qui ont eu lieu sur le véhicule les 30jours
	 *         avant la réservation en entré
	 * @throws DaoException
	 */
	public List<Reservation> findResaOf30DayAfterByVehicle(Reservation reservationATester) throws DaoException {

		List<Reservation> list_reservation = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATION_30_DAYS_AFTER_BY_VEHICLE_QUERY);
			ps.setLong(1, reservationATester.getVehicle().getId());
			ps.setDate(2, reservationATester.getDebut());
			ps.setDate(3, IOUtils.addDays(reservationATester.getDebut(), 30));
			ps.setLong(4, reservationATester.getId());
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(resultSet.getLong("id"));

				Client client = new Client();
				client.setId(resultSet.getInt("client_id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));

				reservation.setClient(client);

				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("vehicle_id"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));
				reservation.setVehicle(vehicle);

				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));

				list_reservation.add(reservation);

			}

			resultSet.close();
			ps.close();
			connection.close();
			return list_reservation;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/**
	 * Cette fonction permet de récupérer la liste des réservations d'un véhicule
	 * dont la période interfère avec une réservation de ce même véhicule
	 * 
	 * @param reservationATester La réservation sur laquelle on se base
	 * @return La liste des réservations d'un véhicule dont la période interfère
	 *         avec une réservation de ce même véhicule
	 * @throws DaoException
	 */
	public List<Reservation> findResaOfVehicleInPeriodOfOtherReservation(Reservation reservationATester)
			throws DaoException {

		List<Reservation> list_reservation = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection
					.prepareStatement(FIND_RESERVATION_OF_VEHICLE_IN_PERIOD_OF_OTHER_RESERVATION);
			ps.setLong(1, reservationATester.getVehicle().getId());
			ps.setDate(2, reservationATester.getDebut());
			ps.setDate(3, reservationATester.getFin());
			ps.setLong(4, reservationATester.getId());
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(resultSet.getLong("id"));

				Client client = new Client();
				client.setId(resultSet.getInt("client_id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));

				reservation.setClient(client);

				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("vehicle_id"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));
				reservation.setVehicle(vehicle);

				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));

				list_reservation.add(reservation);

			}

			resultSet.close();
			ps.close();
			connection.close();
			return list_reservation;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/**
	 * Cette fonction permet de récupérer la liste des réservations qui ont eu lieu
	 * sur un véhicule et pour un client les 7jours avant une réservation donner en
	 * entré
	 * 
	 * @param reservationATester La réservation sur laquelle on se base
	 * @return La liste des réservations qui ont eu lieu sur un véhicule et pour un
	 *         client les 7jours avant la réservation en entré
	 * @throws DaoException
	 */
	public List<Reservation> findResaOf7LastDayByVehicleAndClient(Reservation reservationATester) throws DaoException {

		List<Reservation> list_reservation = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection
					.prepareStatement(FIND_RESERVATION_7_DAYS_BEFORE_BY_VEHICLE_AND_CLIENT_QUERY);
			ps.setLong(1, reservationATester.getVehicle().getId());
			ps.setLong(2, reservationATester.getClient().getId());
			ps.setDate(3, IOUtils.subtractDays(reservationATester.getFin(), 7));
			ps.setDate(4, reservationATester.getFin());
			ps.setLong(5, reservationATester.getId());
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(resultSet.getLong("id"));

				Client client = new Client();
				client.setId(resultSet.getInt("client_id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));

				reservation.setClient(client);

				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("vehicle_id"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));
				reservation.setVehicle(vehicle);

				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));

				list_reservation.add(reservation);

			}

			resultSet.close();
			ps.close();
			connection.close();
			return list_reservation;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/**
	 * Cette fonction permet de récupérer la liste des réservations qui ont eu lieu
	 * sur un véhicule et pour un client les 7jours après une réservation donner en
	 * entré
	 * 
	 * @param reservationATester La réservation sur laquelle on se base
	 * @return La liste des réservations qui ont eu lieu sur un véhicule et pour un
	 *         client les 7jours après la réservation en entré
	 * @throws DaoException
	 */
	public List<Reservation> findResaOf7DayAfterByVehicleAndClient(Reservation reservationATester) throws DaoException {

		List<Reservation> list_reservation = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection
					.prepareStatement(FIND_RESERVATION_7_DAYS_AFTER_BY_VEHICLE_AND_CLIENT_QUERY);
			ps.setLong(1, reservationATester.getVehicle().getId());
			ps.setLong(2, reservationATester.getClient().getId());
			ps.setDate(3, reservationATester.getDebut());
			ps.setDate(4, IOUtils.addDays(reservationATester.getDebut(), 7));
			ps.setLong(5, reservationATester.getId());
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(resultSet.getLong("id"));

				Client client = new Client();
				client.setId(resultSet.getInt("client_id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));

				reservation.setClient(client);

				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("vehicle_id"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));
				reservation.setVehicle(vehicle);

				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));

				list_reservation.add(reservation);

			}

			resultSet.close();
			ps.close();
			connection.close();
			return list_reservation;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/**
	 * Permet de récupérer la liste de toutes les réservations de la base de données
	 * 
	 * @return La liste de toutes les réservations présentes dans la base de données
	 * @throws DaoException
	 */
	public List<Reservation> findAll() throws DaoException {

		List<Reservation> list_reservation = new ArrayList<>();
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_QUERY);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(resultSet.getLong("id"));

				Client client = new Client();
				client.setId(resultSet.getInt("client_id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));

				reservation.setClient(client);

				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("vehicle_id"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));
				reservation.setVehicle(vehicle);

				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));

				list_reservation.add(reservation);

			}
			resultSet.close();
			ps.close();
			connection.close();
			return list_reservation;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/**
	 * Permet de récupérer un Objet Optional<Reservation> en fonction de l'id de la
	 * réservation
	 * 
	 * @param Id L'identifiant de la réservation
	 * @return Un Optional <Reservation> contenant la réservation s'elle existe dans
	 *         la base de données et null sinon
	 * @throws DaoException
	 */
	public Optional<Reservation> findById(long Id) throws DaoException {

		Optional<Reservation> opt_resa;
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATION_QUERY);
			ps.setLong(1, Id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(resultSet.getLong("id"));

				Client client = new Client();
				client.setId(resultSet.getInt("client_id"));
				client.setNom(resultSet.getString("nom"));
				client.setPrenom(resultSet.getString("prenom"));
				client.setEmail(resultSet.getString("email"));
				client.setNaissance(resultSet.getDate("naissance"));

				reservation.setClient(client);

				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong("vehicle_id"));
				vehicle.setModele(resultSet.getString("modele"));
				vehicle.setConstructeur(resultSet.getString("constructeur"));
				vehicle.setNb_places(resultSet.getShort("nb_places"));
				reservation.setVehicle(vehicle);

				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));

				opt_resa = Optional.of(reservation);

			} else {
				opt_resa = Optional.empty();
			}
			resultSet.close();
			ps.close();
			connection.close();
			return opt_resa;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/**
	 * Permet de récupérer le nombre de réservation présent dans la base de données
	 * 
	 * @return Le nombre de réservation présent dans la base de données
	 * @throws DaoException
	 */
	public int nbOfResa() throws DaoException {
		try {

			int nbOfResa = 0;

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(COUNT_RESERVATION_QUERY);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				nbOfResa = resultSet.getInt("count");
			}

			resultSet.close();
			ps.close();
			connection.close();

			return nbOfResa;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
	}

}
