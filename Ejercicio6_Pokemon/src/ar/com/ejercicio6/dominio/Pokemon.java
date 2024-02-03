package ar.com.ejercicio6.dominio;

import java.io.Serializable;

import ar.com.ejercicio6.utils.TipoPokemon;

public abstract class Pokemon implements Serializable{
	
	private static final long serialVersionUID = 2L;
	
	private String nombre;
	private Integer vida;
	private Integer ataque;
	private TipoPokemon tipo;
	
	public Pokemon(String nombre, Integer vida, Integer ataque, TipoPokemon tipo) {
		super();
		this.nombre = nombre;
		this.vida = vida;
		this.ataque = ataque;
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getVida() {
		return vida;
	}

	public void setVida(Integer vida) {
		this.vida = vida;
	}

	public Integer getAtaque() {
		return ataque;
	}

	public void setAtaque(Integer ataque) {
		this.ataque = ataque;
	}

	public TipoPokemon getTipo() {
		return tipo;
	}

	public void setTipo(TipoPokemon tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Pokemon [nombre=" + nombre + ", vida=" + vida + ", ataque=" + ataque + ", tipo=" + tipo + "]";
	}
	
	
}
