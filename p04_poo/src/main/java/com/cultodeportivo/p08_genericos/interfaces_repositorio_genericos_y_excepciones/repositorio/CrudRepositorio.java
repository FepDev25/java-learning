package com.cultodeportivo.p08_genericos.interfaces_repositorio_genericos_y_excepciones.repositorio;

import java.util.List;

import com.cultodeportivo.p08_genericos.interfaces_repositorio_genericos_y_excepciones.repositorio.excepciones.AccesoDatoException;

public interface CrudRepositorio<T> {
    List<T> listar();
    T porId(Integer id) throws AccesoDatoException;
    void crear(T t) throws AccesoDatoException;
    void editar(T t) throws AccesoDatoException;
    void eliminar(Integer id) throws AccesoDatoException;
}
