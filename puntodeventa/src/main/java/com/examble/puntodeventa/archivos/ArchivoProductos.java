package com.examble.puntodeventa.archivos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.examble.puntodeventa.modelos.Productos;

public class ArchivoProductos {
    private static final String RUTA = "productos.txt";

    public static List<Productos> cargarProductos() {
        List<Productos> lista = new ArrayList<>();
        File file = new File(RUTA);

        //Si el archivo no existe, crea datos de ejemplo automáticamente (Requisito)
        if (!file.exists()) {
            crearDatosEjemplo();
        }

        return lista;
    }

    public static void guardarProductos(List<Productos> lista) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(RUTA)))) {
            for (Productos p : lista) {
                out.println(p.aLineaTxt());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar productos: " + e.getMessage());
        }
    }

    private static void crearDatosEjemplo() {
        List<Productos> ejemplo = new ArrayList<>();
        ejemplo.add(new Productos("P001", "Leche Alquería 1L", 4500, 20));
        ejemplo.add(new Productos("P002", "Pan Tajado Blanco Bimbo 470g", 6600, 15));
        ejemplo.add(new Productos("P003", "Café Sello Rojo 250g", 13500, 10));
        ejemplo.add(new Productos("P004", "Quipitos Pops", 500, 18));
        ejemplo.add(new Productos("P005", "Jugo del Valle Fresh Frutas Cítricas 1.5L", 5000, 23));
    }
}
