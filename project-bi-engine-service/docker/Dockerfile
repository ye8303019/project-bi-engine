FROM        local-dtr.zhihuiya.com/buildenv/apm:Rel.1.0-5
MAINTAINER  PatSnap DevOps <devops@patsnap.com>

# Add app jar file and config file
COPY container_files /

ADD *.jar /opt/apps/project-bi-engine.jar

RUN sh -c 'touch /opt/apps/project-bi-engine.jar'