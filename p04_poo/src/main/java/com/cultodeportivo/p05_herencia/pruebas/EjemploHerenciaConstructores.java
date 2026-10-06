package com.cultodeportivo.p05_herencia.pruebas;

import com.cultodeportivo.p05_herencia.Alumno;
import com.cultodeportivo.p05_herencia.AlumnoInternacional;
import com.cultodeportivo.p05_herencia.Persona;
import com.cultodeportivo.p05_herencia.Profesor;

public class EjemploHerenciaConstructores {

    public static void main(String[] args) {

        System.out.println("======== Creando la instancia de la clase Alumno ========");
        Alumno alumno = new Alumno("Felipe", "Peralta", 20, "UPS");
        alumno.setNotaCastellano(5.5);
        alumno.setNotaHistoria(6.3);
        alumno.setNotaMatematica(4.9);
        alumno.setEmail("Felipe@correo.com");

        System.out.println("======= Creando la instancia de la clase AlumnoInternacional ======= ");
        AlumnoInternacional alumnoInt = new AlumnoInternacional("Peter", "Gosling", "Australia");
        alumnoInt.setEdad(15);
        alumnoInt.setInstitucion("UPS");
        alumnoInt.setNotaIdiomas(6.8);
        alumnoInt.setNotaCastellano(6.2);
        alumnoInt.setNotaHistoria(5.8);
        alumnoInt.setNotaMatematica(6.5);
        alumnoInt.setEmail("peter@correo.com");

        System.out.println("======= Creando la instancia de la clase de Profesor =======");
        Profesor profesor = new Profesor("Luci", "Pérez", "Matemáticas");
        profesor.setEdad(37);
        profesor.setEmail("profesor.luci@colegio.com");

        System.out.println(" ======== - =======");

        imprimir(alumno);
        imprimir(alumnoInt);
        imprimir(profesor);
    }

    public static void imprimir(Persona persona) {
        System.out.println("Imprimiendo datos en común del tipo Persona:");
        System.out.println("nombre: " + persona.getNombre()
                + ", apellido: " + persona.getApellido()
                + ", edad: " + persona.getEdad()
                + ", email: " + persona.getEmail());

        if (persona instanceof Alumno alumno) {
            System.out.println("Imprimiendo los datos del tipo Alumno:");
            System.out.println("Institución: " + alumno.getInstitucion());
            System.out.println("Nota matemáticas: " + alumno.getNotaMatematica());
            System.out.println("Nota Historia: " + alumno.getNotaHistoria());
            System.out.println("Nota Castellano: " + alumno.getNotaCastellano());

            if (persona instanceof AlumnoInternacional alumnoInternacional) {
                System.out.println("Imprimiendo los datos del tipo AlumnoInternacional:");
                System.out.println("Nota idiomas: " + alumnoInternacional.getNotaIdiomas());
                System.out.println("Pais: " + alumnoInternacional.getPais());
            }
            System.out.println("============== sobre escritura promedio ===============");
            System.out.println("Promedio: " + alumno.calcularPromedio());
            System.out.println("============== sobre escritura promedio ===============");
        }

        if (persona instanceof Profesor profesor) {
            System.out.println("Imprimiendo los datos del tipo Profesor:");
            System.out.println("Asignatura: " + profesor.getAsignatura());
        }

        System.out.println("============== sobre escritura saludar ===============");
        System.out.println(persona.saludar());
        System.out.println("=============================");
    }
}
