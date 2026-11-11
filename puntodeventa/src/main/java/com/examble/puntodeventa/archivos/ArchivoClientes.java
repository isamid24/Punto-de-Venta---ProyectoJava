package com.examble.puntodeventa.archivos;

import com.examble.puntodeventa.modelos.Clientes;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoClientes {
    private static final String RUTA = "clientes.txt";

    public static List<Clientes> cargarClientes() {
        List<Clientes> lista = new ArrayList<>();
        File file = new File(RUTA);
        if (!file.exists()) {
            crearDatosEjemplo();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Clientes c = Clientes.desdeLineaTxt(linea);
                    if (c != null) lista.add(c);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarClientes(List<Clientes> lista) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(RUTA)))) {
            for (Clientes c : lista) {
                out.println(c.aLineaTxt());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar clientes: " + e.getMessage());
        }
    }

    private static void crearDatosEjemplo() {
        List<Clientes> ejemplo = new ArrayList<>();
        ejemplo.add(new Clientes("1017123456", "Isabella Sánchez", "3001234567"));
        ejemplo.add(new Clientes("1018987654", "Juan Pérez", "3119876543"));
        guardarClientes(ejemplo);
    }
}