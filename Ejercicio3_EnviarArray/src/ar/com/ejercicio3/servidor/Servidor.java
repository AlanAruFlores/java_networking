package ar.com.ejercicio3.servidor;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	public static void main(String[] args) {
		try (ServerSocket servidor = new ServerSocket(3335)) {
			System.out.println("Esperando un cliente...");
			
			Socket cliente  =  servidor.accept();
			
			if(cliente.isConnected())
				System.out.println("El cliente esta conectado!!");
			
			int []arr = {4,5,2,1,4,6,7,8};
			
			ObjectOutputStream escritura = new ObjectOutputStream(cliente.getOutputStream());
			
			escritura.writeObject(arr);
			escritura.flush();
			
			
			DataInputStream lectura = new DataInputStream(cliente.getInputStream());
			
			String respuesta = lectura.readUTF();
			
			System.out.println(respuesta);
			
			escritura.close();
			lectura.close();
			servidor.close();
			
			
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
