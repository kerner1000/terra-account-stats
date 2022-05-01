package com.github.kerner1000.terra.bot;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id="build-info")
public class AboutEndpoint {

    private final BuildProperties buildProperties;

    public AboutEndpoint(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @ReadOperation
    public BuildProperties getBuildProperties() {
        return buildProperties;
    }
}
