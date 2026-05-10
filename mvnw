#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script, version 3.3.2
#
# Optional ENV vars
#   MVNW_REPOURL - repo url base for downloading maven-wrapper.jar
#   MVNW_USERNAME/MVNW_PASSWORD - user and password for downloading maven-wrapper.jar
#   MVNW_VERBOSE - true: enable verbose logging; debug: trace the script; others: silence the output
# ----------------------------------------------------------------------------

# OS specific support
cygwin=false
mingw=false
darwin=false
case "$(uname)" in
  CYGWIN*) cygwin=true ;;
  MINGW*)  mingw=true ;;
  Darwin*) darwin=true ;;
esac

# Resolve links - $0 may be a soft link
PRG="$0"
while [ -h "$PRG" ]; do
  ls=$(ls -ld "$PRG")
  link=$(expr "$ls" : '.*-> \(.*\)$')
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=$(dirname "$PRG")/"$link"
  fi
done

PRGDIR=$(dirname "$PRG")
BASEDIR=$(cd "$PRGDIR" && pwd)

# Reset the CDPATH
unset CDPATH

##########################################################################################
# Extension to allow automatically downloading the maven-wrapper.jar from Maven-central
# This allows using the maven wrapper in projects that prohibit checking in binary data.
##########################################################################################
wrapperJarPath="$BASEDIR/.mvn/wrapper/maven-wrapper.jar"
if [ -r "$BASEDIR/.mvn/wrapper/maven-wrapper.properties" ]; then
  if [ "$MVNW_VERBOSE" = true ]; then
    echo "Reading properties from $BASEDIR/.mvn/wrapper/maven-wrapper.properties"
  fi
  wrapperUrl=$(sed -n 's/^wrapperUrl=\(.*\)$/\1/p' "$BASEDIR/.mvn/wrapper/maven-wrapper.properties" | tr -d '\r')
fi
if [ -z "$wrapperUrl" ]; then
  wrapperUrl="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
fi
if [ -n "$MVNW_REPOURL" ]; then
  wrapperUrl="$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
fi

if [ ! -r "$wrapperJarPath" ]; then
  if [ "$MVNW_VERBOSE" = true ]; then
    echo "Couldn't find $wrapperJarPath, downloading it ..."
    echo "Downloading from: $wrapperUrl"
  fi

  if $cygwin; then
    wrapperJarPath=$(cygpath --path --windows "$wrapperJarPath")
  fi

  if command -v wget > /dev/null; then
    QUIET=""
    if [ "$MVNW_VERBOSE" != true ]; then
      QUIET="--quiet"
    fi
    if [ -z "$MVNW_USERNAME" ] || [ -z "$MVNW_PASSWORD" ]; then
      wget $QUIET "$wrapperUrl" -O "$wrapperJarPath" || rm -f "$wrapperJarPath"
    else
      wget $QUIET --http-user="$MVNW_USERNAME" --http-password="$MVNW_PASSWORD" "$wrapperUrl" -O "$wrapperJarPath" || rm -f "$wrapperJarPath"
    fi
  elif command -v curl > /dev/null; then
    QUIET=""
    if [ "$MVNW_VERBOSE" != true ]; then
      QUIET="--silent"
    fi
    if [ -z "$MVNW_USERNAME" ] || [ -z "$MVNW_PASSWORD" ]; then
      curl $QUIET -o "$wrapperJarPath" "$wrapperUrl" -f -L || rm -f "$wrapperJarPath"
    else
      curl $QUIET --user "$MVNW_USERNAME:$MVNW_PASSWORD" -o "$wrapperJarPath" "$wrapperUrl" -f -L || rm -f "$wrapperJarPath"
    fi
  else
    echo "Cannot download maven-wrapper.jar: wget or curl not found"
    exit 1
  fi

  if [ ! -r "$wrapperJarPath" ]; then
    echo "ERROR: could not download maven-wrapper.jar"
    exit 1
  fi

  if [ "$MVNW_VERBOSE" = true ]; then
    echo "Finished downloading $wrapperJarPath"
  fi
fi
##########################################################################################
# End of extension
##########################################################################################

# If specified, validate the SHA-256 sum of the Maven wrapper jar file
wrapperSha256Sum=""
while IFS="=" read -r key value; do
  case "$key" in (wrapperSha256Sum) wrapperSha256Sum=$value; break ;; esac
done < "$BASEDIR/.mvn/wrapper/maven-wrapper.properties"
if [ -n "$wrapperSha256Sum" ]; then
  wrapperSha256Result=false
  if command -v sha256sum > /dev/null; then
    if echo "$wrapperSha256Sum  $wrapperJarPath" | sha256sum -c > /dev/null 2>&1; then
      wrapperSha256Result=true
    fi
  elif command -v shasum > /dev/null; then
    if echo "$wrapperSha256Sum  $wrapperJarPath" | shasum -a 256 -c > /dev/null 2>&1; then
      wrapperSha256Result=true
    fi
  else
    echo "WARNING: Could not verify maven-wrapper.jar SHA-256 checksum: sha256sum or shasum not found"
  fi
  if [ $wrapperSha256Result = false ]; then
    echo "ERROR: Failed to validate Maven wrapper SHA-256, your Maven wrapper might be compromised." >&2
    echo "Investigate or delete $wrapperJarPath to attempt a clean download." >&2
    echo "If you updated your Maven version, you need to update the specified wrapperSha256Sum property." >&2
    exit 1
  fi
fi

MAVEN_OPTS="$(concat_lines "$MAVEN_PROJECTBASEDIR/.mvn/jvm.config") $MAVEN_OPTS"

# Find the java executable
if [ -n "$JAVA_HOME" ] ; then
  if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
    # IBM's JDK on AIX uses strange locations for the executables
    JAVACMD="$JAVA_HOME/jre/sh/java"
  else
    JAVACMD="$JAVA_HOME/bin/java"
  fi
  if [ ! -x "$JAVACMD" ] ; then
    echo "The JAVA_HOME environment variable is not defined correctly, >&2"
    echo "this environment variable is needed to run this program." >&2
    exit 1
  fi
else
  JAVACMD=$(command -v java)
  if [ ! -x "$JAVACMD" ] ; then
    echo "The java executable is not found in PATH, and JAVA_HOME is not set." >&2
    echo "Install Java or set JAVA_HOME to run this program." >&2
    exit 1
  fi
fi

if [ -z "$MAVEN_PROJECTBASEDIR" ]; then
  MAVEN_PROJECTBASEDIR="$BASEDIR"
  export MAVEN_PROJECTBASEDIR
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=$(cygpath --path --windows "$JAVA_HOME")
  [ -n "$CLASSPATH" ] && CLASSPATH=$(cygpath --path --windows "$CLASSPATH")
  [ -n "$MAVEN_PROJECTBASEDIR" ] && MAVEN_PROJECTBASEDIR=$(cygpath --path --windows "$MAVEN_PROJECTBASEDIR")
  wrapperJarPath=$(cygpath --path --windows "$wrapperJarPath")
fi

# Provide a "standardized" way to retrieve the CLI args that will
# temporary work around the entangled state
MAVEN_CMD_LINE_ARGS="$@"
export MAVEN_CMD_LINE_ARGS

WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

# shellcheck disable=SC2086
exec "$JAVACMD" \
  $JVM_CONFIG_MAVEN_PROPS \
  $MAVEN_OPTS \
  $MAVEN_DEBUG_OPTS \
  -classpath "$wrapperJarPath" \
  "-Dmaven.multiModuleProjectDirectory=${MAVEN_PROJECTBASEDIR}" \
  ${WRAPPER_LAUNCHER} "$@"
