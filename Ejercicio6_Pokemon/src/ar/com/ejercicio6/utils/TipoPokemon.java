package ar.com.ejercicio6.utils;

public enum TipoPokemon {
	
	FUEGO(4), AGUA(3), PLANTA(2);

	private Integer multiplicador;
	
	private TipoPokemon(Integer multiplicador) {
		this.multiplicador = multiplicador;
	}
	
	
	public Integer getMultiplicador() {
		return this.multiplicador;
	}

}
