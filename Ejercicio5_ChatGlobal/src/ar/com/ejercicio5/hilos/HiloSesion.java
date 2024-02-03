package ar.com.ejercicio5.hilos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

import ar.com.ejercicio5.dominio.Conexion;
import ar.com.ejercicio5.servidor.ServidorImpl;

public final class HiloSesion implements Runnable {
	
	private Conexion conexionCliente;
	private ServidorImpl servidor;
	
	public HiloSesion(Conexion conexion, ServidorImpl servidor) {
		this.conexionCliente = conexion;
		this.servidor = servidor;
	}
	
	@Override
	public void run() {
		String msjCliente = "";
			try {	
				while (!msjCliente.equalsIgnoreCase("stop")) {

					msjCliente = conexionCliente.getLectura().readUTF();
					
					if(!msjCliente.equalsIgnoreCase("stop"))
						enviarMensaje(this.conexionCliente.getNombre()+" : "+msjCliente);
					
				}	
				eliminarCliente();

			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	public void enviarMensaje(String msj) {
		Set<Conexion> conexiones = servidor.getConexiones();
			try {
					for(Conexion c : conexiones) {
	
					DataOutputStream escritura = c.getEscritura();
					escritura.writeUTF(msj);
					escritura.flush();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void eliminarCliente() {
		Set<Conexion> conexiones = servidor.getConexiones();
		conexiones.remove(conexionCliente);
		try {
			this.conexionCliente.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Se desconecto "+this.conexionCliente.getNombre());
	}
}
