package ar.com.ejemplo2.cliente;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente {
	
	public static void main(String[] args) {
		
		try(Socket cliente = new Socket("localhost",3334)){
			if(cliente.isConnected())
				System.out.println("El cliente se conecto al servidor...");
			DataInputStream lectura = new DataInputStream(
					cliente.getInputStream());
			
			String msj = lectura.readUTF();
			System.out.println(msj);
			
			lectura.close();
			cliente.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
