package ar.com.gestor.dao;

import java.util.Set;

public interface IDAO <T,E>{
	public Boolean insertar(T obj);
	public T seleccionPorDni(E dni);
	public Set<T> seleccinarTodo();
	public Boolean actualizarEmpleado(T obj);
	public Boolean eliminarPorDni(E dni);	
}
