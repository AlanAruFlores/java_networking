package ar.com.ejemplo.url;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {
	public static void main(String[] args) {
		try {
			URL direccionURL = new URI("https://github.com/AlanAruFlores/java_multithreading-").toURL();
			System.out.println("Protocolo: "+direccionURL.getProtocol());
			System.out.println("Host Name: "+direccionURL.getHost());
			System.out.println("Puerto: "+direccionURL.getPort());
			System.out.println("Puerto x defecto: "+direccionURL.getDefaultPort());
			System.out.println("File: "+direccionURL.getFile());
			System.out.println("Query: "+direccionURL.getQuery());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}
	
}
