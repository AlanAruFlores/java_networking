package ar.com.ejercicio6.dominio;

public final class PocionVida extends Pocion{

	private static final long serialVersionUID = 8L;

	private final Integer VIDA = 20;
	
	public PocionVida () {	
		super("POCION DE VIDA");
	}
	
	public Integer getVida() {
		return this.VIDA;
	}

	@Override
	public String toString() {
		return "PocionVida [VIDA=" + VIDA + " "+super.toString()+"]";
	}
	
	
}
