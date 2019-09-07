package com.thoughtworks.go.scm.plugin.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InMemoryConsumer implements StreamConsumer {
    private final Queue<String> lines = new ConcurrentLinkedQueue<>();

    public void consumeLine(String line) {
        try {
            lines.add(line);
        } catch (RuntimeException ignore) {
        }
    }

    public List<String> asList() {
        return new ArrayList<>(lines);
    }

    public String toString() {
        return String.join(System.lineSeparator(), lines);
    }
}
