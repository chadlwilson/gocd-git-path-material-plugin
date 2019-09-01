package com.thoughtworks.go.scm.plugin.git;


import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.scm.plugin.cmd.ProcessOutputStreamConsumer;
import com.thoughtworks.go.scm.plugin.jgit.JGitHelper;
import com.thoughtworks.go.scm.plugin.model.GitConfig;

import java.io.File;

public class HelperFactory {
    private static final Logger LOGGER = Logger.getLoggerFor(HelperFactory.class);

    public static GitHelper git(GitConfig gitConfig, File workingDirectory, ProcessOutputStreamConsumer stdOut, ProcessOutputStreamConsumer stdErr) {
        GitHelper gitCmd = gitCmd(gitConfig, workingDirectory, stdOut, stdErr);
        if (isAvailable(gitCmd))
            return gitCmd;

        return jGit(gitConfig, workingDirectory, stdOut, stdErr);
    }

    private static GitHelper gitCmd(GitConfig gitConfig, File workingDirectory, ProcessOutputStreamConsumer stdOut, ProcessOutputStreamConsumer stdErr) {
        return new GitCmdHelper(gitConfig, workingDirectory, stdOut, stdErr);
    }

    private static GitHelper jGit(GitConfig gitConfig, File workingDirectory, ProcessOutputStreamConsumer stdOut, ProcessOutputStreamConsumer stdErr) {
        return new JGitHelper(gitConfig, workingDirectory, stdOut, stdErr);
    }

    public static GitHelper git(GitConfig gitConfig, File workingDirectory) {
        GitHelper gitCmd = gitCmd(gitConfig, workingDirectory);
        if (isAvailable(gitCmd))
            return gitCmd;

        return jGit(gitConfig, workingDirectory);
    }

    private static GitHelper gitCmd(GitConfig gitConfig, File workingDirectory) {
        return new GitCmdHelper(gitConfig, workingDirectory);
    }

    private static GitHelper jGit(GitConfig gitConfig, File workingDirectory) {
        return new JGitHelper(gitConfig, workingDirectory);
    }

    private static boolean isAvailable(GitHelper gitCmd) {
        try {
            // make sure git is available
            LOGGER.info("Command line git found [{}]", gitCmd.version());
            return true;
        } catch (Exception e) {
            LOGGER.info("No command line git found; will continue with JGit [{}]", e.toString());
        }
        return false;
    }
}
