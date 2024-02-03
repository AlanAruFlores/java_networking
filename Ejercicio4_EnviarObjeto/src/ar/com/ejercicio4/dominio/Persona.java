package ar.com.ejercicio4.dominio;

import java.io.Serializable;

import ar.com.ejercicio4.util.Nacionalidad;

public final class Persona implements Serializable{
	//Nos servira para distinguir la clase a la hora de serealizar
	private static final long serialVersionUID = 1L;
	
	
	private String nombre;
	private String apellido;
	private Integer edad;
	private Nacionalidad nacionalidad;
	
	public Persona(String nombre, String apellido, Integer edad, Nacionalidad nacionalidad) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.nacionalidad = nacionalidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public Nacionalidad getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(Nacionalidad nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	@Override
	public String toString() {
		return "Persona [nombre=" + nombre + ", apellido=" + apellido + ", edad=" + edad + ", nacionalidad="
				+ nacionalidad + "]";
	}
	
}
