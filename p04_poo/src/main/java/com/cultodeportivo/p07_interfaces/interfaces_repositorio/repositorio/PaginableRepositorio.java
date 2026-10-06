package com.cultodeportivo.p07_interfaces.interfaces_repositorio.repositorio;

import java.util.List;

import com.cultodeportivo.p07_interfaces.interfaces_repositorio.modelo.Cliente;

// Interface para repositorios que permiten paginar los resultados
public interface PaginableRepositorio {
    List<Cliente> listar(int desde, int hasta);
}
