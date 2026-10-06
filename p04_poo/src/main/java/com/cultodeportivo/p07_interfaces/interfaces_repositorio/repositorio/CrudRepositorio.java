package com.cultodeportivo.p07_interfaces.interfaces_repositorio.repositorio;


import java.util.List;

import com.cultodeportivo.p07_interfaces.interfaces_repositorio.modelo.Cliente;

// CRUD: Create, Read, Update, Delete
public interface CrudRepositorio {
    List<Cliente> listar();
    Cliente porId(Integer id);
    void crear(Cliente cliente);
    void editar(Cliente cliente);
    void eliminar(Integer id);
}
