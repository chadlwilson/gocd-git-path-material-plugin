package com.thoughtworks.go.scm.plugin.cmd;

import java.util.List;

public interface StreamConsumer {
    void consumeLine(String line);

    List<String> asList();
}
