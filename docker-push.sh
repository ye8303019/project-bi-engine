#!/bin/bash

export PROJECT_NAME=project-bi-engine-service

export RELEASE_VERSION=Dev.1.0.0-SNAPSHOT
export LOCAL_DTR_URL=local-dtr.zhihuiya.com
export CN_DTR_URL=docker-registry.patsnap.com
export JP_DTR_URL=docker-registry-jp.patsnap.com

docker tag ${LOCAL_DTR_URL}/platform/${PROJECT_NAME}:${RELEASE_VERSION} ${LOCAL_DTR_URL}/platform/${PROJECT_NAME}:${RELEASE_VERSION}-${BUILD_NUMBER}
docker push ${LOCAL_DTR_URL}/platform/${PROJECT_NAME}:${RELEASE_VERSION}-${BUILD_NUMBER}

#docker login -u dtr -p patsnapDTR ${CN_DTR_URL}
#docker tag ${LOCAL_DTR_URL}/patsnap/${PROJECT_NAME}-service:${RELEASE_VERSION} ${CN_DTR_URL}/patsnap/${PROJECT_NAME}:${RELEASE_VERSION}-${BUILD_NUMBER}
#docker push ${CN_DTR_URL}/patsnap/${PROJECT_NAME}:${RELEASE_VERSION}-${BUILD_NUMBER}
#
#docker login -u devops@patsnap.com -p patsnapADMIN2015 -e devops@patsnap.com ${JP_DTR_URL}
#docker tag ${LOCAL_DTR_URL}/patsnap/${PROJECT_NAME}-service:${RELEASE_VERSION} ${JP_DTR_URL}/patsnap/${PROJECT_NAME}:${RELEASE_VERSION}-${BUILD_NUMBER}
#docker push ${JP_DTR_URL}/patsnap/${PROJECT_NAME}:${RELEASE_VERSION}-${BUILD_NUMBER}