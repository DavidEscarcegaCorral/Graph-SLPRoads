/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graph;

/**
 *
 * @author janethcristinagalvanquinonez
 */
public class Arista {
    
    int peso;
    private Nodo inicio;
    private Nodo destino;

    public Arista(int peso, Nodo inicio, Nodo destino) {
        this.peso = peso;
        this.inicio = inicio;
        this.destino = destino;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public Nodo getInicio() {
        return inicio;
    }

    public void setInicio(Nodo inicio) {
        this.inicio = inicio;
    }

    public Nodo getDestino() {
        return destino;
    }

    public void setDestino(Nodo destino) {
        this.destino = destino;
    }
    
    
    
    
    
}  
    

