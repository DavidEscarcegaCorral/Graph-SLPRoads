/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graph;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author janethcristinagalvanquinonez
 */
public class Grafo {
    
    private Map<String, Nodo> nodos;
    
    public Grafo(){
        this.nodos= new HashMap<>();
    }
    
    
    public void agregarNodo(String nombre, int x, int y){
        Nodo nodo= new Nodo(nombre, x, y);
        nodos.put(nombre, nodo);
    }
    
    public void cargarNodos() throws IOException{
        ObjectMapper mapper= new ObjectMapper();
        File file = new File("src/main/java/informacionMapa/nodos.json");
        TypeReference<List<Nodo>> typeReference= new TypeReference<>() {};
        List<Nodo> listaNodos= mapper.readValue(file, typeReference);
        for(Nodo nodo: listaNodos){
            this.nodos.put(nodo.getNombre(), nodo);
        }
  
        
      
        
    }
    public void imprimirNodo(){
        for(Nodo nodo: nodos.values()){
            System.out.println(nodo);
            
        }
        
    }


    public Map<String, Nodo> getNodos() {
        return nodos;
    }
}
    

