package com.thoughtworks.go.scm.plugin.model.requestHandlers;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.scm.plugin.git.GitHelper;
import com.thoughtworks.go.scm.plugin.git.HelperFactory;
import com.thoughtworks.go.scm.plugin.model.GitConfig;
import com.thoughtworks.go.scm.plugin.model.Revision;
import com.thoughtworks.go.scm.plugin.util.JsonUtils;
import com.thoughtworks.go.scm.plugin.util.ListUtils;
import com.thoughtworks.go.scm.plugin.util.Validator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;

public class LatestRevisionSinceRequestHandler implements RequestHandler {
    private static Logger LOGGER = Logger.getLoggerFor(LatestRevisionSinceRequestHandler.class);

    @Override
    @SuppressWarnings("unchecked")
    public GoPluginApiResponse handle(GoPluginApiRequest apiRequest) {
        Map<String, Object> responseMap = (Map<String, Object>) JsonUtils.parseJSON(apiRequest.requestBody());

        GitConfig gitConfig = GitConfig.create(apiRequest);

        File flyweightFolder = new File((String) responseMap.get("flyweight-folder"));
        Map<String, Object> previousRevisionMap = (Map<String, Object>) responseMap.get("previous-revision");
        String previousRevision = (String) previousRevisionMap.get("revision");
        LOGGER.debug(String.format("flyweight: %s, previous commit: %s", flyweightFolder, previousRevision));

        Map<String, Object> fieldMap = new HashMap<>();
        Validator.validateUrl(gitConfig, fieldMap);
        if (!fieldMap.isEmpty()) {
            String message = (String) fieldMap.get("message");
            LOGGER.error(String.format("Invalid url: %s", message));
            return JsonUtils.renderErrrorApiResponse(message);
        }

        try {
            GitHelper git = HelperFactory.git(gitConfig, flyweightFolder);
            git.cloneOrFetch();
            Map<String, String> configuration = JsonUtils.parseScmConfiguration(apiRequest);
            List<Revision> newerRevisions = git.getRevisionsSince(previousRevision, configuration.get("path"));

            LOGGER.debug(String.format("Fetching newerRevisions for path %s", configuration.get("path")));

            if (ListUtils.isEmpty(newerRevisions)) {
                return JsonUtils.renderSuccessApiResponse(null);
            } else {
                LOGGER.debug(String.format("New commits: %s", newerRevisions.size()));
                return JsonUtils.renderSuccessApiResponse(
                        Map.of("revisions",
                                newerRevisions
                                        .stream()
                                        .map(Revision::getRevisionMap)
                                        .collect(toList())));
            }
        } catch (Throwable t) {
            LOGGER.error("get latest revisions since: ", t);
            return JsonUtils.renderErrrorApiResponse(getRootCauseMessage(t));
        }
    }
}
