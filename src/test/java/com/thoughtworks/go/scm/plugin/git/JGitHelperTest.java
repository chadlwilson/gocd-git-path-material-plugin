package com.thoughtworks.go.scm.plugin.git;

import com.thoughtworks.go.scm.plugin.GitHelper;
import com.thoughtworks.go.scm.plugin.jgit.JGitHelper;
import com.thoughtworks.go.scm.plugin.model.GitConfig;
import org.eclipse.jgit.lib.ConfigConstants;

import java.io.File;


public class JGitHelperTest extends AbstractGitHelperTest {
    @Override
    protected GitHelper getHelper(GitConfig gitConfig, File workingDir) {
        return new JGitHelper(gitConfig, workingDir);
    }
}