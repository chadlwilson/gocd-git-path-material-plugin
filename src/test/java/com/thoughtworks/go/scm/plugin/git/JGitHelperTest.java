package com.thoughtworks.go.scm.plugin.git;

import com.thoughtworks.go.scm.plugin.GitHelper;
import com.thoughtworks.go.scm.plugin.jgit.JGitHelper;
import com.thoughtworks.go.scm.plugin.model.GitConfig;
import org.eclipse.jgit.lib.ConfigConstants;

import java.io.File;


public class JGitHelperTest extends AbstractGitHelperTest {
    @Override
    protected GitHelper getHelper(GitConfig gitConfig, File workingDir) {
        final JGitHelper helper = new JGitHelper(gitConfig, workingDir);

        // Disable any GPG
        helper.setRepoConfigConsumer(config -> config.setBoolean(
                ConfigConstants.CONFIG_COMMIT_SECTION, null,
                ConfigConstants.CONFIG_KEY_GPGSIGN, false));

        return helper;
    }
}