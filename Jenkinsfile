 node {

    checkout scm

    stage('Init gradle if not available') {
        sh './gradlew --no-daemon wrapper'
    }
    stage('Build') {
        sh './gradlew --no-daemon war'
        sh './gradlew --no-daemon createTomcatWar'
    }
    stage('Integration testing') {
        //sh './gradlew --no-daemon integration_tests_local'
    }
    stage('SonarQube analysis') {
        sh './gradlew --no-daemon sonarqube'
    }
    stage('Archive results') {
        archiveArtifacts artifacts: '**/build/libs/*.jar,**/build/libs/*.war', fingerprint: true
    }
 }