package com.epf.rentmanager.model;

import java.sql.Date;

public class Reservation {
	private long id;
	private Client client;
	private Vehicle vehicle;
	private Date debut;
	private Date fin;
	
	public Reservation() {
		
	}
	
	

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", client=" + client.getId() + ", vehicle=" + vehicle.getId() + ", debut=" + debut + ", fin="
				+ fin + "]";
	}



	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}


	public Vehicle getVehicle() {
		return vehicle;
	}



	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}



	public Date getDebut() {
		return debut;
	}
	public void setDebut(Date debut) {
		this.debut = debut;
	}
	
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
}
