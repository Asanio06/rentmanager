package com.epf.rentmanager.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Pattern;

public class IOUtils {
	
	/**
	 * Affiche un message sur la sortie standard
	 * @param message Le message à afficher
	 */
	public static void print(String message) {
		System.out.println(message); 
	}
	
	/**
	 * Affiche un message sur la sortie standard
	 * @param message Le message à afficher
	 * @param mandatory True si la saisie est obligatoire; False sinon
	 */
	public static String readString(String message, boolean mandatory) {
		print(message);
		
		String input = null;
		int attempt = 0;
		
		do {
			if (attempt >= 1) {
				print("Cette valeur est obligatoire");
			}
			
			input = readString();
			attempt++;
		} while (mandatory && (input.isEmpty() || input == null));
		
		return input;
	}
	
	/**
	 * Lit un message sur l'entrée standard
	 */
	public static String readString() {
		Scanner scanner = new Scanner(System.in);
		String value = scanner.nextLine();
		
		return value;
	}
	
	/**
	 * Lit un entier sur l'entrée standard
	 * @param message
	 * @return L'entier saisi par l'utilisateur
	 */
	public static int readInt(String message) {
		print(message);
		
		String input = null;
		int output = 0;
		boolean error = false;
		
		do {
			input = readString();
			error = false;
			
			try {
				output = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				error = true;
				print("Veuillez saisir un nombre");
			}
		} while (error);
		
		return output;
	}
	
	
	/**
	 * Lit un entier sur l'entrée standard
	 * @param message Le message qu'on souhaite afficher avant que l'utilisateur puisse saisir un Short
	 * @return Le Short saisi par l'utilisateur
	 */
	public static short readShort(String message) {
		print(message);
		
		String input = null;
		short output = 0;
		boolean error = false;
		
		do {
			input = readString();
			error = false;
			
			try {
				output = Short.parseShort(input);
			} catch (NumberFormatException e) {
				error = true;
				print("Veuillez saisir un nombre valide");
			}
		} while (error);
		
		return output;
	}
	
	
	/**
	 * Lit une date sur l'entrée standard
	 * @param message Le message qu'on souhaite afficher avant que l'utilisateur puisse saisir une date
	 * @param mandatory True si la saisie est obligatoire; False sinon
	 * @return La LocalDate saisie par l'utilisateur
	 */
	public static LocalDate readDate(String message, boolean mandatory) {
		print(message);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
		LocalDate output = null;
		boolean error = false;
		
		do {
			try {
				error = false;
				String stringDate = readString();
	        	output = LocalDate.parse(stringDate, formatter);
	        } catch (DateTimeParseException e) {
	        	error = true;
	        	print("Veuillez saisir une date valide (dd/MM/yyyy)");
	        } 
		} while (error && mandatory);
        
		return output;
	}
	
	/**
	 * Permet de soustraire un nombre de jour à une date
	 * @param date 
	 * @param days Le nombre de jour qu'on souhaite retirer
	 * @return La date obtenu après soustraction
	 */
	public static Date subtractDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -days);
        return new Date(c.getTimeInMillis());
    }
	
	/**
	 * Permet d'ajouter un nombre de jour à une date
	 * @param date 
	 * @param days Le nombre d'années qu'on souhaite retirer
	 * @return La date obtenu après soustraction
	 */
	public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return new Date(c.getTimeInMillis());
    }
	
	/**
	 * Permet de soustraire un nombre d'années à une date
	 * @param date 
	 * @param year Le nombre d'années qu'on souhaite retirer
	 * @return La date obtenu après soustraction
	 */
	public static Date subtractYear(Date date, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -year);
        return new Date(c.getTimeInMillis());
    }
	
	/**
	 * Permet d'ajouter un nombre d'années à une date
	 * @param date 
	 * @param year Le nombre d'années qu'on souhaite ajouter
	 * @return La date obtenu après ajout
	 */
	public static Date addYear(Date date, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);
        return new Date(c.getTimeInMillis());
    }
	
	
	/**
	 * Permet d'obtenir le nombre de jours entre 2 dates
	 * @param date1
	 * @param date2
	 * @return le nombre de jours entre les 2 dates
	 */
	public static long dateDiff(Date date1, Date date2) {
		
		return ChronoUnit.DAYS.between(LocalDate.parse(date1.toString()),LocalDate.parse(date2.toString()));

	}
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidMail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}
}
