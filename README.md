# gocd-git-path-material-plugin

[![Build Status](https://travis-ci.org/TWChennai/gocd-git-path-material-plugin.svg?branch=master)](https://travis-ci.org/TWChennai/gocd-git-path-material-plugin) ![License](https://img.shields.io/github/license/TWChennai/gocd-git-path-material-plugin)

GoCD plugin to introduce a material that watches on a sub-directory of a Git repository.
 
This is the missing GoCD support for [mono-repos](https://developer.atlassian.com/blog/2015/10/monorepos-in-git/)

## What you can do with this plugin
...that you can't do with the built-in Git material:
* Support **mono-repos** without excessive triggering from a material that watches the entire repository
* Implement **side-channels** into deployment pipelines for source-controlled environment configuration
* Version control deployment configuration in the same repository as your code without rebuilding software every time
  unrelated configuration is changed; supporting [twelve-factor](https://12factor.net/) apps
* **Fix fan-in** - materials that monitor different paths in a repo are considered separate when [evaluating fan-in](https://docs.gocd.org/current/advanced_usage/fan_in.html) conditions
* Provide **clean, filtered change logs** for pipeline/VSM comparisons showing only changes on the monitored paths within a repository

This plugin is intended as 
* a **drop-in replacement** for the built-in GoCD Git material
* replaces the problematic use of **whitelists** and **blacklists**
  * whitelists and blacklists in GoCD tend to prevent/block fan-in because materials with different whitelists are still considered equivalent
  by the fan-in algorithm. This makes them unsuitable for use as environment side-channels or dealing with mono-repos.

### Advanced features

*Wildcard whitelists* - When the git command line is available on your agents; you can also use wildcards like `config/*/prod.yaml`. Anything that works with `git log` will work here.

*Shallow clones* - supported in the same way as the GoCD Git Material supports them

## Installation

**Manually**
* Download from [releases](https://github.com/TWChennai/gocd-git-path-material-plugin/releases/)
* Follow the installation instructions [here](https://docs.go.cd/current/extension_points/plugin_user_guide.html#installing-and-uninstalling-of-plugins)

**Dockerized GoCD automatic plugin install**
* Find [release](https://github.com/TWChennai/gocd-git-path-material-plugin/releases/) URL
* Set environment variable like the below; replacing `${VERSION}`
    ```
    GOCD_PLUGIN_INSTALL_gocd-git-path-material-plugin=https://github.com/TWChennai/gocd-git-path-material-plugin/releases/download/v${VERSION}/gocd-git-path-material-plugin-${VERSION}.jar
    ```

## Usage

### Via pipelines-as-code

From GoCD `19.2.0` onwards you can use via pluggable material configuration with [gocd-yaml-config-plugin](https://github.com/tomzo/gocd-yaml-config-plugin#pluggable)
```yaml
materials:
  path-filtered-material:
    plugin_configuration:
      id: git-path
    options:
      url: https://github.com/TWChennai/gocd-git-path-sample.git
      username: username # optional
      path: path1, path2/subpath
      shallow_clone: false # optional
    destination: destDir
```

### Via UI wizards

Available from GoCD `19.8.0` onwards.

![gocd-git-path-material-create-pipeline-wizard](docs/create-pipeline-wizard.png)

![gocd-git-path-material-add-scm-1](docs/add-scm-1.png)
![gocd-git-path-material-add-scm-2](docs/add-scm-2.png)

### Via UI advanced configuration

Available on all compatible versions of GoCD.

![gocd-git-path-material-plugin-add](docs/add-material.png)

![gocd-git-path-material-plugin-popup](docs/gitmaterial-popup.png)

You can see a sample [here](samples/sample-pipelines.gocd.yaml).

### Visualising changes

VSM view
![gocd-git-path-material-plugin-view-vsm](docs/view-vsm.png)

View changes grouped by material
![gocd-git-path-material-plugin-view-comparison](docs/view-comparison.png)

### Known issues

*Creating new pipelines via UI on pre `19.8.0` GoCD versions*

You will *not* be able to see *Git Path* material as a material type when creating a new pipeline. 
Add a dummy git material, then edit to add a new *Git Path* material and remove the old one. This problem
has been resolved in newer GoCD versions via the pipeline creation wizard.

*Stale data in agent repository clones*

This plugin uses Git commands to filter the history and determine the most recent revision that matches the
monitored paths. This means that changes that are not monitored in your paths may be "stale". The plugin does
not `rm` un-monitored paths from a clone; meaning your build task could accidentally be depending on files in the
repository that are out-of-date.

Be careful with your repository structure to keep your monitored path expressions simple.

### Contributing

#### Build

execute the following command to build the plugin

```bash
./gradlew clean build
```

#### Docker

You can quickly test the plugin using Docker, ensure you have installed docker, refer [docker installation guide](https://www.docker.com/products/overview) for installing docker for different environments

Execute the following gradle task to start the go-server
```bash
./gradlew clean startGoCd
```

You can now access the [go-server via port 8153](http://localhost:8153)

##### reload

If you like to reload the go-server with new build run,
```bash
./gradlew clean restartGoCd
```

##### stop

You can stop the running docker instance with the following gradle task
```bash
./gradlew clean stopGoCd
```

#### Releasing

Released via the [gocd-plugin-gradle-task-helpers](https://github.com/gocd/gocd-plugin-gradle-task-helpers) onto
GitHub.

* Check the`gocdPlugin.pluginVersion` version in `build.gradle` and bump if necessary
* Tag and publish
    ```bash
    ./gradlew githubRelease                       
    PRERELEASE=no ./gradlew githubRelease
    ```
* Edit the release description on `GitHub` if necessary.