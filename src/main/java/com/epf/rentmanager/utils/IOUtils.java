package com.epf.rentmanager.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Scanner;

public class IOUtils {
	
	/**
	 * Affiche un message sur la sortie standard
	 * @param message
	 */
	public static void print(String message) {
		System.out.println(message); 
	}
	
	/**
	 * Affiche un message sur la sortie standard
	 * @param message
	 * @param mandatory
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
	 * @return
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
	 * @param message
	 * @return
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
	 * @param message
	 * @param mandatory
	 * @return
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
	
	public static Date subtractDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -days);
        return new Date(c.getTimeInMillis());
    }
	
	public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return new Date(c.getTimeInMillis());
    }
	
	public static long dateDiff(Date date1, Date date2) {
		
		return ChronoUnit.DAYS.between(LocalDate.parse(date1.toString()),LocalDate.parse(date2.toString()));

	}
}
