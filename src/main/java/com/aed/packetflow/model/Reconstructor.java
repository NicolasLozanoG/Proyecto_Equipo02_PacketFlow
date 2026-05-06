package com.aed.packetflow.model;

import java.util.Comparator;

public class Reconstructor {
    public static boolean reconstruct(Message message) {
        if (message.getStatus() == MessageStatus.COMPLETO) {
            // Reordenar paquetes usando el sequence number
            message.getReceivedPackets().sort(Comparator.comparingInt(Packet::getSequenceNumber));
            
            // Cambiar estado a reconstruido
            message.setStatus(MessageStatus.RECONSTRUIDO);
            return true;
        }
        return false;
    }
}
