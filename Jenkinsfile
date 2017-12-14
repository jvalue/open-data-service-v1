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
                sh "docker/couchdb/couchdb-start.sh"
            }
        }

        stage('Acceptance Stage') {
            steps {
                parallel(
                        'run ods': {
                            sh "./gradlew run &"
                        },
                        'run integration tests': {
                            sleep 45
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
            sh "docker/couchdb/couchdb-stop.sh"
            deleteDir()
        }
    }
}
