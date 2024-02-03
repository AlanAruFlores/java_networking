package ar.com.ejemplo.urlconnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class Main {

	public static void main(String[] args) {
		try {
			URL url  = new URI("https://www.javatpoint.com/URLConnection-class").toURL();
			URLConnection conexion = url.openConnection();
			
			//obtengo el flujo de lectura de la url para leer sus datos
			InputStream flujoLectura = conexion.getInputStream();
		
			int i = 0 ;
			while((i = flujoLectura.read()) != -1) {
				System.out.print((char)i);
			}
			
			flujoLectura.close();
			
		}catch(MalformedURLException | URISyntaxException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
