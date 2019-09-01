package com.thoughtworks.go.scm.plugin.cmd;

import java.util.List;

public interface StreamConsumer {
    public void consumeLine(String line);

    public List<String> asList();
}
