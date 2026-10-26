package dao;

import java.util.List;

public interface IDao<T> {
    
    // Guarda el registro y devuelve el objeto con su ID autogenerado
    T guardar(T t);
    
    // Busca un registro por su ID
    T buscar(Integer id);
    
    // Actualiza un registro existente (este es el método que faltaba)
    T actualizar(T t);
    
    // Elimina un registro por su ID
    void eliminar(Integer id);
    
    // Devuelve una lista con todos los registros
    List<T> buscarTodos();
    
}