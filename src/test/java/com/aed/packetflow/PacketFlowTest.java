package com.aed.packetflow;

import com.aed.packetflow.model.Message;
import com.aed.packetflow.model.MessageStatus;
import com.aed.packetflow.model.Network;
import com.aed.packetflow.model.Packet;
import com.aed.packetflow.model.Reconstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PacketFlowTest {

    private Network network;

    @BeforeEach
    public void setUp() {
        // Red con capacidad para 10 paquetes y tamaño máximo de 100 bytes
        network = new Network(10, 100);
    }

    @Test
    public void testSendMessageFragmentation() {
        Message msg = new Message("MSG1", 250, 1);
        assertTrue(network.sendMessage(msg));
        
        // 250 / 100 = 2.5 -> 3 paquetes esperados
        assertEquals(3, msg.getTotalExpectedPackets());
        assertEquals(3, network.getTransitCount());
    }

    @Test
    public void testNetworkCapacityLimit() {
        // Red con capacidad de 2 paquetes
        Network smallNetwork = new Network(2, 100);
        Message msg = new Message("MSG2", 300, 1); // Necesita 3 paquetes
        smallNetwork.sendMessage(msg);
        
        // Solo debería encolar 2 paquetes
        assertEquals(2, smallNetwork.getTransitCount());
    }

    @Test
    public void testReceiveAndReconstruct() {
        Message msg = new Message("MSG3", 150, 1);
        network.sendMessage(msg);
        
        // Recibir los dos paquetes
        Packet p1 = network.receiveNextPacket();
        Packet p2 = network.receiveNextPacket();
        
        assertNotNull(p1);
        assertNotNull(p2);
        
        msg.receivePacket(p2); // Simulamos que llega desordenado
        msg.receivePacket(p1);
        
        assertEquals(MessageStatus.COMPLETO, msg.getStatus());
        
        assertTrue(Reconstructor.reconstruct(msg));
        assertEquals(MessageStatus.RECONSTRUIDO, msg.getStatus());
        
        // Verificar orden de la lista
        assertEquals(0, msg.getReceivedPackets().get(0).getSequenceNumber());
        assertEquals(1, msg.getReceivedPackets().get(1).getSequenceNumber());
    }

    @Test
    public void testDeleteMessage() {
        Message msg = new Message("MSG4", 150, 1);
        network.sendMessage(msg);
        
        assertEquals(1, network.getMessages().size());
        assertEquals(2, network.getTransitCount());
        
        assertTrue(network.deleteMessage("MSG4"));
        
        assertEquals(0, network.getMessages().size());
        assertEquals(0, network.getTransitCount());
    }
}
