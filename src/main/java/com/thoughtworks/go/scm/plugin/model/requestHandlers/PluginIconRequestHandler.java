package com.thoughtworks.go.scm.plugin.model.requestHandlers;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.scm.plugin.util.JsonUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class PluginIconRequestHandler implements RequestHandler {

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest apiRequest) {
        try {
            byte[] contents = IOUtils.toByteArray(getClass().getResourceAsStream("/git-path.svg"));
            return JsonUtils.renderSuccessApiResponse(
                    Map.of("content_type", "image/svg+xml",
                            "data", Base64.getEncoder().encodeToString(contents)));
        } catch (IOException e) {
            return JsonUtils.renderErrorApiResponse(String.format("Could not load plugin icon due to %s", e.toString()));
        }
    }
}
