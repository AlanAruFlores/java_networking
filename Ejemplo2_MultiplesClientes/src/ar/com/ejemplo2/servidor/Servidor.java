package ar.com.ejemplo2.servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ar.com.ejemplo2.dominio.Usuario;

public class Servidor {

	static List<Usuario> usuariosConectados = new ArrayList<>();
	static ServerSocket servidor;	
	
	
	//static Boolean seguir = true;
	
	//HILOS:
	
	//Hilo para escuchar conexiones
	static Thread hiloEscucharConexiones = new Thread() {
		public void run() {
			int i = 1;
			try {
				while (true) {
					System.out.println("Escuchando conexiones...");
					Socket cliente = servidor.accept();
					if(cliente.isConnected()) {
						System.out.println("Se conecto el usuario "+i);
						usuariosConectados.add(new Usuario("Usuario "+i,cliente));
						i++;
					}
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	};
	
	public static void main(String[] args) {
		
		try{
			BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
			servidor = new ServerSocket(3334);
			//Inicio el servidor y va a escuchar conexiones
			hiloEscucharConexiones.setDaemon(true);

			hiloEscucharConexiones.start();
			System.out.println("Escribe algo para cerrar el servidor");
			String palabra = entrada.readLine();
			mandarMensajeAClientes(palabra);
			vaciarLista();
		/*
			seguir = false;
			hiloEscucharConexiones.interrupt();*/
			entrada.close();
			servidor.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private static void mandarMensajeAClientes(String mensaje) {
	
		try {
			
			for(Usuario u : usuariosConectados) {
				Socket aux = u.getSocket();
				DataOutputStream escritura = new DataOutputStream(aux.getOutputStream());
				escritura.writeUTF(mensaje);
				escritura.flush();
			}	
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}

	}
	
	
	private static void vaciarLista() {
		usuariosConectados.clear();
	}
}
