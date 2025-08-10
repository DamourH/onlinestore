#!/usr/bin/env groovy

pipeline {
    agent any

    environment {
        // Configure environment variables as needed
        HEROKU_APP_NAME = 'your-heroku-app-name'
        SONARQUBE_ENV = 'sonar'  // Name of your SonarQube environment configured in Jenkins
    }

    stages {
        stage('checkout') {
            steps {
                checkout scm
            }
        }

        stage('check java') {
            steps {
                sh "java -version"
            }
        }

        stage('clean') {
            steps {
                sh "chmod +x gradlew"
                sh "./gradlew clean --no-daemon"
            }
        }

        stage('nohttp') {
            steps {
                sh "./gradlew checkstyleNohttp --no-daemon"
            }
        }

        stage('npm install') {
            steps {
                sh "./gradlew npm_install -PnodeInstall --no-daemon"
            }
        }

        stage('Install Snyk CLI') {
            steps {
                script {
                    // Install Snyk CLI using npm (more reliable than direct download)
                    sh "npm install -g snyk"
                    
                    // Alternatively, you could use this direct download approach:
                    // def snykUrl = sh(script: "curl -s https://api.github.com/repos/snyk/snyk/releases/latest | grep -E 'browser_download_url.*snyk-macos(-arm64)?' | cut -d '\"' -f 4", returnStdout: true).trim()
                    // if (snykUrl) {
                    //     sh "curl -Lo ./snyk ${snykUrl}"
                    //     sh "chmod +x ./snyk"
                    // } else {
                    //     error "Failed to get Snyk download URL"
                    // }
                }
            }
        }

        stage('Snyk test') {
            steps {
                sh 'snyk test --all-projects || true'  // Continue even if vulnerabilities found
            }
        }

        stage('Snyk monitor') {
            steps {
                sh 'snyk monitor --all-projects || true'
            }
        }

        stage('backend tests') {
            steps {
                script {
                    try {
                        sh "./gradlew test integrationTest -PnodeInstall --no-daemon"
                    } catch(err) {
                        throw err
                    } finally {
                        junit '**/build/**/TEST-*.xml'
                    }
                }
            }
        }

        stage('frontend tests') {
            steps {
                script {
                    try {
                        sh "./gradlew npm_run_test -PnodeInstall --no-daemon"
                    } catch(err) {
                        throw err
                    } finally {
                        junit '**/build/test-results/TESTS-*.xml'
                    }
                }
            }
        }

        stage('packaging') {
            steps {
                sh "./gradlew bootJar -x test -Pprod -PnodeInstall --no-daemon"
                archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
            }
        }

        stage('deployment') {
            steps {
                script {
                    // Choose ONE of these deployment methods:

                    // 1. Heroku CLI JAR deployment (requires Heroku CLI installed on Jenkins)
                    // withCredentials([usernamePassword(credentialsId: 'heroku-credentials', usernameVariable: 'HEROKU_EMAIL', passwordVariable: 'HEROKU_API_KEY')]) {
                    //     sh "heroku deploy:jar build/libs/*.jar --app ${HEROKU_APP_NAME}"
                    // }

                    // 2. Git deployment to Heroku
                    // withCredentials([usernamePassword(credentialsId: 'heroku-credentials', usernameVariable: 'HEROKU_EMAIL', passwordVariable: 'HEROKU_API_KEY')]) {
                    //     sh "git remote add heroku https://heroku:${HEROKU_API_KEY}@git.heroku.com/${HEROKU_APP_NAME}.git"
                    //     sh "git push heroku HEAD:main"
                    // }

                    // 3. Container deployment (if using Jib)
                    // withCredentials([usernamePassword(credentialsId: 'heroku-credentials', usernameVariable: 'HEROKU_EMAIL', passwordVariable: 'HEROKU_API_KEY')]) {
                    //     sh "heroku container:login"
                    //     sh "./gradlew jibBuildTar --no-daemon"
                    //     sh "heroku container:push web --app ${HEROKU_APP_NAME}"
                    //     sh "heroku container:release web --app ${HEROKU_APP_NAME}"
                    // }

                    // For now, just echo since we don't have the task
                    echo "Deployment would happen here. Uncomment and configure your preferred Heroku deployment method."
                }
            }
        }

        stage('quality analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_ENV}") {
                    sh "./gradlew sonarqube --no-daemon"
                }
            }
        }

        stage('publish docker') {
            steps {
                // Using Jib to build and push Docker image
                sh "./gradlew bootJar jib -Pprod -PnodeInstall --no-daemon"
                
                // Alternative if using Docker directly:
                // script {
                //     docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                //         def customImage = docker.build("your-docker-image:${env.BUILD_ID}")
                //         customImage.push()
                //     }
                // }
            }
        }
    }

    post {
        always {
            // Clean up after build
            cleanWs()
        }
        success {
            // Notify success
            echo 'Pipeline completed successfully'
        }
        failure {
            // Notify failure
            echo 'Pipeline failed'
        }
    }
}
