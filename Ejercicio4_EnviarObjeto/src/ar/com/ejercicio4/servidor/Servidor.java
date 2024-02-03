package ar.com.ejercicio4.servidor;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import ar.com.ejercicio4.dominio.Persona;
import ar.com.ejercicio4.util.Nacionalidad;

public class Servidor {
	
	static HashMap<Long, Persona> datos = new HashMap<Long, Persona>(){
	    private static final long serialVersionUID = 2L;
		{

			put(45542324L, new Persona("Pepe", "Flores", 24, Nacionalidad.ARGENTINA));
			put(45233422L, new Persona("Martin", "Ferreira", 10, Nacionalidad.BRASIL));
			put(43342223L, new Persona("Clara", "Mente", 44, Nacionalidad.CHILE));
			put(42334424L, new Persona("Miguel", "Flores", 24, Nacionalidad.MEXICO));
		}
	};
	
	public static void main(String [] args) {
		try(ServerSocket servidor = new ServerSocket(3336)){
			System.out.println("Esperando una conexion....");
			Socket cliente = servidor.accept();
			
			System.out.println("Cliente conectado !!");
			
			DataInputStream lectura = new DataInputStream(cliente.getInputStream());
			
			Long dato = lectura.readLong();
			
			Persona personaBuscada = datos.get(dato);
			if(personaBuscada == null)
				System.out.println("No se encontro a la persona!!. Cuyo id es: "+dato);
			else
				System.out.println("Se enconro a la persona!! . Cuyo id es: "+dato);
			
			ObjectOutputStream escritura = new ObjectOutputStream(cliente.getOutputStream());
			
			//mando el objeto
			escritura.writeObject(personaBuscada);
			escritura.flush(); //limpio el buffer y mando el objeto
			
			String respuesta = lectura.readUTF(); //Espero una respuesta por parte del cliente
			System.out.println(respuesta);
			
			escritura.close();
			lectura.close();
			servidor.close();
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
}
