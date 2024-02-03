package ar.com.ejercicio2.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente {
	public static void main(String[] args)
	{
		try(Socket cliente = new Socket("localhost", 3334)){
			System.out.println("Se conecto al servidor !! ");
			
			DataInputStream entrada = new DataInputStream(cliente.getInputStream());
			DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());

			int resultado  = entrada.readInt();
			mostrarTabla(resultado);
			
			salida.writeUTF("Se mostro la tabla del "+resultado);
			salida.flush();
			
			entrada.close();
			salida.close();
			cliente.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
		
		
	}
	
	private static void mostrarTabla(int n) {
		for(int i  = 0 ; i <= 10; i++)
			System.out.println(n+" X " +i+" = "+(n*i));
	}
}
