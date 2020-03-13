node {
    stage("Checkout") {
        deleteDir()
        checkout scm
    }

    stage("set application.properties") {
        sh 'echo ${GIT_BRANCH}'
        sh 'echo $GIT_BRANCH'
        sh 'echo ${env.BRANCH_NAME}'

        build job: 'Set application.properties', parameters: [[$class: 'StringParameterValue', name: 'BRANCH', value: env.BRANCH_NAME]]
    }

    stage("test") {
         sh "mvn clean test"
    }
}