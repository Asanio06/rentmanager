package com.epf.rentmanager.model;

import java.sql.Date;

public class Reservation {
	private long id;
	private long client_id;
	private long vehicle_id;
	private Date debut;
	private Date fin;
	
	public Reservation() {
		
	}
	
	public Reservation(long id, long client_id, long vehicle_id, Date debut, Date fin) {
		super();
		this.id = id;
		this.client_id = client_id;
		this.vehicle_id = vehicle_id;
		this.debut = debut;
		this.fin = fin;
	}
	
	
	
	@Override
	public String toString() {
		return "Reservation [id=" + id + ", client_id=" + client_id + ", vehicle_id=" + vehicle_id + ", debut=" + debut
				+ ", fin=" + fin + "]";
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getClient_id() {
		return client_id;
	}
	public void setClient_id(long client_id) {
		this.client_id = client_id;
	}
	public long getVehicle_id() {
		return vehicle_id;
	}
	public void setVehicle_id(long vehicle_id) {
		this.vehicle_id = vehicle_id;
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
