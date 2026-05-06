package com.aed.packetflow;

import com.aed.packetflow.model.Message;
import com.aed.packetflow.model.Network;
import com.aed.packetflow.model.Packet;
import com.aed.packetflow.model.Reconstructor;

import java.util.Scanner;

public class Main {
    private static Network network;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Bienvenido a PacketFlow ===");

        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Configurar Red");
            System.out.println("2. Enviar Mensaje");
            System.out.println("3. Recibir siguiente paquete");
            System.out.println("4. Reconstruir Mensajes");
            System.out.println("5. Eliminar Mensaje");
            System.out.println("6. Consultar Estado de la Red");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            String input = scanner.nextLine();
            int option = -1;
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (option) {
                case 1:
                    configurarRed(scanner);
                    break;
                case 2:
                    enviarMensaje(scanner);
                    break;
                case 3:
                    recibirPaquete();
                    break;
                case 4:
                    reconstruirMensajes();
                    break;
                case 5:
                    eliminarMensaje(scanner);
                    break;
                case 6:
                    consultarEstado();
                    break;
                case 7:
                    System.out.println("Saliendo de PacketFlow...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private static void configurarRed(Scanner scanner) {
        System.out.print("Capacidad de la red (max paquetes en tránsito): ");
        int capacity = Integer.parseInt(scanner.nextLine());
        System.out.print("Tamaño máximo de paquete: ");
        int maxPacketSize = Integer.parseInt(scanner.nextLine());
        network = new Network(capacity, maxPacketSize);
        System.out.println("Red configurada correctamente.");
    }

    private static void enviarMensaje(Scanner scanner) {
        if (network == null) {
            System.out.println("Debe configurar la red primero.");
            return;
        }
        System.out.print("Identificador del mensaje: ");
        String id = scanner.nextLine();
        System.out.print("Tamaño total del mensaje: ");
        int size = Integer.parseInt(scanner.nextLine());
        System.out.print("Prioridad (opcional, presione enter para 0): ");
        String priorityStr = scanner.nextLine();
        int priority = priorityStr.isEmpty() ? 0 : Integer.parseInt(priorityStr);

        Message msg = new Message(id, size, priority);
        if (network.sendMessage(msg)) {
            System.out.println("Mensaje '" + id + "' enviado a la red y fragmentado en " + msg.getTotalExpectedPackets() + " paquetes.");
        }
    }

    private static void recibirPaquete() {
        if (network == null) {
            System.out.println("Debe configurar la red primero.");
            return;
        }
        Packet p = network.receiveNextPacket();
        if (p == null) {
            System.out.println("No hay paquetes en tránsito en la red.");
        } else {
            System.out.println("Paquete recibido de la red: " + p);
            Message m = network.findMessage(p.getMessageId());
            if (m != null) {
                m.receivePacket(p);
                System.out.println("Paquete asignado al mensaje '" + m.getId() + "'. Estado del mensaje: " + m.getStatus());
            } else {
                System.out.println("El mensaje asociado a este paquete ya no existe (fue eliminado). Paquete descartado.");
            }
        }
    }

    private static void reconstruirMensajes() {
        if (network == null) {
            System.out.println("Debe configurar la red primero.");
            return;
        }
        int count = 0;
        for (Message m : network.getMessages()) {
            if (Reconstructor.reconstruct(m)) {
                count++;
                System.out.println("Mensaje '" + m.getId() + "' ha sido RECONSTRUIDO exitosamente.");
            }
        }
        if (count == 0) {
            System.out.println("No hay mensajes completos listos para ser reconstruidos.");
        }
    }

    private static void eliminarMensaje(Scanner scanner) {
        if (network == null) {
            System.out.println("Debe configurar la red primero.");
            return;
        }
        System.out.print("Identificador del mensaje a eliminar: ");
        String id = scanner.nextLine();
        if (network.deleteMessage(id)) {
            System.out.println("Mensaje '" + id + "' y sus paquetes en tránsito han sido eliminados.");
        } else {
            System.out.println("Mensaje '" + id + "' no encontrado.");
        }
    }

    private static void consultarEstado() {
        if (network == null) {
            System.out.println("Debe configurar la red primero.");
            return;
        }
        System.out.println("--- ESTADO DE LA RED ---");
        System.out.println("Paquetes en tránsito: " + network.getTransitCount() + " / " + network.getCapacity());
        System.out.println("Mensajes en el sistema: " + network.getMessages().size());
        for (Message m : network.getMessages()) {
            int missing = m.getTotalExpectedPackets() - m.getReceivedPackets().size();
            System.out.println(" - " + m.toString() + " (Faltantes: " + missing + ")");
        }
        System.out.println("------------------------");
    }
}
