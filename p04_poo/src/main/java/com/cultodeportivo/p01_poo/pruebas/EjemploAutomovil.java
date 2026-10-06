package com.cultodeportivo.p01_poo.pruebas;

import com.cultodeportivo.p01_poo.Automovil;
import com.cultodeportivo.p01_poo.Color;
import com.cultodeportivo.p01_poo.Estanque;
import com.cultodeportivo.p01_poo.Motor;
import com.cultodeportivo.p01_poo.TipoMotor;

public class EjemploAutomovil {
    public static void main(String[] args) {

        // Crear instancias de Automovil con diferentes constructores y atributos
        Motor motorSubaru = new Motor(2.0, TipoMotor.GASOLINA);
        Automovil subaru = new Automovil("Subaru", "Impreza");
        subaru.setMotor(motorSubaru);
        subaru.setEstanque(new Estanque());
        subaru.setColor(Color.BLANCO);
        subaru.setFabricante("Subaru Fab");
        System.out.println("Subaru: " + subaru);

        Motor motorMazda = new Motor(3.0, TipoMotor.DIESEL);
        Automovil mazda = new Automovil("Mazda", "BT-50", Color.ROJO, motorMazda);
        mazda.setEstanque(new Estanque(45));
        System.out.println("Mazda: " + mazda);

        Automovil nissan = new Automovil("Nissan", "Navara", Color.GRIS,
                new Motor(4.0, TipoMotor.DIESEL), new Estanque(50));
        System.out.println("Nissan: " + nissan);

        Automovil nissan2 = new Automovil("Nissan", "Navara", Color.GRIS,
                new Motor(3.5, TipoMotor.GASOLINA), new Estanque(50));
        System.out.println("Nissan 2: " + nissan2);

        Automovil auto = new Automovil();
        System.out.println("Auto: " + auto);

        // Comparar objetos y mostrar detalles
        System.out.println("nissan == nissan2: " + (nissan == nissan2));
        System.out.println("nissan.equals(nissan2): " + (nissan.equals(nissan2)));
        System.out.println();

        // Mostrar detalles de cada automovil
        System.out.println("Detalle subaru: " + subaru.verDetalle() + "\n");
        System.out.println("Detalle mazda: " + mazda.verDetalle() + "\n");
        System.out.println("Detalle nissan: " + nissan.verDetalle() + "\n");

        // Usar métodos de la clase Automovil
        System.out.println(subaru.acelerar(3000));
        System.out.println(subaru.frenar());
        System.out.println(mazda.acelerarFrenar(4000));

        // Calcular consumo de combustible
        System.out.println("Kilómetros por litro: " + subaru.calcularConsumo(300, 0.6f));
        System.out.println("Kilómetros por litro: " + subaru.calcularConsumo(300, 60));
        System.out.println("Kilómetros por litro: " + nissan.calcularConsumo(300, 60));
    }
}
