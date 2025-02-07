package ar.com.ejercicio6.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Jugador implements Serializable{
 
	private static final long serialVersionUID = 10L;

	private String nombreJugador;
	private Pokemon pokemon;
	private List<Pocion> inventario;
	
	
	public Jugador(String nombreJugador,
			Pokemon pokemon) {
		this.nombreJugador  = nombreJugador;
		this.pokemon = pokemon;
		this.inventario = new ArrayList<Pocion>();
	}

	public String getNombreJugador() {
		return nombreJugador;
	}

	public void setNombreJugador(String nombreJugador) {
		this.nombreJugador = nombreJugador;
	}

	public Pokemon getPokemon() {
		return pokemon;
	}

	public List<Pocion> getInventario(){
		return this.inventario;
	}
	
	public void setPocion(Pocion pocion) {
		this.inventario.add(pocion);
	}
	
	public void setInventario(List<Pocion> pociones) {
		this.inventario.clear();
		this.inventario.addAll(pociones);
	}
	
	public void setPokemon(Pokemon pokemon) {
		this.pokemon = pokemon;
	}
	
	@Override
	public String toString() {
		String txt = "JUGADOR: "+this.nombreJugador+""
				+ "\nPOKEMON: "+this.pokemon;
		return txt;
	}
	
}
