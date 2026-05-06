package com.aed.packetflow.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Network {
    private int capacity;
    private int maxPacketSize;
    private Queue<Packet> transitQueue;
    private List<Message> messages;

    public Network(int capacity, int maxPacketSize) {
        this.capacity = capacity;
        this.maxPacketSize = maxPacketSize;
        this.transitQueue = new LinkedList<>();
        this.messages = new LinkedList<>();
    }

    public boolean sendMessage(Message message) {
        if (findMessage(message.getId()) != null) {
            System.out.println("Error: El mensaje con ID " + message.getId() + " ya existe.");
            return false;
        }

        messages.add(message);
        int totalSize = message.getTotalSize();
        int expectedPackets = (int) Math.ceil((double) totalSize / maxPacketSize);
        message.setTotalExpectedPackets(expectedPackets);

        for (int i = 0; i < expectedPackets; i++) {
            if (transitQueue.size() >= capacity) {
                System.out.println("¡Capacidad de red alcanzada! Descartando paquete " + i + " del mensaje " + message.getId());
                continue;
            }
            int size = (i == expectedPackets - 1) ? (totalSize - i * maxPacketSize) : maxPacketSize;
            boolean isLast = (i == expectedPackets - 1);
            Packet p = new Packet(message.getId(), i, size, isLast);
            transitQueue.add(p);
        }
        return true;
    }

    public Message findMessage(String id) {
        for (Message m : messages) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public boolean deleteMessage(String id) {
        Message m = findMessage(id);
        if (m != null) {
            messages.remove(m);
            Queue<Packet> newQueue = new LinkedList<>();
            while (!transitQueue.isEmpty()) {
                Packet p = transitQueue.poll();
                if (!p.getMessageId().equals(id)) {
                    newQueue.add(p);
                }
            }
            transitQueue = newQueue;
            return true;
        }
        return false;
    }

    public Packet receiveNextPacket() {
        return transitQueue.poll();
    }

    public int getTransitCount() {
        return transitQueue.size();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public int getMaxPacketSize() {
        return maxPacketSize;
    }

    public int getCapacity() {
        return capacity;
    }
}
