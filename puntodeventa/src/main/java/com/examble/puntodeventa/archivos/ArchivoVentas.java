package com.examble.puntodeventa.archivos;

import com.examble.puntodeventa.modelos.Venta;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//import org.jetbrains.kotlin.it.unimi.dsi.fastutil.longs.V;
//import org.jetbrains.kotlin.it.unimi.dsi.fastutil.objects.v;

public class ArchivoVentas {
    private static final String RUTA = "ventas.txt";

    public static List<Venta> cargarVentas() {
        List<Venta> lista = new ArrayList<>();
        File file = new File(RUTA);
        if (!file.exists()) {
            crearDatosEjemplo();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Venta v = Venta.desdeLineaTxt(linea);
                    if (v != null) lista.add(v);
                }                
            }
        } catch (IOException e){
            System.err.println("Error al cargar ventas: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarVentas(List<Venta> lista) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(RUTA)))) {
            for (Venta v : lista) {
                out.println(v.aLineaTxt());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar ventas: " + e.getMessage());
        }
    }

    public static void agregarVenta(Venta v) {
        List<Venta> lista = cargarVentas();
        lista.add(v);
        guardarVentas(lista);
    }

    private static void crearDatosEjemplo() {
        List<Venta> ejemplo = new ArrayList<>();
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        ejemplo.add(new Venta("V001", fecha, "1017123456", 14500.0, 20000.0, 5500.0));
        guardarVentas(ejemplo);

    }
}
