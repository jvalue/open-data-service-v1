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

    stages {
        stage('Commit Stage') {
            steps {
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

        stage('Acceptance Stage: Start CouchDB Docker Container') {
            steps {
                sh "docker-compose -f docker/docker-compose.yml up -d couchdb"
                timeout(time: 2, unit: "MINUTES") {
                    echo "Waiting until couchdb is ready."
                    waitUntil {
                        script {
                            try {
                                sleep 1
                                sh 'wget -q http://localhost:5984/ -O /dev/null'
                                return true
                            } catch (exception) {
                                return false
                            }
                        }
                    }
                }
            }
        }

        stage('Acceptance Stage') {
            steps {
                parallel(
                        'run ods': {
                            sh "./gradlew run &"
                        },
                        'run integration tests': {
                            timeout(time: 2, unit: "MINUTES") {
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
            post {
                always {
                    sh "kill `pgrep -a java | grep \"GradleWrapperMain\" | awk '{print \$1}'`"
                }
            }

        }
    }

    post {
        always {
            sh "docker-compose -f docker/docker-compose.yml stop couchdb"
            deleteDir()
        }
    }
}

