
public class Ruta {

    private double peligro;
    private Lugar salida;
    private Lugar destino;

    public Ruta(Lugar salida, Lugar destino, double peligro) {
        this.salida = salida;
        this.destino = destino;
        this.peligro = peligro;
    }

    public double calcularDistancia() {
        double lat1 = Math.toRadians(salida.getLatitude());
        double lon1 = Math.toRadians(salida.getLongitude());
        double lat2 = Math.toRadians(destino.getLatitude());
        double lon2 = Math.toRadians(destino.getLongitude());

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Radio de la Tierra en kilómetros
        double r = 6637.1;

        if (salida.getLongitude() < 0 && destino.getLongitude() > 0) {
            //Aumenta el peso si es interoceanica aumenta un 20% el peso
            double distancia = (r * c) + (r * c) * 0.2;
            return distancia;
        } else {
            double distancia = r * c;
            return distancia;
        }

    }

    public double calcularPeso() {
        double distancia = calcularDistancia();
        double peso = distancia;

        if (destino instanceof Portaavion) {
            peso *= 1.10; // Aumenta el peso en un 10% si el destino es un portaaviones
        } else {
            peso *= 1; // Aumenta el peso en un 0% si el destino es un aeropuerto
        }

        if (peligro <= 1) {
            return peso;
        } else {
            //System.out.println("El peso de la ruta se ajusto a "+peso + (peso * getPeligro()));
            return (peso * getPeligro());
        }

    }

    // Getters y setters
    public Lugar getSalida() {
        return salida;
    }

    public void setSalida(Lugar salida) {
        this.salida = salida;
    }

    public Lugar getDestino() {
        return destino;
    }

    public void setDestino(Lugar destino) {
        this.destino = destino;
    }

    public double getPeligro() {
        return peligro;
    }

    public void setPeligro(double peligro) {
        this.peligro += peligro;
    }
}
