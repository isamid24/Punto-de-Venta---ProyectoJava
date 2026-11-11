package com.examble.puntodeventa.archivos;

import com.examble.puntodeventa.modelos.CierreCaja;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoCierres {
    private static final String ARCHIVO = "cierres.txt";

    public static List<CierreCaja> cargarCierres() {
        List<CierreCaja> lista = new ArrayList<>();
        File file = new File(ARCHIVO);

        if (!file.exists()) {
            return lista;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split("\\|");
                if (partes.length >= 8) {
                    String id = partes[0];
                    String fechaHora = partes[1];
                    double baseInicial = Double.parseDouble(partes[2]);
                    double totalVentas = Double.parseDouble(partes[3]);
                    double totalEsperado = Double.parseDouble(partes[4]);
                    double efectivoContado = Double.parseDouble(partes[5]);
                    double diferencia = Double.parseDouble(partes[6]);
                    String estado = partes[7];

                    lista.add(new CierreCaja(id, fechaHora, baseInicial, totalVentas, 
                                             totalEsperado, efectivoContado, diferencia, estado));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void guardarCierre(CierreCaja cierre) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            String linea = String.join("|",
                    cierre.getId(),
                    cierre.getFechaHora(),
                    String.valueOf(cierre.getBaseInicial()),
                    String.valueOf(cierre.getTotalVentas()),
                    String.valueOf(cierre.getTotalEsperado()),
                    String.valueOf(cierre.getEfectivoContado()),
                    String.valueOf(cierre.getDiferencia()),
                    cierre.getEstado()
            );
            writer.write(linea);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}