package ar.com.ejercicio5.dominio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public final class Conexion implements Serializable{

	private static final long serialVersionUID = 1L;

	private static Integer cont_id = 1;
	
	private final Integer ID = cont_id++;
	private String nombre;
	private Socket socket;
	private DataInputStream lectura;
	private DataOutputStream escritura;
	
	public Conexion(String nombre, Socket socket) {
		try {
			this.nombre = nombre;
			this.socket = socket;
			this.lectura = new DataInputStream(socket.getInputStream());
			this.escritura = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Integer getId() {
		return this.ID;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public DataInputStream getLectura() {
		return this.lectura;
		
	}
	
	public DataOutputStream getEscritura() {
		return this.escritura;
	}
	
}
