package com.thoughtworks.go.scm.plugin.cmd;

import com.thoughtworks.go.scm.plugin.util.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InMemoryConsumer implements StreamConsumer {
    private Queue<String> lines = new ConcurrentLinkedQueue<String>();

    public void consumeLine(String line) {
        try {
            lines.add(line);
        } catch (RuntimeException e) {
            // LOG.error("Problem consuming line [" + line + "]", e);
        }
    }

    public List<String> asList() {
        return new ArrayList<String>(lines);
    }

    public String toString() {
        return ListUtils.join(asList(), "\n");
    }
}
