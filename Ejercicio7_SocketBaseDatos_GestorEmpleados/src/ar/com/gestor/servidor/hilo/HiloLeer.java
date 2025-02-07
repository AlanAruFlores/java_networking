package ar.com.gestor.servidor.hilo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Set;

import ar.com.gestor.dao.IDAOManager;
import ar.com.gestor.dao.IEmpleadoDAO;
import ar.com.gestor.dao.impl.DAOManagerImpl;
import ar.com.gestor.model.Empleado;
import ar.com.gestor.servidor.Servidor;

public class HiloLeer implements Runnable {

	private Servidor servidor;
	private Socket cliente;

	private DataInputStream lectura;
	private DataOutputStream escritura;
	private ObjectOutputStream escrituraObjetos;
	
	private ObjectInputStream lecturaObjetos;

	private IDAOManager manager;
	private IEmpleadoDAO empleadoDAO;
	
	public HiloLeer(Servidor servidor, Socket cliente) throws IOException, SQLException {
		this.servidor = servidor;
		this.setCliente(cliente);
		this.lectura = new DataInputStream(cliente.getInputStream());
		this.lecturaObjetos = new ObjectInputStream(cliente.getInputStream());
		this.escrituraObjetos = new ObjectOutputStream(cliente.getOutputStream());
		this.escritura = new DataOutputStream(cliente.getOutputStream());
		this.manager = new DAOManagerImpl();
		this.empleadoDAO = manager.getEmpleadoDAO();
	}

	/*
	 * 
	 * 1-insertar 2-selecciona dni 3-seleccionar todo 4-actualizar 5-eliminar
	 * 6-salir
	 */
	@Override
	public void run() {
		int opcion = -1;

		try {
			while (opcion != 6) {
				opcion = this.lectura.readInt();
				darResultado(opcion);
			}
			lectura.close();
			lecturaObjetos.close();
			escritura.close();
			escrituraObjetos.close();
			manager.cerrarConexion();
			servidor.flag = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String darResultado(int opcion) {
		String mensaje = "";
		try {
			switch (opcion) {
			case 1:
				Empleado empl = (Empleado)this.lecturaObjetos.readObject();
				Boolean result = empleadoDAO.insertar(empl);
				mensaje = (result) ? "SE INSERTO CON EXITO" : "NO SE INSERTO CON EXITO";
				this.escritura.writeUTF(mensaje);
				this.escritura.flush();
				break;
			case 2:
				int dni = this.lectura.readInt();
				Empleado emp = empleadoDAO.seleccionPorDni(dni);
				mensaje = (emp != null) ? emp.toString() : "NO SE ENCONTRO EL EMPLEADO CON CUYO DNI";
				this.escritura.writeUTF(mensaje);
				this.escritura.flush();
				break;
			case 3:
				Set<Empleado> empleados = empleadoDAO.seleccinarTodo();
				mensaje = (empleados.size() != 0) ? empleados.toString() : "NO EXISTE NINGUN EMPLEADO REGISTRADO";
				this.escritura.writeUTF(mensaje);
				this.escritura.flush();
				break;
			case 4:
				Integer dniEmpleado = this.lectura.readInt();
				Empleado empleadoEncontrado = this.empleadoDAO.seleccionPorDni(dniEmpleado);
				this.escrituraObjetos.writeObject(empleadoEncontrado);
				this.escrituraObjetos.flush();
				if(empleadoEncontrado != null) {
					Empleado emple = (Empleado)this.lecturaObjetos.readObject();
					Boolean result2 = this.empleadoDAO.actualizarEmpleado(emple);
					mensaje = (result2) ? "SE ACTUALIZO CON EXITO" : "NO SE ENCONTRO EL EMPLEADO CON CUYO DNI";
					this.escritura.writeUTF(mensaje);
					this.escritura.flush();
				}
				break;
			case 5:
				int dni2 = this.lectura.readInt();
				Boolean result3 = empleadoDAO.eliminarPorDni(dni2);
				mensaje = (result3) ? "SE ELIMINO CON EXITO": "NO SE ENCONTRO EL EMPLEADO CON CUYO DNI";
				this.escritura.writeUTF(mensaje);
				this.escritura.flush();
				break;
			case 6:
				mensaje="CERRANDO EL SERVIDOR...";
				System.out.println(mensaje);
				this.escritura.writeUTF(mensaje);
				this.escritura.flush();
			}
		}catch(ClassNotFoundException | IOException ex) {
			ex.printStackTrace();
		}


		return mensaje;

	}

	public Socket getCliente() {
		return cliente;
	}

	public void setCliente(Socket cliente) {
		this.cliente = cliente;
	}

}
