#!/bin/bash

source token.sh
source gradle.properties

filename="linux-amd64-github-release.tar.bz2"
os="linux"

if [[ "$(uname)" == "Darwin" ]]; then
    filename="darwin-amd64-github-release.tar.bz2"
    os="darwin"
fi

if [[ ! -f  "./bin/${os}/amd64/github-release" ]]; then
    if [[ ! -f ${filename} ]]; then
        wget https://github.com/aktau/github-release/releases/download/v0.7.2/${filename} -O ${filename}
    fi
    tar xf ${filename}
fi

GIT_HUB_RELEASE_BIN="./bin/${os}/amd64/github-release"

echo "GitHub [${1}] of version ${version}"

case "$1" in
  release)
    ${GIT_HUB_RELEASE_BIN} release \
      --user chadlwilson \
      --repo gocd-git-path-material-plugin \
      --tag ${version} \
      --name ${version} \
      --pre-release;;
  upload)
    ${GIT_HUB_RELEASE_BIN} upload \
      --user chadlwilson \
      --repo gocd-git-path-material-plugin \
      --tag ${version} \
      --name "gocd-git-path-material-plugin-$version.jar" \
      --file build/libs/gocd-git-path-material-plugin-${version}.jar;;
  delete)
    ${GIT_HUB_RELEASE_BIN} delete \
      --user chadlwilson \
      --repo gocd-git-path-material-plugin \
      --tag ${version};;
  *)
    echo "usage release.sh release | upload | delete"
esac
