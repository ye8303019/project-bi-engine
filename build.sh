#!/bin/bash

export BUILD_REVERSION=${BUILD_REVERSION:-UNKNOWN}
export BUILD_NUMBER=${BUILD_NUMBER:-UNKNOWN}

echo "BUILD_NUMBER: $BUILD_NUMBER"
echo "BUILD_REVERSION: $BUILD_REVERSION"
echo "PATH: $PATH"
echo "DYNAMODB_URI: $DYNAMODB_URI"

die() {
   err=$?
   echo "Failed with exit code $err." >&2
   exit $err
}

build() {
   #
   ### Step 1: Build
   #
   echo "================== Building Service =================="
   mvn clean test -P !cq -Dapp.build.number=${BUILD_NUMBER} -Dapp.build.reversion=${BUILD_REVERSION} -Ddynamodb.uri=${DYNAMODB_URI} || die
}

publish() {
   #
   ### Step 2: Publish to Nexus & Local DTR
   echo "================== Publish Service Packages to Nexus and Local DTR =================="
   mvn deploy -P docker,!cq -Dmaven.test.skip=true -Ddb.migration.skip=true -Dapp.build.number=${BUILD_NUMBER} -Dapp.build.reversion=${BUILD_REVERSION} || die

   ### Step 3: Sonar
   echo "================== Sonar for Service Docs =================="
   mvn sonar:sonar -Dapp.build.number=${BUILD_NUMBER} -Dapp.build.reversion=${BUILD_REVERSION}
}

case $1 in
   build)
      shift
      build
      ;;
   publish)
      shift
      publish
      ;;
   help|*)
      echo "Usage: ./build.sh [COMMAND]"
      echo "COMMANDS"
      echo ""
      echo "   build     Exec clean, compile and unit test."
      echo "   publish   Exec publish to nexus and sonar."
      echo "   help      Show command help."
      ;;
esac