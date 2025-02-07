package ar.com.gestor.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import ar.com.gestor.servidor.hilo.HiloLeer;

public class Servidor {

	private final Integer PUERTO = 3335;

	private ServerSocket sckServidor;
	private Socket sckCliente;

	
	public Boolean flag = true;
	
	public void ejecutar() {
		try {
			sckServidor = new ServerSocket(PUERTO);
			System.out.println("Esperando conexiones....");
			sckCliente = sckServidor.accept();
			System.out.println("Listo para recibir consultas !! ");
			Thread hiloLeer = new Thread (new HiloLeer(this, sckCliente));
			hiloLeer.start();
			while(flag) {}
			sckCliente.close();
			sckServidor.close();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}

	}
}
