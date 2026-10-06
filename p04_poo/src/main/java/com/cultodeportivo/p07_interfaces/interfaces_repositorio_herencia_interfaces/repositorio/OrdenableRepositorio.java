package com.cultodeportivo.p07_interfaces.interfaces_repositorio_herencia_interfaces.repositorio;

import java.util.List;

import com.cultodeportivo.p07_interfaces.interfaces_repositorio_herencia_interfaces.modelo.Cliente;

public interface OrdenableRepositorio {
    List<Cliente> listar(String campo, Direccion dir);
}
