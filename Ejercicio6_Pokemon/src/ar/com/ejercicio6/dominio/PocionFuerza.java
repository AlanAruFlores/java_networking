package ar.com.ejercicio6.dominio;

public final class PocionFuerza extends Pocion {

	private static final long serialVersionUID = 7L;

	private final Integer DANIO=20;
	
	public PocionFuerza() {
		super("POCION DE FUERZA");
	}
	
	public Integer getDanio() {
		return this.DANIO;
	}

	@Override
	public String toString() {
		return "PocionFuerza [DANIO=" + DANIO + " "+super.toString()+"]";
	}
	
	
}
