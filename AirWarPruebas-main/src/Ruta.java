/**
 * La clase Ruta representa una ruta entre dos lugares, con un nivel de peligro asociado.
 */
public class Ruta {

    private double peligro;
    private Lugar salida;
    private Lugar destino;

    /**
     * Constructor de la clase Ruta.
     *
     * @param salida   El lugar de salida de la ruta.
     * @param destino  El lugar de destino de la ruta.
     * @param peligro  El nivel de peligro asociado a la ruta.
     */
    public Ruta(Lugar salida, Lugar destino, double peligro) {
        this.salida = salida;
        this.destino = destino;
        this.peligro = peligro;
    }

    /**
     * Calcula la distancia entre el lugar de salida y el lugar de destino utilizando la fórmula del haversine.
     *
     * @return La distancia en kilómetros entre el lugar de salida y el lugar de destino.
     */
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
            // Aumenta el peso si es interoceánica aumenta un 20% el peso
            double distancia = (r * c) + (r * c) * 0.2;
            return distancia;
        } else {
            double distancia = r * c;
            return distancia;
        }
    }

    /**
     * Calcula el peso de la ruta teniendo en cuenta la distancia y otros factores.
     *
     * @return El peso de la ruta.
     */
    public double calcularPeso() {
        double distancia = calcularDistancia();
        double peso = distancia;

        if (destino instanceof Portaavion) {
            peso *= 1.10; // Aumenta el peso en un 10% si el destino es un portaaviones
        } else {
            peso *= 1; // Aumenta el peso en un 0% si el destino es un aeropuerto
        }

        if (peligro <= 1) {
            peligro = 1;
            return peso;
        } else {
            //System.out.println("El peso de la ruta se ajustó a "+peso + (peso * getPeligro()));
            return (peso * getPeligro());
        }
    }

    // Getters y setters

    /**
     * Obtiene el lugar de salida de la ruta.
     *
     * @return El lugar de salida.
     */
    public Lugar getSalida() {
        return salida;
    }

    /**
     * Establece el lugar de salida de la ruta.
     *
     * @param salida El lugar de salida.
     */
    public void setSalida(Lugar salida) {
        this.salida = salida;
    }

    /**
     * Obtiene el lugar de destino de la ruta.
     *
     * @return El lugar de destino.
     */
    public Lugar getDestino() {
        return destino;
    }

    /**
     * Establece el lugar de destino de la ruta.
     *
     * @param destino El lugar de destino.
     */
    public void setDestino(Lugar destino) {
        this.destino = destino;
    }

    /**
     * Obtiene el nivel de peligro asociado a la ruta.
     *
     * @return El nivel de peligro.
     */
    public double getPeligro() {
        return peligro;
    }

    /**
     * Establece el nivel de peligro asociado a la ruta.
     *
     * @param peligro El nivel de peligro.
     */
    public void setPeligro(double peligro) {
        this.peligro += peligro;
    }
}
