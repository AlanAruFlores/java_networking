package ar.com.ejercicio3.cliente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Cliente {

	public static void main(String[] args) {
		try(Socket cliente = new Socket ("localhost",3335)){
			
			if(cliente.isConnected())
				System.out.println("Conectado al Servidor exitosamente !!");
			
			ObjectInputStream lectura = new ObjectInputStream(cliente.getInputStream());
			
			int [] arr = (int[])(lectura.readObject());
			
			for(int i = 0 ; i<arr.length; i++)
				System.out.println(arr[i]);
			

			DataOutputStream escritura = new DataOutputStream(cliente.getOutputStream());
			escritura.writeUTF("El cliente mostro el arreglo enviado ! ");
			escritura.flush();
			
			
			escritura.close();
			lectura.close();
			cliente.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}catch(ClassNotFoundException ex) {
			//sucede si el parseo es incorrecto en la lectura del objeto
			ex.printStackTrace();
		}
	}
}
