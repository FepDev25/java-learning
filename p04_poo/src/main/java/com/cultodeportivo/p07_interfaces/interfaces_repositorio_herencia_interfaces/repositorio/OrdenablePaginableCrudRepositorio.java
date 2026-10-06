package com.cultodeportivo.p07_interfaces.interfaces_repositorio_herencia_interfaces.repositorio;

// Interface que combina las funcionalidades de Ordenable, Paginable, CRUD y Contable
public interface OrdenablePaginableCrudRepositorio extends OrdenableRepositorio,
    PaginableRepositorio, CrudRepositorio, ContableRepositorio {
}
