
import com.fazecast.jSerialComm.*;

public class ArduinoReceiver {

    private SerialPort serialPort;
    private boolean isOpen;
    private DataListener dataListener;
    private String dataReceived; // Variable de instancia para almacenar el valor recibido

    public interface DataListener {

        void onDataReceived(String data);
    }

    public ArduinoReceiver() {
        serialPort = null;
        isOpen = false;
        dataListener = null;
    }

    public void connect(String portName, int baudRate) {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);

        if (serialPort.openPort()) {
            isOpen = true;
            serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        return;
                    }

                    byte[] newData = new byte[serialPort.bytesAvailable()];
                    serialPort.readBytes(newData, newData.length);
                    String receivedData = new String(newData);

                    if (receivedData.trim().equals("1")) {
                        MapApp.dataReceived = receivedData;
                        receivedData = "0";
                        // Ejecutar acción si el valor recibido es 1
                        // Acción 1
                    } else if (receivedData.trim().equals("2")) {
                        if ((MapApp.avionesEnVuelo.size() ) > MapApp.indexAvion) {
                            MapApp.indexAvion++;
                            receivedData = "0";

                            // Ejecutar acción si el valor recibido es 2
                            // Acción 2
                        }
                    } else if (receivedData.trim().equals("3")) {
                        if (MapApp.indexAvion > 0) {
                            MapApp.indexAvion--;
                            receivedData = "0";

                            // Ejecutar acción si el valor recibido es 2
                            // Acción 2
                        }
                    }

                    //System.out.println("Received data: " + receivedData);
                    //dataReceived = receivedData; // Asignar el valor recibido a la variable de instancia
                }
            });
        } else {
            System.err.println("Failed to open the serial port.");
        }
    }

    public void disconnect() {
        if (isOpen) {
            serialPort.removeDataListener();
            serialPort.closePort();
            isOpen = false;
        }
    }

    public void setDataListener(DataListener listener) {
        this.dataListener = listener;
    }

    public String getDataReceived() {
        return dataReceived;
    }
}
