package ar.com.ejercicio4.cliente;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

import ar.com.ejercicio4.dominio.Persona;

public class Cliente {
	
	public static void main(String[] args) {
		try(Socket cliente  = new Socket("localhost",3336)){
			if(cliente.isConnected()) //evaluo que se haya conectado exitosamente el socket
				System.out.println("Conectado!!");
			
			
			BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
			
			Long id = 0L;
			System.out.println("Ingrese el id de la persona a obtener: ");
			id = Long.parseLong(entrada.readLine());
		
			DataOutputStream escritura = new DataOutputStream(cliente.getOutputStream());
			escritura.writeLong(id);
			escritura.flush();//mando el id de la persona
			
			ObjectInputStream lectura = new ObjectInputStream(cliente.getInputStream());
			
			//Deserealizo el objeto
			Persona personaBuscada = (Persona)lectura.readObject();
			
			//muestro el estado del objeto
			System.out.println(personaBuscada);
			
			
			if(personaBuscada != null)
				escritura.writeUTF("El cliente recibio el objeto");
			else
				escritura.writeUTF("El cliente no recibio el objeto esperado");
			
			escritura.close();
			lectura.close();
			
			entrada.close();
			
			cliente.close();
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}catch(ClassNotFoundException ex) {
			ex.printStackTrace(); //Sucede si hubo un error a la hora de deserealizar
		}
	}
}
