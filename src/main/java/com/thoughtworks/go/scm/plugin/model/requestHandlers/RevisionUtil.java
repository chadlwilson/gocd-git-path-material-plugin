package com.thoughtworks.go.scm.plugin.model.requestHandlers;

import com.thoughtworks.go.scm.plugin.model.ModifiedFile;
import com.thoughtworks.go.scm.plugin.model.Revision;
import com.thoughtworks.go.scm.plugin.util.ListUtils;
import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class RevisionUtil {
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public Map<String, Object> toMap(Revision revision) {
        Map<String, Object> response = new HashMap<>();
        response.put("revision", revision.getRevision());
        response.put("timestamp", new SimpleDateFormat(DATE_PATTERN).format(revision.getTimestamp()));
        response.put("user", revision.getUser());
        response.put("revisionComment", revision.getComment());
        List<Map> modifiedFilesMapList = new ArrayList<>();

        if (!ListUtils.isEmpty(revision.getModifiedFiles())) {
            for (ModifiedFile modifiedFile : revision.getModifiedFiles()) {
                Map<String, String> modifiedFileMap = new HashMap<>();
                modifiedFileMap.put("fileName", modifiedFile.getFileName());
                modifiedFileMap.put("action", modifiedFile.getAction());
                modifiedFilesMapList.add(modifiedFileMap);
            }
        }
        response.put("modifiedFiles", modifiedFilesMapList);
        return response;
    }
}
