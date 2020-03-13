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
         sh "mvn package -e -Dmaven.test.skip=true"
    }
}