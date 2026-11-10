package com.examble.puntodeventa.modelos;

public class CierreCaja {
    private String fecha;
    private double baseInicial;
    private double totalVendido;
    private double efectivoEsperado;
    private double efectivoContado;
    private double diferencia;


    public CierreCaja(String fecha, double baseInicial, double totalVendido, double efectivoEsperado, double efectivoContado, double diferencia) {
        this.fecha = fecha;
        this.baseInicial = baseInicial;
        this.totalVendido = totalVendido;
        this.efectivoEsperado = efectivoEsperado;
        this.efectivoContado = efectivoContado;
        this.diferencia = diferencia;
    }
    
    public String getFecha() { return fecha; }
    public double getBaseInicial() { return baseInicial; }
    public double getTotalVendido() { return totalVendido; }
    public double getEfectivoEsperado() { return efectivoEsperado; }
    public double getEfectivoContado() { return efectivoContado; }
    public double getDiferencia() { return diferencia; }

    public String aLineaTxt() {
        return (fecha + "|" + baseInicial + "|" + totalVendido + "|" + efectivoEsperado + "|" + efectivoContado + "|" + diferencia);
    }

}
