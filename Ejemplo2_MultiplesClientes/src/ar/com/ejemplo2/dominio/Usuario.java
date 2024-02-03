package ar.com.ejemplo2.dominio;

import java.net.Socket;

public class Usuario {
	private String nombre;
	private Socket socket;
	
	
	public Usuario(String nombre, Socket socket) {
		super();
		this.nombre = nombre;
		this.socket = socket;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + "]";
	}
	
	
	
}
