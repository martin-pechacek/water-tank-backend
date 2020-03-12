node {
    stage("Checkout") {
        deleteDir()
        checkout scm
    }

    stage("test") {
         sh "mvn clean test"
    }
}