package ar.com.gestor.cliente;
import java.io.IOException;
import java.net.Socket;

import ar.com.gestor.cliente.hilo.HiloCliente;

public class Cliente {
	
	private Socket sck;

	
	private final Integer PUERTO_SERVIDOR = 3335;
	private final String HOST_SERVIDOR = "localhost";
	
	public Boolean flag = true;
	
	public void ejecutar() {
		try {
			sck = new Socket(HOST_SERVIDOR,PUERTO_SERVIDOR);
			System.out.println("Conectado al servidor de base de datos");
			Thread hiloCliente = new Thread(new HiloCliente(this, sck));
			hiloCliente.start();
			while(flag) {}
			sck.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
