#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        sh "chmod +x gradlew"
        sh "./gradlew clean --no-daemon"
    }
    stage('nohttp') {
        sh "./gradlew checkstyleNohttp --no-daemon"
    }

    stage('npm install') {
        sh "./gradlew npm_install -PnodeInstall --no-daemon"
    }
    stage('Install Snyk CLI') {
       sh """
           curl -Lo ./snyk $(curl -s https://api.github.com/repos/snyk/snyk/releases/latest | grep "browser_download_url.*snyk-linux" | cut -d ':' -f 2,3 | tr -d \" | tr -d ' ')
           chmod +x snyk
       """
    }
    stage('Snyk test') {
       sh './snyk test --all-projects'
    }
    stage('Snyk monitor') {
       sh './snyk monitor --all-projects'
    }
    stage('backend tests') {
        try {
            sh "./gradlew test integrationTest -PnodeInstall --no-daemon"
        } catch(err) {
            throw err
        } finally {
            junit '**/build/**/TEST-*.xml'
        }
    }

    stage('frontend tests') {
        try {
            sh "./gradlew npm_run_test -PnodeInstall --no-daemon"
        } catch(err) {
            throw err
        } finally {
            junit '**/build/test-results/TESTS-*.xml'
        }
    }

    stage('packaging') {
        sh "./gradlew bootJar -x test -Pprod -PnodeInstall --no-daemon"
        archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
    }

    stage('deployment') {
        sh "./gradlew deployHeroku --no-daemon"
    }

    stage('quality analysis') {
        withSonarQubeEnv('sonar') {
            sh "./gradlew sonarqube --no-daemon"
        }
    }

    def dockerImage
    stage('publish docker') {
        // A pre-requisite to this step is to setup authentication to the docker registry
        // https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin#authentication-methods
        sh "./gradlew bootJar jib -Pprod -PnodeInstall --no-daemon"
    }
}
