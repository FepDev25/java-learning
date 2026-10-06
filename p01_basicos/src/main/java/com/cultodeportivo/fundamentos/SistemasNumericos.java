package com.cultodeportivo.fundamentos;

import java.util.Random;

public class SistemasNumericos {

    public static void main(String[] args) {
        Random random = new Random();
        int randomNumber = random.nextInt(100);

        System.out.println("Numero decimal: " + randomNumber);

        String resultadoBinario = Integer.toBinaryString(randomNumber);
        String resultadoOctal = Integer.toOctalString(randomNumber);
        String resultadoHexadecimal = Integer.toHexString(randomNumber);

        System.out.println("Transformacion binario: " + resultadoBinario);
        System.out.println("Transformacion octal: " + resultadoOctal);
        System.out.println("Transformacion hexadecimal: " + resultadoHexadecimal);

        int numeroBinario = 0b11110;
        System.out.println("Numero binario: " + numeroBinario);

        int numeroOctal = 036;
        System.out.println("Numero octal: " + numeroOctal);

        int numeroHexadecimal = 0x1e;
        System.out.println("Numero hexadecimal: " + numeroHexadecimal);
    }
}
