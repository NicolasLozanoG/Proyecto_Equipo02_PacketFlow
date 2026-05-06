package com.aed.packetflow.model;

public class Packet {
    private String messageId;
    private int sequenceNumber;
    private int size;
    private boolean isLast;

    public Packet(String messageId, int sequenceNumber, int size, boolean isLast) {
        this.messageId = messageId;
        this.sequenceNumber = sequenceNumber;
        this.size = size;
        this.isLast = isLast;
    }

    public String getMessageId() {
        return messageId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getSize() {
        return size;
    }

    public boolean isLast() {
        return isLast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return sequenceNumber == packet.sequenceNumber && 
               messageId.equals(packet.messageId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(messageId, sequenceNumber);
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "idMsj='" + messageId + '\'' +
                ", sec=" + sequenceNumber +
                ", tamaño=" + size +
                ", último=" + isLast +
                '}';
    }
}
