node {
    stage("Checkout") {
        deleteDir()
        checkout scm
    }

    stage("set application.properties") {
        build job: 'Set application.properties', parameters: [[$class: 'StringParameterValue', name: 'BRANCH', value: ${BRANCH_NAME}]]
    }

    stage("test") {
         sh "mvn clean test"
    }
}