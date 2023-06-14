/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ulise
 */
public class Ruta {
    private int origen;
    private int destino;
    private double distancia;
    private double peso;
    private double peligro;

    public Ruta(int origen, int destino, double distancia, double peso, double peligro) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.peso = peso;
        this.peligro = peligro;
    }

    int getOrigen() {
        return origen;
    }

    public getDestino() {
        return destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public double getPeso() {
        return peso;
    }

    public double getPeligro() {
        return peligro;
    }

    public void aumentarPeligro() {
        peligro++;
    }

    public void ajustarPeligro() {
        peligro = 0;
    }
}
