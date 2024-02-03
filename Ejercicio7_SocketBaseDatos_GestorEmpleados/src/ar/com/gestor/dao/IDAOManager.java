package ar.com.gestor.dao;

import java.sql.Connection;

public interface IDAOManager {
	public IEmpleadoDAO getEmpleadoDAO();
	public Connection getConexion();
	public void cerrarConexion();
}
