package ar.com.ejercicio5.hilos;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import ar.com.ejercicio5.dominio.Conexion;
import ar.com.ejercicio5.servidor.ServidorImpl;

public final class HiloEscuchar implements Runnable {
	
	private ServidorImpl servidor;
	
	public HiloEscuchar(ServidorImpl servidor) {
		this.servidor = servidor;
		
	}
	
	@Override
	public void run() {
			try {
				while(true) {
					System.out.println("Esperando nuevas conexiones...");
					Socket sckCliente = servidor.getServerSocket().accept();
					String nombreCliente = new DataInputStream(sckCliente.getInputStream()).readUTF();
					System.out.println("El "+nombreCliente+" se ha conectado");
					Conexion conexionNueva = new Conexion(nombreCliente, sckCliente);
					servidor.agregarConexion(conexionNueva);
					
					
					//Creo un hilo propio del cliente para escuchar sus mensajes
					Thread hSesion  = new Thread(new HiloSesion(conexionNueva,servidor));
					hSesion.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}
}
