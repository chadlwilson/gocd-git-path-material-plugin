package com.thoughtworks.go.scm.plugin.cmd;

import com.thoughtworks.go.plugin.api.logging.Logger;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.File;

public class Console {
    private static final Logger LOGGER = Logger.getLoggerFor(Console.class);

    public static CommandLine createCommand(String... args) {
        CommandLine gitCmd = new CommandLine("git");
        gitCmd.addArguments(args, false);
        return gitCmd;
    }

    public static ConsoleResult runOrBomb(CommandLine commandLine, File workingDir, ProcessOutputStreamConsumer stdOut, ProcessOutputStreamConsumer stdErr) {
        Executor executor = new DefaultExecutor();
        executor.setStreamHandler(new PumpStreamHandler(stdOut, stdErr));
        if (workingDir != null) {
            executor.setWorkingDirectory(workingDir);
        }

        try {
            return new ConsoleResult(
                    executor.execute(commandLine),
                    stdOut.output(),
                    stdErr.output());

        } catch (Exception e) {
            LOGGER.warn(getMessage("Command failed", commandLine, workingDir));
            stdOut.output().forEach(LOGGER::warn);
            stdErr.output().forEach(LOGGER::warn);
            throw new RuntimeException(getMessage(String.format("Exception (%s)", e.getMessage()), commandLine, workingDir), e);
        }
    }

    private static String getMessage(String type, CommandLine commandLine, File workingDir) {
        return String.format("%s Occurred: %s - %s ", type, commandLine.toString(), workingDir);
    }
}
