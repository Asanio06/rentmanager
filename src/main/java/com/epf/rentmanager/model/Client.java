package com.epf.rentmanager.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class Client {
	private long id;
	private String nom;
	private String prenom;
	private String email;
	private Date naissance;
	
	public Client() {
		
	}
	
	public Client(long id, String nom, String prenom, String email, Date naissance) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.naissance = naissance;
	}
	
	@Override
	public String toString() {
		return "[id= " + id + ", nom= " + nom + ", prenom= " + prenom + ", email= " + email + ", naissance= "
				+ naissance + " ]";
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getNaissance() {
		return naissance;
	}
	public void setNaissance(Date naissance) {
		this.naissance = naissance;
	}
	
	public int getAge() {
		LocalDate birthday = this.naissance.toLocalDate();
		LocalDate today = LocalDate.now();
		Period p = Period.between(birthday, today);
		int age = p.getYears();
		return age;
	}
}
