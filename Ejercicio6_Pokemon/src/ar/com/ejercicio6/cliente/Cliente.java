package ar.com.ejercicio6.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ar.com.ejercicio6.dominio.Bulbasour;
import ar.com.ejercicio6.dominio.Charizard;
import ar.com.ejercicio6.dominio.Jugador;
import ar.com.ejercicio6.dominio.Pocion;
import ar.com.ejercicio6.dominio.PocionFuerza;
import ar.com.ejercicio6.dominio.PocionVida;
import ar.com.ejercicio6.dominio.Pokemon;
import ar.com.ejercicio6.dominio.Treecko;
import ar.com.ejercicio6.utils.Menu;
import ar.com.ejercicio6.utils.MenuInventario;

//Jugador
public class Cliente {
	private Jugador jugador;
	private Jugador enemigo;
	
	private Socket sck;
	private ObjectOutputStream escritura;
	private ObjectInputStream lectura;
	
	
	//DATOS DEL SERVIDOR
	private final String SERVIDOR = "localhost";
	private final Integer PUERTO = 3334;
	
	private Integer count = 1;
	private Boolean esTurno = false;

	//FLAG
	private Boolean flag=true;
	
	public Cliente() {}
	
	public void ejecutar() {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("INGRESE EL NOMBRE DEL JUGADOR: ");
			
			String nombreJugador = entrada.readLine();
			
			sck = new Socket(SERVIDOR, PUERTO);
			this.escritura = new ObjectOutputStream(sck.getOutputStream());
			this.lectura = new ObjectInputStream(sck.getInputStream());
			
			//El jugador elige su pokemon
			Pokemon pokemon = elegirPokemon(entrada);
			
			//Instanciamos un objeto de jugador
			this.jugador = new Jugador(nombreJugador, pokemon);

			
			//Establezco pociones
			this.jugador.setPocion(new PocionVida());
			this.jugador.setPocion(new PocionVida());
			this.jugador.setPocion(new PocionVida());

			
			this.jugador.setPocion(new PocionFuerza());

			empezarJuego(entrada);
			
			lectura.close();
			entrada.close();
			escritura.close();
			this.sck.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Empezamos el juego a partir de este metodo
	 * @param entrada
	 */
	private void empezarJuego(BufferedReader entrada) {
		//Servidor (enemigo)
		try {
			while(flag) {
				mostrarJugadores();
				if(count % 2 != 0) {

					Menu opcion = ingresarOpcion(entrada);
					switch(opcion) {
						case ATACAR:
							this.enemigo.getPokemon().setVida(
									this.enemigo.getPokemon().getVida() - this.jugador.getPokemon().getAtaque());
							
							if(this.enemigo.getPokemon().getVida() <= 0)
								flag = false;
							
							System.out.println("HAS ATACADO A "+this.enemigo.getNombreJugador()+""
									+ ", LE QUEDA "+this.enemigo.getPokemon().getVida()+" HP");
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
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void actualizarDatos() throws IOException, ClassNotFoundException {
		if(esTurno) {
			Jugador[] jugadoresActualizados = {
					this.jugador,
					this.enemigo
			};
			
			this.escritura.writeObject(jugadoresActualizados);
			this.escritura.flush();
		}
		else {
			Jugador[] actualizados = (Jugador[])this.lectura.readObject();
			this.jugador = actualizados[1];
			this.enemigo = actualizados[0];
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
					this.jugador.getPokemon().getVida() + ((PocionVida)pocion).getVida());
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
					aux = (i == 0) ? " X "+cantidadVida : ((i == 1) ? " X "+cantidadFuerza : ""); 
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
			
			if(p.getClass().getSimpleName().equalsIgnoreCase(clasePocion)) {
				count++;
			}
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
		if(count%2 != 0) {
			esTurno = true;
			System.out.println("TU TURNO....");
		
		}else {
			esTurno = false;
			System.out.println("ESPERANDO....");
		}
		
		if(count == 1) {
			//RECIBO INFORMACION DEL SERVIDOR
			this.enemigo  =(Jugador) this.lectura.readObject();
			//MANDO TAMBIEN NUESTRO JUGADOR
			this.escritura.writeObject(this.jugador);
			this.escritura.flush();
		}
		
		System.out.println("\n\n CONTRINCANTE: ");
		System.out.println(this.enemigo.toString());
		
		System.out.println("\n\n TU: ");
		System.out.println(	this.jugador.toString());
		
	
	}
	
	private Pokemon elegirPokemon(BufferedReader entrada) throws NumberFormatException, IOException {
		List<Pokemon> pokemonsLista= new ArrayList<>() {
			private static final long serialVersionUID=1L;
			{
				add(new Treecko());
				add(new Charizard());
				add(new Bulbasour());
		}};
		
		System.out.println("ELIGE UN POKEMON: ");
		Integer opcion = 0;
		do {
			for(int i = 0; i<pokemonsLista.size(); i++) {
				System.out.println((i+1)+") "+pokemonsLista.get(i).getNombre());
			}
			opcion = Integer.parseInt(entrada.readLine());
		}while(opcion < 1 || opcion >pokemonsLista.size());
			
		return pokemonsLista.get(opcion-1);
	}
	

	private void hacerEspacios(Integer n) {
		for(var i = 0 ; i<n ; i++)
			System.out.println();
	}
	
	public Socket getSocket() {
		return this.sck;
	}
	
	public ObjectOutputStream getEscritura() {
		return this.escritura;
	}
	
	public ObjectInputStream getLectura() {
		return this.lectura;
	}
	
}
