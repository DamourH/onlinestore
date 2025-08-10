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
        // Alternative method using npm (recommended)
        sh "npm install -g snyk"
        
        // Or if you prefer the direct download method:
        // sh '''
        //     SNYK_URL=$(curl -s https://api.github.com/repos/snyk/snyk/releases/latest | grep -E "browser_download_url.*snyk-macos(-arm64)?" | cut -d \'"\' -f 4)
        //     if [ -n "$SNYK_URL" ]; then
        //         curl -Lo ./snyk "$SNYK_URL"
        //         chmod +x ./snyk
        //     else
        //         echo "Failed to get Snyk download URL"
        //         exit 1
        //     fi
        // '''
    }
    
    stage('Snyk test') {
        sh 'snyk test --all-projects || true'  // Continue even if vulnerabilities found
    }
    
    stage('Snyk monitor') {
        sh 'snyk monitor --all-projects || true'
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
        sh "./gradlew bootJar jib -Pprod -PnodeInstall --no-daemon"
    }
}
