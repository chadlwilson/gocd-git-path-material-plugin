package com.thoughtworks.go.scm.plugin.model;

import com.thoughtworks.go.scm.plugin.util.ListUtils;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
public class Revision {
    private String revision;
    private Date timestamp;
    private String comment;
    private String user;
    private String emailId;
    private List<ModifiedFile> modifiedFiles;
    private boolean isMergeCommit;


    public Revision(String revision, Date timestamp, String comment, String user, String emailId, List<ModifiedFile> modifiedFiles) {
        this.revision = revision;
        this.timestamp = timestamp;
        this.comment = comment;
        this.user = user;
        this.emailId = emailId;
        this.modifiedFiles = modifiedFiles == null ? new ArrayList<>() : modifiedFiles;
    }

    public Revision(String revision, Date date, String comments, String user, String email) {
        this(revision, date, comments, user, email, null);
    }

    public final ModifiedFile createModifiedFile(String filename, String action) {
        ModifiedFile file = new ModifiedFile(filename, action);
        modifiedFiles.add(file);
        return file;
    }

}
