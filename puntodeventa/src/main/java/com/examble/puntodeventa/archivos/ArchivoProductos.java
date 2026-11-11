package com.examble.puntodeventa.archivos;

import com.examble.puntodeventa.modelos.Productos;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoProductos {
    private static final String RUTA = "productos.txt";

    public static List<Productos> cargarProductos() {
        List<Productos> lista = new ArrayList<>();
        File file = new File(RUTA);

        if (!file.exists()) {
            crearDatosEjemplo();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Productos p = Productos.desdeLineaTxt(linea);
                    if (p != null) {
                        lista.add(p);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarProductos(List<Productos> lista) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(RUTA)))) {
            for (Productos p : lista) {
                out.println(p.aLineaTxt());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar productos: " + e.getMessage());
        }
    }

    private static void crearDatosEjemplo() {
        List<Productos> ejemplo = new ArrayList<>();
        ejemplo.add(new Productos("P001", "Leche Alquería 1L", 4500, 20));
        ejemplo.add(new Productos("P002", "Pan Tajado Bimbo", 6000, 15));
        ejemplo.add(new Productos("P003", "Café Sello Rojo 250g", 8500, 10));
        ejemplo.add(new Productos("P004", "Arroz Roa 1Kg", 4200, 30));
        ejemplo.add(new Productos("P005", "Gaseosa Coca-Cola 1.5L", 5500, 25));
        guardarProductos(ejemplo);
    }
}