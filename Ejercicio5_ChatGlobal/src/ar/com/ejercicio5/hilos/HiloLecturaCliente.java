package ar.com.ejercicio5.hilos;

import java.io.IOException;

import ar.com.ejercicio5.cliente.ClienteImpl;

public class HiloLecturaCliente implements Runnable{

private ClienteImpl cliente;
	
	public HiloLecturaCliente(ClienteImpl cliente) {
		this.cliente = cliente;
	}
	
	@Override
	public void run() {
		
		String msj = "";
			try {
				ciclo : while(true) {

					msj = cliente.getLectura().readUTF();
					if(msj.equalsIgnoreCase("close_all"))
						break ciclo;
					System.out.println(msj);
				}
				this.cliente.flag = false;

			}catch(IOException ex) {
				this.cliente.flag=false;
				//ex.printStackTrace();
			}
		

	}

}
