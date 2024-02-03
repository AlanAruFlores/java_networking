package ar.com.ejercicio2.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	
	public static void main(String[] args) {
		int numeroRandom = obtenerNumeroRandom(1, 10);
		try (ServerSocket servidor = new ServerSocket(3334)){
			System.out.println("Esperando Cliente...");
			Socket cliente = servidor.accept();
			System.out.println("Cliente conectado ...");
		
		
			DataInputStream entrada = new DataInputStream(cliente.getInputStream());
			DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
			
			//Mande el numero entero
			salida.writeInt(numeroRandom);
			salida.flush();
			String respuesta = entrada.readUTF();
			
			System.out.println(respuesta);
			
			entrada.close();
			salida.close();
			
			
			servidor.close();
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	private static int obtenerNumeroRandom(int min, int max) {
		return (int)(Math.random()*max+min);
	}
}
