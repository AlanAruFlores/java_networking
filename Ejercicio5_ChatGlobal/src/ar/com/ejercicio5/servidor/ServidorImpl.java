package ar.com.ejercicio5.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import ar.com.ejercicio5.dominio.Conexion;
import ar.com.ejercicio5.hilos.HiloEscuchar;

public final class ServidorImpl {
	private ServerSocket servidor;
	
	private final Integer NUMERO_PUERTO  = 3334;
	
	private Set<Conexion> conexiones = new HashSet<>();
	
	
	public ServidorImpl() {}
	
	public void ejecutar() {
		try{
			this.servidor = new ServerSocket(NUMERO_PUERTO);
			Thread hEscuchar = new Thread(new HiloEscuchar(this));
			hEscuchar.setDaemon(true);
			hEscuchar.start();
			
			BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Ingrese un caracter para terminar.");
			entrada.readLine();

			cerrarTodo();
			entrada.close();
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}finally {
			try {
				servidor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void cerrarTodo() {
		try {
			for(Conexion c : this.conexiones) {
					c.getEscritura().writeUTF("close_all");
					c.getEscritura().flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ServerSocket getServerSocket() {
		return this.servidor;
	}
	
	public void agregarConexion(Conexion conexion) {
		this.conexiones.add(conexion);
	}
	
	public Set<Conexion> getConexiones(){
		return this.conexiones;
	}
}
