package ar.com.gestor.cliente.hilo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ar.com.gestor.cliente.Cliente;
import ar.com.gestor.model.Empleado;

public class HiloCliente implements Runnable {

	private Socket sck;
	private Cliente cliente;

	private DataInputStream lectura;
	private ObjectInputStream lecturaObjetos;
	private ObjectOutputStream escrituraObjetos;
	private DataOutputStream escritura;

	public HiloCliente(Cliente cliente, Socket sck) throws IOException {
		this.setSck(sck);
		this.cliente = cliente;
		this.lectura = new DataInputStream(sck.getInputStream());
		this.escrituraObjetos = new ObjectOutputStream(sck.getOutputStream());
		this.lecturaObjetos =  new ObjectInputStream(sck.getInputStream());
		this.escritura = new DataOutputStream(sck.getOutputStream());
	}

	@Override
	public void run() {
		int opcion = -1;
		BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));

		try {
			while (opcion != 6) {
				hacerEspacios(8);
				opcion = ingresarOpcion(entrada);
				//MANDO LA OPCION AL SERVIDOR.
				this.escritura.writeInt(opcion);
				this.escritura.flush();
				realizarPeticion(opcion, entrada);
			}
			lectura.close();
			lecturaObjetos.close();
			escritura.close();
			escrituraObjetos.close();
			cliente.flag = false;
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void realizarPeticion(int opcion, BufferedReader entrada) throws NumberFormatException, IOException, ClassNotFoundException {
		
		switch(opcion) {
			case 1:
				Empleado e = registrarEmpleado(entrada);
				this.escrituraObjetos.writeObject(e);
				this.escrituraObjetos.flush();
				String mensaje = this.lectura.readUTF();
				System.out.println(mensaje);
				break;
			case 2:
				Integer dni = ingresarDni(entrada);
				this.escritura.writeInt(dni);
				this.escritura.flush();
				System.out.println("EMPLEADO ENCONTRADO POR DNI: ");
				String mensaje2 = this.lectura.readUTF();
				System.out.println(mensaje2);
				break;
			case 3:
				System.out.println("EMPLEADOS REGISTRADOS: ");
				String mensaje3 = this.lectura.readUTF();
				System.out.println(mensaje3);
				break;
			case 4:
				Integer dni2 =ingresarDni(entrada);
				this.escritura.writeInt(dni2);
				this.escritura.flush();
				Empleado e2 = (Empleado)this.lecturaObjetos.readObject();
				if(e2 != null) {
					System.out.println(e2.toString());
					actualizarEmpleado(e2,entrada);
					this.escrituraObjetos.writeObject(e2);
					this.escrituraObjetos.flush();
					String mensaje4 = this.lectura.readUTF();
					System.out.println(mensaje4);
				}else {
					System.out.println("NO SE ENCONTRO EL EMPLEADO A MODIFICAR");
				}
				break;
			case 5:
				System.out.println("INGRESE EL SIGUIENTE DATO PARA ELIMINAR UN EMPLEADO");
				Integer dni3 = ingresarDni(entrada);
				this.escritura.writeInt(dni3);
				this.escritura.flush();
				String mensaje5 = this.lectura.readUTF();
				System.out.println(mensaje5);
				break;
			case 6:
				String mensaje6 = this.lectura.readUTF();
				System.out.println(mensaje6);
		}
		
	}
	//Metodos de cada case
	private Empleado registrarEmpleado(BufferedReader entrada) throws NumberFormatException, IOException {
		System.out.println("INGRESE SU DNI:");
		Integer dni  =Integer.parseInt(entrada.readLine());
		
		System.out.println("INGRESE SU NOMBRE: ");
		String nombre = entrada.readLine();
		
		System.out.println("INGRESE SU APELLIDO: ");
		String apellido  = entrada.readLine();
		
		System.out.println("INGRESE LA EDAD: ");
		Integer edad = Integer.parseInt(entrada.readLine());
		
		System.out.println("INGRESE SU SUELDO: ");
		Float sueldo = Float.parseFloat(entrada.readLine());
		
		
		return new Empleado(dni,nombre,apellido,edad,sueldo);
	}
	
	private Integer ingresarDni(BufferedReader entrada) throws NumberFormatException, IOException {
		System.out.println("INGRESE EL DNI: ");
		int dni = Integer.parseInt(entrada.readLine());
		return dni;
	}
	
	private void actualizarEmpleado(Empleado emp, BufferedReader entrada) throws NumberFormatException, IOException {
		int opcion =  -1;
		
		while(opcion !=5) {
			
			do {
				System.out.println("INGRESE UNA OPCION A ACTUALIZAR:"
						+ "\n[1]NOMBRE"
						+ "\n[2]APELLIDO"
						+ "\n[3]EDAD"
						+ "\n[4]SUELDO"
						+ "\n[5]ACTUALIZAR");
				opcion = Integer.parseInt(entrada.readLine());
				
				if(opcion < 1 || opcion >5)
					System.err.println("ERROR!!, OPCION NO VALIDA!!");
			}while(opcion < 1 || opcion >5);
			
			
			switch(opcion) {
				case 1:
					System.out.println("INGRESE EL NUEVO NOMBRE");
					String nombre = entrada.readLine();
					emp.setNombre(nombre);
					System.out.println("NOMBRE ACTUALIZADO");
					break;
				case 2:
					System.out.println("INGRESE EL NUEVO APELLIDO");
					String apellido = entrada.readLine();
					emp.setApellido(apellido);
					System.out.println("APELLIDO ACTUALIZADO");
					break;
				case 3:
					System.out.println("INGRESE LA NUEVA EDAD");
					Integer edad = Integer.parseInt(entrada.readLine());
					emp.setEdad(edad);
					System.out.println("EDAD ACTUALIZADA");
					break;
				case 4:
					System.out.println("INGRESE EL NUEVO SUELDO");
					Float sueldo = Float.parseFloat(entrada.readLine());
					emp.setSueldo(sueldo);
					System.out.println("SUELDO ACTUALIZADO");
					break;
				case 5:
					System.out.println("ACTUALIZANDO..");
					
			}

		}
	}
	
	
	//-------
	private int ingresarOpcion(BufferedReader entrada) throws NumberFormatException, IOException {
		int opcion = -1;

		do {
			System.out.println("INGRESE UNA OPCION" + "\n[1]REGISTRAR EMPLEADO" + "\n[2]OBTENER EMPLEADO POR DNI"
					+ "\n[3]OBTENER TODOS LOS EMPLEADOS" + "\n[4]ACTUALIZAR UN EMPLEADO"
					+ "\n[5]ELIMINAR EMPLEADO POR DNI" + "\n[6]SALIR");
			opcion = Integer.parseInt(entrada.readLine());

			if (opcion < 1 || opcion > 6)
				System.err.println("ERROR, OPCION NO VALIDA");
		
			hacerEspacios(8);
		} while (opcion < 1 || opcion > 6);
		
		
		return opcion;
	}
	
	private void hacerEspacios(int n) {
		for(int i = 0 ; i<n; i++) {
			System.out.println();
		}
	}

	public Socket getSck() {
		return sck;
	}

	public void setSck(Socket sck) {
		this.sck = sck;
	}

}
