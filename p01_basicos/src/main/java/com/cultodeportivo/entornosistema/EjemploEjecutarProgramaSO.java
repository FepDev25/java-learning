package com.cultodeportivo.entornosistema;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class EjemploEjecutarProgramaSO {

    public static void main(String[] args) {
        Process proceso;

        try {
            ProcessBuilder pb = new ProcessBuilder("ls", "-l");
            pb.directory(new File(System.getProperty("user.dir")));
            proceso = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = proceso.waitFor();
            System.out.println("\nProcess exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
