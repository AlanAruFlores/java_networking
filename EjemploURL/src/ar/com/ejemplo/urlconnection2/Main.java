package ar.com.ejemplo.urlconnection2;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class Main {

	public static void main(String[] args) {
		if(args.length < 2 || args.length>2) {
			System.out.println("LA SINTAXIS ES LA SIGUIENTE: <url> <archivo>");
			return;
		}
		
		String archivo  = args[1];
		try {
			URL url = new URI(args[0]).toURL();
			URLConnection urlConexion = url.openConnection();
			
			BufferedInputStream flujoLectura = new BufferedInputStream(urlConexion.getInputStream());
		
			//Dentro de la misma instancio un file output stream porque voy a copiarlo en un archivo
			BufferedOutputStream flujoEscritura = new BufferedOutputStream(new FileOutputStream(archivo));
			
			byte[] buffer = new byte[4000];
			int bytesLeidos = -1;//cantidad de bytes leidos
			
			while((bytesLeidos = flujoLectura.read(buffer)) != -1) {
				flujoEscritura.write(buffer,0,bytesLeidos);
			}
			
			flujoLectura.close();
			flujoEscritura.close();
		}catch(MalformedURLException  | URISyntaxException ex) {
			ex.printStackTrace();
		}catch(IOException ex) {
			ex.printStackTrace();
		}finally {
			System.out.println("Se copio el archivo correctamente");
		}
		
		
	}

}

