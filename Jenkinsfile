node {
    stage("Checkout") {
        deleteDir()
        checkout scm
    }

    stage("set application.properties") {

        sh 'printenv'

        build job: 'Set application.properties', parameters: [[$class: 'StringParameterValue', name: 'TRIGGERED_BY_JOB', value: ${JOB_NAME}]]
    }

    stage("test") {
         sh "mvn clean test"
    }
}