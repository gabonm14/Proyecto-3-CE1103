import java.util.*;

public class Grafo {
    private int[][] matrizAdyacencia;
    private List<AirPort> aeropuertos;
    
    public Grafo(List<AirPort> aeropuertos) {
        this.aeropuertos = aeropuertos;
        this.matrizAdyacencia = new int[aeropuertos.size()][aeropuertos.size()];
    }
    
    public void agregarRuta(AirPort origen, AirPort destino, int peso) {
        int origenIndex = aeropuertos.indexOf(origen);
        int destinoIndex = aeropuertos.indexOf(destino);
        
        matrizAdyacencia[origenIndex][destinoIndex] = peso;
    }
    
    public int obtenerPesoRuta(AirPort origen, AirPort destino) {
        int origenIndex = aeropuertos.indexOf(origen);
        int destinoIndex = aeropuertos.indexOf(destino);
        
        return matrizAdyacencia[origenIndex][destinoIndex];
    }
    
    public List<AirPort> obtenerMejorRuta(AirPort origen, AirPort destino) {
        int origenIndex = aeropuertos.indexOf(origen);
        int destinoIndex = aeropuertos.indexOf(destino);
        
        int numVertices = aeropuertos.size();
        int[][] distancias = new int[numVertices][numVertices];
        int[][] rutas = new int[numVertices][numVertices];
        
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                distancias[i][j] = matrizAdyacencia[i][j];
                
                if (distancias[i][j] != 0 && distancias[i][j] != Integer.MAX_VALUE) {
                    rutas[i][j] = i;
                } else {
                    rutas[i][j] = -1;
                }
            }
        }
        
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distancias[i][k] != Integer.MAX_VALUE && distancias[k][j] != Integer.MAX_VALUE &&
                            distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                        rutas[i][j] = rutas[k][j];
                    }
                }
            }
        }
        
        if (distancias[origenIndex][destinoIndex] == Integer.MAX_VALUE) {
            return null; // No hay ruta entre el origen y el destino
        }
        
        List<AirPort> ruta = new ArrayList<>();
        int actual = origenIndex;
        while (actual != destinoIndex) {
            ruta.add(aeropuertos.get(actual));
            actual = rutas[actual][destinoIndex];
        }
        ruta.add(aeropuertos.get(destinoIndex));
        
        return ruta;
    }
}