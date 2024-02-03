package ar.com.ejercicio5.hilos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ar.com.ejercicio5.cliente.ClienteImpl;

public  class HiloEscrituraCliente implements Runnable{

	private ClienteImpl cliente;
	
	public HiloEscrituraCliente(ClienteImpl cliente) {
		this.cliente = cliente;
	}
	
	@Override
	public void run() {
		
		BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));

		String msjCliente = "";
		

			try {
				while(!msjCliente.equalsIgnoreCase("stop")) {

					msjCliente = entrada.readLine();
					cliente.getEscritura().writeUTF(msjCliente);
					cliente.getEscritura().flush();
					System.out.println();
				}
				this.cliente.flag = false;

			} catch (IOException e) {
				this.cliente.flag = false;
				//e.printStackTrace();
			}
		
		
	}
}
