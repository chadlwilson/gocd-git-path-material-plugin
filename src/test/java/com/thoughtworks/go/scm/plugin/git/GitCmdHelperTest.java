package com.thoughtworks.go.scm.plugin.git;

import com.thoughtworks.go.scm.plugin.GitHelper;
import com.thoughtworks.go.scm.plugin.model.GitConfig;
import com.thoughtworks.go.scm.plugin.model.Revision;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GitCmdHelperTest extends AbstractGitHelperTest {
    @Override
    protected GitHelper getHelper(GitConfig gitConfig, File workingDir) {
        return new GitCmdHelper(gitConfig, workingDir);
    }

    @Test
    public void shouldShallowClone() throws Exception {
        extractToTmp("/sample-repository/simple-git-repository-2.zip");

        GitHelper git = getHelper(new GitConfig("file://" + simpleGitRepository.getAbsolutePath(), null, null, "master", false, true), testRepository);
        git.cloneOrFetch();

        assertThat(git.getCommitCount(), is(1));

        Revision revision = git.getLatestRevision();

        verifyRevision(revision, "24ce45d1a1427b643ae859777417bbc9f0d7cec8", "3\ntest multiline\ncomment", 1422189618000L, List.of(ImmutablePair.of("a.txt", "added"), ImmutablePair.of("b.txt", "added")));

        // poll again
        git.cloneOrFetch();

        List<Revision> newerRevisions = git.getRevisionsSince("24ce45d1a1427b643ae859777417bbc9f0d7cec8");

        assertThat(newerRevisions.isEmpty(), is(true));
    }
}
