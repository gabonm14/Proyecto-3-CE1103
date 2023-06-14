// Definición de pines
const int ledPin2 = 2;
const int ledPin3 = 3;
const int ledPin4 = 4;
const int ledPin5 = 5;
const int ledPin6 = 6;
const int buttonPin8 = 8;
const int buttonPin9 = 9;
const int buttonPin10 = 10;
const int buttonPin11 = 11;
const int buzzerPin = 7;

// Variables de estado de los LEDs
int currentLED = ledPin2;  // LED actualmente activo
bool direction = true;  // Dirección del movimiento de la luz (true: hacia la derecha, false: hacia la izquierda)
bool moveLEDs = true;  // Indicador de movimiento de los LEDs
bool buzzerActive = false;  // Estado del buzzer

void setup() {
  // Configurar pines de los LEDs como salidas
  pinMode(ledPin2, OUTPUT);
  pinMode(ledPin3, OUTPUT);
  pinMode(ledPin4, OUTPUT);
  pinMode(ledPin5, OUTPUT);
  pinMode(ledPin6, OUTPUT);

  // Configurar pin del buzzer como salida
  pinMode(buzzerPin, OUTPUT);

  // Configurar pines de los botones como entradas con resistencias pull-up
  pinMode(buttonPin8, INPUT_PULLUP);
  pinMode(buttonPin9, INPUT_PULLUP);
  pinMode(buttonPin10, INPUT_PULLUP);
  pinMode(buttonPin11, INPUT_PULLUP);

  // Inicializar comunicación serial
  Serial.begin(9600);
}

void loop() {
  // Leer estado de los botones
  bool buttonState8 = digitalRead(buttonPin8);
  bool buttonState9 = digitalRead(buttonPin9);
  bool buttonState10 = digitalRead(buttonPin10);
  bool buttonState11 = digitalRead(buttonPin11);

  // Controlar dirección del movimiento de la luz
  if (buttonState10 == LOW) {
    moveLEDs = !moveLEDs;  // Alternar el estado del movimiento de los LEDs
    delay(1);  // Pequeña pausa para evitar cambios rápidos
  }

  // Verificar si los LEDs deben moverse o mantenerse estáticos
  if (moveLEDs) {
    // Encender el LED actual
    digitalWrite(currentLED, HIGH);

    // Apagar el LED de al lado si está encendido
    if (direction) {
      if (currentLED > ledPin2) {
        digitalWrite(currentLED - 1, LOW);
      }
    } else {
      if (currentLED < ledPin6) {
        digitalWrite(currentLED + 1, LOW);
      }
    }

    // Controlar la dirección del movimiento
    if (direction) {
      currentLED++;
      if (currentLED > ledPin6) {
        currentLED = ledPin6;
        direction = !direction;
      }
    } else {
      currentLED--;
      if (currentLED < ledPin2) {
        currentLED = ledPin2;
        direction = !direction;
      }
    }
  } else {
    // Apagar todos los LEDs y encender solo el LED actual
    digitalWrite(ledPin2, LOW);
    digitalWrite(ledPin3, LOW);
    digitalWrite(ledPin4, LOW);
    digitalWrite(ledPin5, LOW);
    digitalWrite(ledPin6, LOW);
    digitalWrite(currentLED, HIGH);
  }

  // Verificar si se cumple la condición para activar el buzzer
  if (buttonState10 == LOW && currentLED == ledPin4) {
    buzzerActive = true;
   
  } else {
    buzzerActive = false;
  
  }


if (buttonState8 == LOW ) {
    //buzzerActive = true;
   Serial.println("2"); // Enviar "1"
 
  
  }
  if (buttonState9 == LOW ) {
    //buzzerActive = true;
   Serial.println("3"); // Enviar "1"
 
  
  }

  // Controlar el estado del buzzer
  if (buzzerActive) {
    //Serial.println("Se cumple la condición. Buzzer activado.");
    tone(buzzerPin, 1000); // Emitir un tono en el buzzer
    Serial.println("1"); // Enviar "1"
  } else {
    
    noTone(buzzerPin); // Detener el tono del buzzer
  }

  // Reiniciar el movimiento de los LEDs si se presiona el botón 11
  if (buttonState11 == LOW) {
    moveLEDs = true;
    direction = true;
    currentLED = ledPin2;
  }

  // Pequeña pausa para ralentizar el movimiento
  delay(150);
}
