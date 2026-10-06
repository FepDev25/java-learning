package com.cultodeportivo.p07_interfaces.interfaces_repositorio.repositorio;

import java.util.List;

import com.cultodeportivo.p07_interfaces.interfaces_repositorio.modelo.Cliente;

// Interface para repositorios que permiten ordenar los resultados
public interface OrdenableRepositorio {
    List<Cliente> listar(String campo, Direccion dir);
}
