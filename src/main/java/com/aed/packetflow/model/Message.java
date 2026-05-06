package com.aed.packetflow.model;

import java.util.LinkedList;
import java.util.List;

public class Message {
    private String id;
    private int totalSize;
    private int priority;
    private MessageStatus status;
    private List<Packet> receivedPackets;
    private int totalExpectedPackets;

    public Message(String id, int totalSize, int priority) {
        this.id = id;
        this.totalSize = totalSize;
        this.priority = priority;
        this.status = MessageStatus.INCOMPLETO;
        this.receivedPackets = new LinkedList<>();
        this.totalExpectedPackets = -1;
    }

    public String getId() {
        return id;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public int getPriority() {
        return priority;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public List<Packet> getReceivedPackets() {
        return receivedPackets;
    }

    public int getTotalExpectedPackets() {
        return totalExpectedPackets;
    }

    public void setTotalExpectedPackets(int totalExpectedPackets) {
        this.totalExpectedPackets = totalExpectedPackets;
    }

    public void receivePacket(Packet packet) {
        if (!receivedPackets.contains(packet)) {
            receivedPackets.add(packet);
        }
        checkIfComplete();
    }

    public void checkIfComplete() {
        if (totalExpectedPackets != -1 && receivedPackets.size() == totalExpectedPackets) {
            if (this.status != MessageStatus.RECONSTRUIDO) {
                this.status = MessageStatus.COMPLETO;
            }
        }
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "id='" + id + '\'' +
                ", tamaño=" + totalSize +
                ", prioridad=" + priority +
                ", estado=" + status +
                ", esperados=" + totalExpectedPackets +
                ", recibidos=" + receivedPackets.size() +
                '}';
    }
}
