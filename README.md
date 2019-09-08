Jenkins JIRA Plugin
===================

[![Build Status](https://ci.jenkins.io/buildStatus/icon?job=Plugins/jira-plugin/master)](https://ci.jenkins.io/blue/organizations/jenkins/Plugins%2Fjira-plugin/activity/)
[![Build Status](https://travis-ci.org/jenkinsci/jira-plugin.svg?branch=master)](https://travis-ci.org/jenkinsci/jira-plugin)
[![Coverage Status](https://coveralls.io/repos/jenkinsci/jira-plugin/badge.svg?branch=master&service=github)](https://coveralls.io/github/jenkinsci/jira-plugin?branch=master)

See [Wiki page](https://wiki.jenkins-ci.org/display/JENKINS/JIRA+Plugin) for more information.

Reported Issues:

* Next Release: 
[[ToDo]](https://issues.jenkins-ci.org/issues/?filter=14997)
[[Done]](https://issues.jenkins-ci.org/issues/?filter=14998)

* Bugs: [[All]](https://issues.jenkins-ci.org/issues/?filter=14761) [[Confirmed]](https://issues.jenkins-ci.org/issues/?filter=14996)

* Other: [[All Non-Bugs]](https://issues.jenkins-ci.org/issues/?filter=14762)
[[All Unresolved]](https://issues.jenkins-ci.org/issues/?filter=14956)
* Categorized:
[[by Votes]](https://issues.jenkins-ci.org/issues/?filter=15156)
[[by Priority]](https://issues.jenkins-ci.org/issues/?filter=15157)

### Building plugin with Docker

Build the plugin locally using Docker and Maven image version 3.3 & newest JDK 8:

    docker run -it --rm -v "$PWD":/usr/src/mymaven -v "$HOME/.m2:/usr/src/mymaven/.m2" -w /usr/src/mymaven maven:3.3-jdk-8 mvn clean package
    
### Atlassian sources import

To resolve some binary compatibility issues [JENKINS-48357](https://issues.jenkins-ci.org/browse/JENKINS-48357), 
the sources from the artifact [com.atlassian.httpclient:atlassian-httpclient-plugin:0.23](https://packages.atlassian.com/maven-external/com/atlassian/httpclient/atlassian-httpclient-plugin/0.23.0/)
has been imported in the project to have control over http(s) protocol transport layer. 
The downloaded sources didn't have any license headers but based on the [pom](https://packages.atlassian.com/maven-external/com/atlassian/httpclient/atlassian-httpclient-plugin/0.23.0/atlassian-httpclient-plugin-0.23.0.pom)
sources are Apache License (see pom in src/main/resources/atlassian-httpclient-plugin-0.23.0.pom)   

