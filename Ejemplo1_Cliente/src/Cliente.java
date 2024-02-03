import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
	/*
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket cliente = new Socket("localhost",3333);
		
		DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
		salida.writeUTF("Hello Server");
		salida.flush();
		
		salida.close();
		cliente.close();
		
	}
	*/
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket cliente = new Socket("localhost", 3333);
		
		DataInputStream input = new DataInputStream(cliente.getInputStream());
		DataOutputStream output  = new DataOutputStream (cliente.getOutputStream());
		
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		
		String mensajeCliente = "";
		String mensajeServidor = "";
		
		while(!mensajeCliente.equals("stop")) {
			mensajeCliente = read.readLine();
			output.writeUTF(mensajeCliente);
			mensajeServidor = input.readUTF();
			System.out.println("Servidor: "+mensajeServidor);
		}
		
		input.close();
		output.close();
		
		read.close();
		
		cliente.close();
		
		
			
	}
}
