package com.thoughtworks.go.scm.plugin.jgit;

import com.thoughtworks.go.scm.plugin.AbstractGitHelperTest;
import com.thoughtworks.go.scm.plugin.git.GitHelper;
import com.thoughtworks.go.scm.plugin.model.GitConfig;
import org.eclipse.jgit.lib.ConfigConstants;

import java.io.File;


public class JGitHelperTest2 extends AbstractGitHelperTest {
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