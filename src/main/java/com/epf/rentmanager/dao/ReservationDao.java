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

import com.epf.rentmanager.exception.DaoException;
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
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
	private static final String COUNT_RESERVATION_QUERY = "SELECT COUNT(id) AS count FROM Reservation;";
	public long create(Reservation reservation) throws DaoException {
		
		long id = 0;
		
		try {
			
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps =connection.prepareStatement(CREATE_RESERVATION_QUERY,Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, reservation.getClient_id());
			ps.setLong(2, reservation.getVehicle_id());
			ps.setDate(3, reservation.getDebut());
			ps.setDate(4, reservation.getFin());
			
			
			
			ps.executeUpdate();
			ResultSet resultSet = ps.getGeneratedKeys();
			if(resultSet.next()) {
				id = resultSet.getLong(1);
			}
			ps.close();
			connection.close();
			
			
		} catch (SQLException e) {
			throw new DaoException();
			
		}
		
		return id;
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
			throw new DaoException();
			
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
				reservation.setClient_id(resultSet.getInt("client_id"));
				reservation.setVehicle_id(resultSet.getInt("vehicle_id"));
				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));
				
				list_reservation.add(reservation);
				
			}
			resultSet.close();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			throw new DaoException();
		}
		
		return list_reservation;
		
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
				reservation.setClient_id(resultSet.getInt("client_id"));
				reservation.setVehicle_id(resultSet.getInt("vehicle_id"));
				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));
				
				list_reservation.add(reservation);
				
			}
			resultSet.close();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			throw new DaoException();
		}
		
		return list_reservation;
		
		 
	}

	public List<Reservation> findAll() throws DaoException {
		
		List<Reservation> list_reservation = new ArrayList<>();
		try {
			
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_QUERY);	
			ResultSet resultSet = ps.executeQuery();
			while(resultSet.next()) {
				Reservation reservation = new Reservation();
				reservation.setClient_id(resultSet.getInt("client_id"));
				reservation.setVehicle_id(resultSet.getInt("vehicle_id"));
				reservation.setDebut(resultSet.getDate("debut"));
				reservation.setFin(resultSet.getDate("fin"));
				
				list_reservation.add(reservation);
				
			}
			resultSet.close();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			throw new DaoException();
		}
		
		return list_reservation;
		 
	}
	
	public long nbOfResa() throws DaoException {
		try {
			
			long nbOfResa = 0;

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(COUNT_RESERVATION_QUERY);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				nbOfResa = resultSet.getLong("count");
			}
			
			resultSet.close();
			ps.close();
			connection.close();

			return nbOfResa;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
			throw new DaoException();
		}
	}
}
