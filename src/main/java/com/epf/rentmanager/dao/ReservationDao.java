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
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;

public class ReservationDao {

	private static ReservationDao instance = null;
	private ReservationDao() {}
	
	public static ReservationDao getInstance() {
		if(instance == null) {
			instance = new ReservationDao();
		}
		return instance;
	}
	
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String UPDATE_RESERVATION_QUERY = "UPDATE Reservation SET debut=?, fin=? WHERE id=?;";
	
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = 
			"SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance, "
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places "
			+ "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id "
			+ "WHERE Reservation.client_id = ?;";
	
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = 
			"SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance,"
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places "
			+ "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id "
			+ "WHERE Reservation.vehicle_id=?;";
	
	private static final String FIND_RESERVATIONS_QUERY = 
			"SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance,"
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places "
			+ "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id ";
	
	private static final String FIND_RESERVATION_QUERY = 
			"SELECT Reservation.id, Reservation.vehicle_id, Reservation.debut, Reservation.fin,Reservation.client_id, "
			+ "Client.nom, Client.prenom,Client.email, Client.naissance,"
			+ "Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places "
			+ "FROM Reservation "
			+ "INNER JOIN Client ON Reservation.client_id= Client.id "
			+ "INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id "
			+ "WHERE Reservation.id = ?";
	
	private static final String COUNT_RESERVATION_QUERY = "SELECT COUNT(id) AS count FROM Reservation;";
	
	
	public long create(Reservation reservation) throws DaoException {
		
		long id = 0;
		
		try {
			
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps =connection.prepareStatement(CREATE_RESERVATION_QUERY,Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, reservation.getClient().getId());
			ps.setLong(2, reservation.getVehicle().getId());
			ps.setDate(3, reservation.getDebut());
			ps.setDate(4, reservation.getFin());
			
			
			
			ps.executeUpdate();
			ResultSet resultSet = ps.getGeneratedKeys();
			if(resultSet.next()) {
				id = resultSet.getLong(1);
			}
			ps.close();
			connection.close();
			return id;
			
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
			
		}
		
		
	}
	
	public int delete(Reservation reservation) throws DaoException {
		
		try {
			
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps =connection.prepareStatement(DELETE_RESERVATION_QUERY);
			ps.setLong(1, reservation.getId());
			
			int nb_ligne_delete = ps.executeUpdate();
			ps.close();
			connection.close();
			return nb_ligne_delete;
			
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
			
		}
		
		
	}
	
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

	
	public List<Reservation> findResaByClientId(long clientId) throws DaoException {
		
		List<Reservation> list_reservation = new ArrayList<>();
		try {
			
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);
			ps.setLong(1,clientId);
			ResultSet resultSet = ps.executeQuery();
			
			while(resultSet.next()) {
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
	
	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
		
		List<Reservation> list_reservation = new ArrayList<>();
		try {
			
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY);	
			ps.setLong(1, vehicleId);
			ResultSet resultSet = ps.executeQuery();
			while(resultSet.next()) {
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

	public List<Reservation> findAll() throws DaoException {
		
		List<Reservation> list_reservation = new ArrayList<>();
		try {
			
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_QUERY);	
			ResultSet resultSet = ps.executeQuery();
			while(resultSet.next()) {
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
	
public Optional<Reservation> findById(long Id) throws DaoException {
		
		Optional<Reservation> opt_resa;
		try {
			
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATION_QUERY);	
			ps.setLong(1,Id);
			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()) {
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
				
			}else {
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
			// TODO Auto-generated catch block
			throw new DaoException(e.getMessage());
		}
	}
}
