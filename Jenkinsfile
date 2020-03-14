node {

    stage("Checkout") {
        deleteDir()
        checkout scm
    }

    stage("Set application.properties") {
        build job: 'Set application.properties', parameters: [[$class: 'StringParameterValue', name: 'TRIGGERED_BY_JOB', value: String.valueOf(JOB_NAME)]]
    }

    stage("JUnit") {
         sh "mvn clean test"
    }

    stage("Build") {
        if(String.valueOf(JOB_NAME).contains("master"){
            sh "mvn package -Pprod -Dmaven.test.skip=true"
        } else {
            sh "mvn package -Dmaven.test.skip=true"
        }
    }

    stage("Deploy") {
        build job: 'Deploy Water Tank', parameters: [[$class: 'StringParameterValue', name: 'TRIGGERED_BY_JOB', value: String.valueOf(JOB_NAME)]]
    }
}