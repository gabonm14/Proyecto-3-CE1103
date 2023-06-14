
import java.util.List;

public abstract class Lugar {

    double latitude;
    double longitude;

    public Lugar(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getX() {
        double longitudeRange = 180.0;
        return (longitude + longitudeRange / 2.0) / longitudeRange * 1280;
    }

    public double getY() {
        double latitudeRange = 90.0;
        return (latitude + latitudeRange / 2.0) / latitudeRange * 720;
    }

    public abstract void recibirAvion(Avion avion);

    public abstract Avion despacharAvion(Avion avion);

    public abstract List<Avion> getAvionesEsperando();
}
