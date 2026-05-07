# Informe de Proyecto: Packet Flow

**Algoritmos y Estructuras de Datos - UCU**

**Integrantes:** Emanuel Shu, Ignacio Rodriguez, Nicolas Lozano

---

## 1. Introducción
PacketFlow es un sistema de simulación de red por consola desarrollado en Java [cite: 4]. Su objetivo principal es modelar la fragmentación de datos, transmisión y posterior reconstrucción utilizando estructuras de datos lineales como listas y colas [cite: 5]. El proyecto demuestra cómo los mensajes se dividen en paquetes, se envían por una red de capacidad limitada y se reconstruyen en el receptor [cite: 6].

## 2. Arquitectura del Sistema
El sistema se organiza en el paquete `com.aed.packetflow.model` e incluye las siguientes clases principales [cite: 8, 9]:

* **Message:** Representa un mensaje lógico. Mantiene el estado (`INCOMPLETO`, `COMPLETO`, `RECONSTRUIDO`), prioridad y lista de paquetes recibidos [cite: 10, 11, 29].
* **Packet:** Fragmento de un mensaje que transporta su número de secuencia y tamaño [cite: 14, 16].
* **Network:** Simula el medio de transmisión. Maneja la fragmentación según el `maxPacketSize` y gestiona la cola de tránsito [cite: 18, 20, 21].
* **Reconstructor:** Se encarga de ordenar los paquetes recibidos para validar la integridad del mensaje [cite: 23, 26].
* **Main:** Punto de entrada con un menú interactivo para configurar la red y gestionar el tráfico [cite: 28, 31].

## 3. Estructuras de Datos Utilizadas
Se implementaron exclusivamente estructuras lineales como exige el requerimiento [cite: 67]:

* **Queue<Packet> (LinkedList):** Utilizada en la red para el tránsito de paquetes bajo una lógica FIFO (First-In, First-Out) [cite: 34, 35].
* **List<Message> / List<Packet> (LinkedList):** Utilizadas para el registro de mensajes y la acumulación de fragmentos recibidos por cada mensaje [cite: 36].

## 4. Flujo de Ejecución
1.  **Fragmentación:** La red divide el mensaje en paquetes calculados según el tamaño máximo permitido [cite: 40, 41].
2.  **Transmisión:** Los paquetes entran en la cola de tránsito. Si la capacidad máxima de la red se supera, los paquetes se descartan (pérdida de paquetes) [cite: 42, 43].
3.  **Eliminación:** Permite buscar y purgar un mensaje por su ID, eliminando también sus paquetes asociados en tránsito [cite: 79].
4.  **Recepción y Reconstrucción:** El receptor extrae paquetes y los asigna al mensaje correspondiente. Una vez completo, se ordenan por número de secuencia para su reconstrucción final [cite: 44, 46, 84].

## 5. Decisiones de Diseño y Dificultades
* **Asunciones:** Se asume que los identificadores de mensajes son únicos. Respecto a la prioridad (opcional), se integró el atributo en la clase `Message` [cite: 85].
* **Dificultades Algorítmicas:** El desafío principal fue la eliminación de mensajes en tránsito [cite: 79]. Dado que una `Queue` estándar no permite eliminar elementos intermedios, se implementó una solución que filtra la cola original hacia una temporal, descartando los fragmentos del mensaje eliminado.
* **Localización:** Se refactorizaron las salidas de consola para asegurar que toda la interacción con el usuario sea en español [cite: 49].

## 6. Validación y Pruebas (JUnit)
Se implementó una suite de pruebas unitarias para validar los requerimientos críticos [cite: 86]:
* **Fragmentación:** Verifica el cálculo correcto de paquetes según el tamaño del mensaje.
* **Capacidad de Red:** Comprueba que el sistema descarte paquetes correctamente al alcanzar el límite definido.
* **Reordenamiento:** Valida que el `Reconstructor` maneje paquetes que llegan en orden aleatorio.
* **Eliminación:** Asegura que la purga de mensajes limpie efectivamente la red.

## 7. Conclusión
PacketFlow integra conceptos de redes y estructuras de datos para resolver el problema de la fragmentación y el reensamblaje de información [cite: 52]. El uso de pruebas unitarias y una arquitectura modular garantiza un sistema extensible y robusto.