package com.cultodeportivo.repositorio;

import java.util.Optional;

public interface Repositorio<T> {
    Optional<T> buscarPorNombre(String nombre);
}
