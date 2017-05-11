package com.sasousuke.birthday;

import java.util.Calendar;
import java.util.Date;

public class Persona extends Object{
	public int id;
	public String nombre;
	public Date nacimiento;
	
	public Persona() {
		// TODO Auto-generated constructor stub
		id = 0;
		nombre = "";
		nacimiento = null;
	}
	public Persona (int pid, String pnombre, Date pnacimiento) {
		id = pid;
		nombre = pnombre;
		nacimiento = pnacimiento;
	}
	
	/*
	 * Determina la edad del contacto
	 * @return Cantidad de años
	 */
	public int age (){
		int result = 0;
		Calendar rightNow = Calendar.getInstance();
		
		Calendar birthday = Calendar.getInstance();
		birthday.setTime(nacimiento);
		
		/* La diferencia de años se determina por
		 * Diferencia de años
		*/ 	
		result = rightNow.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
		
		/* Luego se descuenta un año si:
		 * El dia en curso es anterior al de nacimiento para el mismo año
		*/ 	
		birthday.set(Calendar.YEAR, rightNow.get(Calendar.YEAR));
		
		if (rightNow.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR))
			result --;
		return result;
	}
	
	/*
	 * Determina cuantos dias faltan para el proximo cumpleanos
	 * @return Cantidad de dias
	 */
	public int dayForNextBirthday (){
		int result = 0;
		Calendar rightNow = Calendar.getInstance();

		Calendar birthday = Calendar.getInstance();
		// Asigno la fecha de nacimiento al calendar
		birthday.setTime(nacimiento);
		// Luego llevo al año actual la fecha de nacimiento
		birthday.set(Calendar.YEAR, rightNow.get(Calendar.YEAR));
		// Si aún no ha cumplido años en este año
		if (rightNow.get(Calendar.DAY_OF_YEAR) <= birthday.get(Calendar.DAY_OF_YEAR)){
			// La diferencia en dias es dado el numero del dia del año
			result = birthday.get(Calendar.DAY_OF_YEAR) - rightNow.get(Calendar.DAY_OF_YEAR);
		}
		else{
			// Ya cumplio anos y por tanto el nuevo cumpleaños sera el ano siguiente
			birthday.set(Calendar.YEAR, rightNow.get(Calendar.YEAR) + 1);
			result = (rightNow.getMaximum(Calendar.DAY_OF_YEAR) - rightNow.get(Calendar.DAY_OF_YEAR)) 
					+ birthday.get(Calendar.DAY_OF_YEAR);
		}
		return result;
	}
}
