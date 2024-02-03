package ar.com.ejercicio5.cliente;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import ar.com.ejercicio5.hilos.HiloEscrituraCliente;
import ar.com.ejercicio5.hilos.HiloLecturaCliente;

public class ClienteImpl {
	
	private Socket cliente;
	private String nombreCliente;
	
	private final String NOMBRE_SERVIDOR="localhost";
	private final Integer PUERTO_SERVIDOR = 3334;
	
	private DataInputStream lectura;
	private DataOutputStream escritura;
	
	public Boolean flag = true;
	
	public ClienteImpl() {}
	
	public void ejecutar() {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("INGRESE SU NOMBRE: ");
			nombreCliente = entrada.readLine();
			
			cliente = new Socket(NOMBRE_SERVIDOR,PUERTO_SERVIDOR);
			lectura = new DataInputStream(cliente.getInputStream());
			escritura = new DataOutputStream(cliente.getOutputStream());
			escritura.writeUTF(this.nombreCliente);
			escritura.flush();
			
			Thread hEscritura = new Thread(new HiloEscrituraCliente(this));
			hEscritura.setDaemon(true);
			hEscritura.start();
			
			
			Thread hLectura = new Thread(new HiloLecturaCliente(this));
			hLectura.setDaemon(true);
			hLectura.start();
			
			while(flag) {}
			
			/*
			String msjCliente = "";
			
			while(!msjCliente.equalsIgnoreCase("stop")) {
				msjCliente = entrada.readLine();
				escritura.writeUTF(msjCliente);
				escritura.flush();
				System.out.println();
			}
			
			String msj = "";
			ciclo : while(true) {
				try {
					msj = getLectura().readUTF();
					if(msj.equalsIgnoreCase("close_all"))
						break ciclo;
					System.out.println(msj);
				}catch(IOException ex) {
					ex.printStackTrace();
				}
			}
			*/
			lectura.close();
			escritura.close();
			cliente.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public DataInputStream getLectura() {
		return this.lectura;
	}
	
	public DataOutputStream getEscritura() {
		return this.escritura;
	}
}
