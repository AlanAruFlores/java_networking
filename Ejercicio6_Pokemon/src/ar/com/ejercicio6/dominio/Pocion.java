package ar.com.ejercicio6.dominio;

import java.io.Serializable;

public abstract class Pocion implements Serializable{
	
	private static final long serialVersionUID = 6L;
	
	private String nombrePocion;
	
	public Pocion(String nombrePocion) {
		this.nombrePocion = nombrePocion;
	}
	
	public String getNombre() {
		return this.nombrePocion;
	}

	@Override
	public String toString() {
		return "Pocion [nombrePocion=" + nombrePocion + "]";
	}
	
	
}
