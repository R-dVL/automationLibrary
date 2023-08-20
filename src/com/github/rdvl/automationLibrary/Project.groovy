package com.github.rdvl.automationLibrary;

public class Project {
    private def pipeline
    private String name
    private String version
    private String artifactId
    private String url
    private def destination
    private String tech

    Project(pipeline, name, version) {
        this.pipeline = pipeline
        this.name = name
        this.version = version
        this.artifactId = name + "_" + version
        this.url = pipeline.cfg.projects."${name}".url
        this.destination = pipeline.cfg.projects."${name}".destination
        this.tech = pipeline.cfg.projects."${name}".tech
    }

    def prepare() {
        switch(tech) {
            case 'maven':
                pipeline.checkout(scm: [$class: 'GitSCM', userRemoteConfigs: [[url: url, credentialsId: 'github-token']], branches: [[name: version]]],poll: false)

                def mvnHome = pipeline.tool name: 'Maven 3.9.4', type: 'maven'
                def mvnCmd = "${mvnHome}/bin/mvn"

                pipeline.sh "${mvnCmd} clean package"
                break

            case 'node':
                break

            case 'react':
                break

            default:
                pipeline.error("${name} | Tech not defined.")
                break
        }
    }

    @NonCPS
    def getArtifactId() {
        return this.artifactId
    }

    @Override
    @NonCPS
    public String toString() {
        return """
            Name: ${name}
            Version: ${version}
            Artifact: ${artifactId}
            Url: ${url}
            Destination: ${destination}
            Tech: ${tech}
        """;
    }
}
