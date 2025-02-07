package ar.com.gestor.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import ar.com.gestor.dao.IDAOManager;
import ar.com.gestor.dao.IEmpleadoDAO;


public class DAOManagerImpl implements IDAOManager{
	
	private Connection conn;
	
	private final String URL_CONNECTION = "jdbc:mysql://127.0.0.1:3306/gestor_empleados";
	private final String HOST = "root";
	private final String PASSWORD= "";
	
	//DAO
	private IEmpleadoDAO daoEmpleado;
	
	public DAOManagerImpl() throws SQLException {
		this.conn = DriverManager.getConnection(URL_CONNECTION, obtenerPropiedades());
	}
	
	private Properties obtenerPropiedades() {
		Properties prop = new Properties();
		prop.setProperty("user",HOST);
		prop.setProperty("password", PASSWORD);
		
		return prop;
	}
	
	public IEmpleadoDAO getEmpleadoDAO() {
		if(this.daoEmpleado == null) 
			this.daoEmpleado = new EmpleadoDAOImpl(this.conn);
		return this.daoEmpleado;
	}
	
	public Connection getConexion() {
		return this.conn;
	}
	
	public void cerrarConexion() {
		try {
			if(verificarSiEstaConectado()) {
				this.conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Boolean verificarSiEstaConectado() throws SQLException {
		return !this.conn.isClosed();
	}
}
