import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	/*
	public static void main(String [] args) throws IOException {
		ServerSocket servidor = new ServerSocket(3333);
	
		System.out.println("Esperando la conexion con el cliente....");
		Socket cliente = servidor.accept();
		
		System.out.println("Conexion con cliente establecida");
	
		DataInputStream inputClient = new DataInputStream(cliente.getInputStream());
	
		String mensaje = inputClient.readUTF();
		System.out.println("Cliente: "+mensaje);
		
		
		servidor.close();
		
	}*/
	
	public static void main(String[] args) throws IOException {
		ServerSocket servidor = new ServerSocket(3333);
		System.out.println("Esperando una conexion....");
		Socket cliente = servidor.accept();
		
		DataInputStream input = new DataInputStream(cliente.getInputStream());
		DataOutputStream output = new DataOutputStream(cliente.getOutputStream());
		
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		
		String mensajeCliente="";
		String mensajeServidor = "";
		
		while(!mensajeCliente.equals("stop")) {
			mensajeCliente = input.readUTF();
			System.out.println("Cliente: "+mensajeCliente
					);
			mensajeServidor  =read.readLine();
			output.writeUTF(mensajeServidor);
			output.flush();
		}
		
		input.close();
		output.close();
		read.close();
		servidor.close();
	}
}
