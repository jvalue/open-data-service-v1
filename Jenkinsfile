#!/usr/bin/env groovy

import java.text.SimpleDateFormat

def buildNumber = BUILD_NUMBER
def buildDateStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date())
def buildTag = "${buildDateStr}-${buildNumber}"

println "\n Job name: ${JOB_NAME}\n Build date: ${buildDateStr}\n Build number: ${buildNumber}\n Build tag: ${buildTag}"

pipeline {
    agent {
        label "ods-dev"
    }

    environment {
        // Creates ODS_DOCKER_USR, ODS_DOCKER_PSW implicitly
		ODS_DOCKER = credentials('ods-docker')
	}

    stages {
        stage('Commit Stage') {
            steps {
                sh "docker login -u $ODS_DOCKER_USR -p $ODS_DOCKER_PSW https://mojo-docker.cs.fau.de"
                sh "./gradlew clean build"
            }
        }

        stage('Acceptance Stage: Copy ods-configuration.yml') {
            steps {
                configFileProvider(
                        [configFile(fileId: '18c949a9-f9a1-4cab-91de-ccb4af376ec2', variable: 'ODS_CONFIG')]) {
                    sh "cat $ODS_CONFIG > server/ods-configuration.yml"
                }
            }
        }

        stage('Acceptance Stage: Unit Tests') {
            steps {
                sh "./gradlew test"
            }
        }

        stage('Acceptance Stage: Build Docker Image') {
            steps {
                sh "./gradlew dockerBuild"
            }
        }

        stage('Acceptance Stage') {
            steps {
                parallel(
                        'Run ODS Container': {
                            sh "docker-compose -f docker/docker-compose.yml -f docker/docker-compose.local.yml up -d"
                        },
                        'Run Integration Tests': {
                            timeout(time: 5, unit: "MINUTES") {
                                echo "Waiting until ODS service is ready."
                                waitUntil {
                                    script {
                                        try {
                                            sleep 3
                                            sh 'wget -q http://localhost:8080/ods/api/v1/version -O /dev/null'
                                            return true
                                        } catch (exception) {
                                            return false
                                        }
                                    }
                                }
                            }

                            sh "./gradlew integrationTest"
                        }
                )
            }
        }

        stage('Release Stage: Push Docker Image') {
            steps {
                sh "./gradlew dockerPush"
            }
        }

    }

    post {
        always {
            sh "docker-compose -f docker/docker-compose.yml -f docker/docker-compose.local.yml stop"
            deleteDir()
        }
    }
}

