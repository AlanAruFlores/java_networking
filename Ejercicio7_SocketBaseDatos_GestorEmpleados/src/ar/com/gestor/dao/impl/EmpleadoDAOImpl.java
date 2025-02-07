package ar.com.gestor.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import ar.com.gestor.dao.IEmpleadoDAO;
import ar.com.gestor.model.Empleado;

public class EmpleadoDAOImpl implements IEmpleadoDAO{
	
	private Connection conn;

	//QUERY
	private final String INSERT = "insert into empleados (dni,nombre,apellido,edad,sueldo) values (?,?,?,?,?)";
	private final String SELECT_DNI = "select dni,nombre,apellido,edad,sueldo from empleados where dni = ?";
	private final String SELECT = "select * from empleados";
	private final String UPDATE = "update empleados set nombre = ? , apellido = ? , edad=?, sueldo = ? where dni = ?";
	private final String DELETE = "delete from empleados where dni = ?";
	
	public EmpleadoDAOImpl(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Boolean insertar(Empleado obj) {
		try(PreparedStatement ps = this.conn.prepareStatement(INSERT)){
			ps.setInt(1,obj.getDni());
			ps.setString(2, obj.getNombre());
			ps.setString(3, obj.getApellido());
			ps.setInt(4, obj.getEdad());
			ps.setFloat(5, obj.getSueldo());
			
			var rows = ps.executeUpdate();
			if(rows == 0)
				return false;
			
			ps.close();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return true;
	}

	@Override
	public Empleado seleccionPorDni(Integer dni) {	
		Empleado emp = null;
		try(PreparedStatement ps = this.conn.prepareStatement(SELECT_DNI)){
			ps.setInt(1, dni);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				emp =  new Empleado(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getFloat(5));
		
			rs.close();
			ps.close();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return emp;
	}

	@Override
	public Set<Empleado> seleccinarTodo() {
		Set<Empleado> empleados  = new HashSet<Empleado>();
		try(PreparedStatement ps = this.conn.prepareStatement(SELECT)){
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				empleados.add(new Empleado(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getFloat(5)));
			}
			
			rs.close();
			ps.close();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return empleados;
	}

	@Override
	public Boolean actualizarEmpleado(Empleado obj) {
		
		try(PreparedStatement ps = this.conn.prepareStatement(UPDATE)){
			ps.setString(1, obj.getNombre());
			ps.setString(2, obj.getApellido());
			ps.setInt(3, obj.getEdad());
			ps.setFloat(4, obj.getSueldo());
			ps.setInt(5,obj.getDni());

			var rows = ps.executeUpdate();
			
			if(rows == 0)
				return false;
			
			ps.close();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return true;
	}

	@Override
	public Boolean eliminarPorDni(Integer dni) {

		try(PreparedStatement ps = this.conn.prepareStatement(DELETE)){
			ps.setInt(1, dni);
			
			var rows = ps.executeUpdate();
			if(rows == 0)
				return false;
			
			ps.close();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return true;
	}

}
