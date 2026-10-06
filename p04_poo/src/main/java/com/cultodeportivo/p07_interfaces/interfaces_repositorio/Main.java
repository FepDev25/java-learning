package com.cultodeportivo.p07_interfaces.interfaces_repositorio;

import java.util.List;

import com.cultodeportivo.p07_interfaces.interfaces_repositorio.modelo.Cliente;
import com.cultodeportivo.p07_interfaces.interfaces_repositorio.repositorio.ClienteListRepositorio;
import com.cultodeportivo.p07_interfaces.interfaces_repositorio.repositorio.CrudRepositorio;
import com.cultodeportivo.p07_interfaces.interfaces_repositorio.repositorio.Direccion;
import com.cultodeportivo.p07_interfaces.interfaces_repositorio.repositorio.OrdenableRepositorio;
import com.cultodeportivo.p07_interfaces.interfaces_repositorio.repositorio.PaginableRepositorio;

public class Main {
    public static void main(String[] args) {

        CrudRepositorio repo = new ClienteListRepositorio();
        repo.crear(new Cliente("Jano", "Pérez"));
        repo.crear(new Cliente("Bea", "González"));
        repo.crear(new Cliente("Luci", "Martínez"));
        repo.crear(new Cliente("Felipe", "Peralta"));

        List<Cliente> clientes = repo.listar();
        clientes.forEach(System.out::println);
        System.out.println("===== paginable desde 1 hasta 4 =====");
        List<Cliente> paginable = ((PaginableRepositorio)repo).listar(1, 4);
        paginable.forEach(System.out::println);

        System.out.println("===== ordenar por apellido =====");
        List<Cliente> clientesOrdenAsc = ((OrdenableRepositorio)repo).listar("apellido", Direccion.ASC);
        for(Cliente c: clientesOrdenAsc){
            System.out.println(c);
        }

        System.out.println("===== editar =====");
        Cliente beaActualizar = new Cliente("Bea", "Mena");
        beaActualizar.setId(2);
        repo.editar(beaActualizar);
        Cliente bea = repo.porId(2);
        System.out.println(bea);
        System.out.println(" ============= ");
        ((OrdenableRepositorio)repo).listar("nombre", Direccion.ASC).forEach(System.out::println);
        System.out.println("===== eliminar ======");
        //repo.eliminar(2);
        repo.listar().forEach(System.out::println);
    }
}
