#!/bin/sh

sed -i "s|APP_JAVA_OPTS|${APP_JAVA_OPTS}|g"  /etc/supervisord.d/project-bi-engine.ini