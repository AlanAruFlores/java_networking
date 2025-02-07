package ar.com.ejercicio6.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ar.com.ejercicio6.dominio.Charizard;
import ar.com.ejercicio6.dominio.Jugador;
import ar.com.ejercicio6.dominio.Pocion;
import ar.com.ejercicio6.dominio.PocionFuerza;
import ar.com.ejercicio6.dominio.PocionVida;
import ar.com.ejercicio6.utils.Menu;
import ar.com.ejercicio6.utils.MenuInventario;

//Contrincante
public final class Servidor {
	private Jugador jugador;
	private Jugador cliente;
	
	private ServerSocket servidor;
	private ObjectInputStream lectura;
	private ObjectOutputStream escritura;

	//INFORMACION DEL SERVIDOR
	private final Integer PUERTO = 3334;
	
	//Socket del cliente
	private Socket sckCliente;
	
	private Integer count = 1;
	private Boolean esTurno = false;
	//FLAG
	private Boolean flag = true;
	public Servidor() {	
	}
	
	public void ejecutar() {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
			
			this.servidor = new ServerSocket(PUERTO);
			//Inicializao el jugador servidor
			this.jugador  = new Jugador("ElServer", new Charizard());
			
			//Espero a que el cliente se conecte
			this.sckCliente = servidor.accept();
			
			this.lectura  = new ObjectInputStream(sckCliente.getInputStream());
			this.escritura = new ObjectOutputStream(sckCliente.getOutputStream());
			
			//Establezco pociones
			this.jugador.setPocion(new PocionVida());
			this.jugador.setPocion(new PocionVida());

			
			this.jugador.setPocion(new PocionFuerza());
			
			empezarJuego(entrada);
			
			lectura.close();
			escritura.close();
			servidor.close();
		
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void empezarJuego(BufferedReader entrada) {
		//Cliente (enemigo)
		try {
			while(flag) {
				mostrarJugadores();
				if(count % 2 == 0) {
					Menu opcion = ingresarOpcion(entrada);
					switch(opcion) {
						case ATACAR:
							this.cliente.getPokemon().setVida(
									this.cliente.getPokemon().getVida() - this.jugador.getPokemon().getAtaque());
							if(this.cliente.getPokemon().getVida() <= 0)
								flag = false;
							
							System.out.println("HAS ATACADO A "+this.cliente.getNombreJugador()+""
									+ ", LE QUEDA "+this.cliente.getPokemon().getVida()+" HP");
							break;
						case VER_INVENTARIO:
							verInventario(entrada);
					}
				}
				actualizarDatos();
				count++;
			}

		}catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		}catch(IOException ex) {
			ex.printStackTrace();
		}

	}
	

	private void actualizarDatos() throws IOException, ClassNotFoundException {
		if(esTurno) {
			Jugador[] jugadoresActualizados = {
					this.jugador,
					this.cliente
			};
			
			this.escritura.writeObject(jugadoresActualizados);
			this.escritura.flush();
		}
		else {
			Jugador[] actualizados = (Jugador[])this.lectura.readObject();
			this.jugador = actualizados[1];
			this.cliente = actualizados[0];
			System.out.println(this.cliente);

		}
	}
	
	private void verInventario(BufferedReader entrada) {
		List<Pocion> pociones = this.jugador.getInventario();
		MenuInventario opcionSeleccionada = null;
		if(pociones.size() == 0) {
			System.out.println("INVENTARIO VACIO");
			return;
		}
		
		ruptura: while(opcionSeleccionada != opcionSeleccionada.SALIR) {
			int cantidadPocionesVida = getCantidadPociones("PocionVida");
			int cantidadPocionesFuerza = getCantidadPociones("PocionFuerza");
			
			opcionSeleccionada = seleccionarOpcionDelInventario(cantidadPocionesVida, cantidadPocionesFuerza, entrada);
			
			List<Pocion> pocionesVida = new ArrayList<Pocion>(){
				{
					addAll(getPocionesTipo(pociones, PocionVida.class));
				}
			};
			List<Pocion> pocionFuerza = new ArrayList<Pocion>(){
				{
					addAll(getPocionesTipo(pociones, PocionFuerza.class));
				}
			};
			
			ruptura_switch:switch(opcionSeleccionada) {
				case POCION_VIDA:
					if(pocionesVida.size()!= 0) {
						PocionVida aux = (PocionVida)pocionesVida.get(0);
						usarPocion(aux);
						pocionesVida.remove(aux);
					}
					break ruptura_switch;
				case POCION_DAÑO:
					if(pocionFuerza.size()!= 0) {
						PocionFuerza aux = (PocionFuerza)pocionFuerza.get(0);
						usarPocion(aux);
						pocionFuerza.remove(aux);
					}
					break ruptura_switch;
				case SALIR:
					JOptionPane.showMessageDialog(null, "TERMINO SU TURNO");
					break ruptura;
			}
			List<Pocion>pocionesActuales = new ArrayList<Pocion>(){
				{
					addAll(pocionesVida);
					addAll(pocionFuerza);
				}
			};
			this.jugador.setInventario(pocionesActuales);
			
			
		}
	}
	private void usarPocion(Pocion pocion) {
		Boolean p  = pocion instanceof PocionVida;
		
		if(p) {
			this.jugador.getPokemon().setVida(
					this.jugador.getPokemon().getVida()+ ((PocionVida)pocion).getVida());
			JOptionPane.showMessageDialog(null, "RECUPERO VIDA TU POKEMON");
		}
		else {
			this.jugador.getPokemon().setAtaque(
				this.jugador.getPokemon().getAtaque() + ((PocionFuerza)pocion).getDanio());
			JOptionPane.showMessageDialog(null, "AUMENTASTE FUERZA A TU POKEMON");
		}
		
	}
	//Generico
	private <T> List<T> getPocionesTipo (List<Pocion> pociones, Class<T> tipo) {
		List<T> pocionesTipo = new ArrayList<T>();
		
		for(Pocion p : pociones) {
			if(tipo.isInstance(p)) {
				pocionesTipo.add((T)p);
			}
		}
		
		return pocionesTipo;
	}
	
	private MenuInventario seleccionarOpcionDelInventario(int cantidadVida, int cantidadFuerza, BufferedReader entrada) {
		MenuInventario menuInventario[] = MenuInventario.values();
		int opcion = -1;
		String aux = "";
		try {
			do {
				for(int i = 0 ; i<menuInventario.length; i++) {
					aux = (i == 0) ? " X "+cantidadVida: ((i == 1) ? " X "+cantidadFuerza : ""); 
					System.out.println("["+(i+1)+"]"+menuInventario[i].name()+""+aux);
				}
				opcion = Integer.parseInt(entrada.readLine());
				
				if((opcion == 1 && cantidadVida <= 0) || (opcion == 2 && cantidadFuerza <=0)) {
					JOptionPane.showMessageDialog(null, "ERROR");
					opcion = 3;
					break;
				}
			}while(opcion < 1 || opcion > menuInventario.length);
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
		return menuInventario[opcion-1];
	}
	
	private int getCantidadPociones(String clasePocion){
		
		List<Pocion> pociones = this.jugador.getInventario();
		
		int count = 0;
		for(Pocion p : pociones) {
			if(p.getClass().getSimpleName().equalsIgnoreCase(clasePocion))
				count++;
		}
		
		return count;
	}
	
	private Menu ingresarOpcion(BufferedReader entrada) throws NumberFormatException, IOException, ClassNotFoundException {
		Menu[] opciones = Menu.values();
		int opcion = -1;
		
		while(opcion < 1 || opcion >2) {
			for(int i = 0 ; i<opciones.length; i++) {
				System.out.println("["+(i+1)+"]"+opciones[i].name());
			}
			opcion = Integer.parseInt(entrada.readLine());
			if(opcion < 1 || opcion >2)
				System.err.println("Error!!");
		}
		

		return opciones[opcion-1];
	}
	
	
	
	private void mostrarJugadores() throws ClassNotFoundException, IOException {
		hacerEspacios(8);
		if(count%2==0) {
			esTurno = true;
			System.out.println("TU TURNO....");
		}
		else {
			esTurno = false;
			System.out.println("ESPERANDO....");
		}
		
		//SUCEDE CUANDO COMIENZA LA RONDA
		if(count == 1) {
			//ENVIO INFORMACION DE JUGADOR DEL SERVIDOR
			this.escritura.writeObject(this.jugador);
			this.escritura.flush();
			//RECIBO LA INFORMACION DEL CLIENTE
			cliente = (Jugador) this.lectura.readObject();
		}
		System.out.println("\n\n CONTRINCANTE: ");
		System.out.println(cliente.toString());
		
		System.out.println("\n\n TU: ");
		System.out.println(	this.jugador.toString());
	
	}
	

	private void hacerEspacios(Integer n) {
		for(var i = 0 ; i<n ; i++)
			System.out.println();
	}
	public Jugador getJugador() {
		return jugador;
	}

	public ObjectInputStream getLectura() {
		return lectura;
	}

	public ObjectOutputStream getEscritura() {
		return escritura;
	}

}
