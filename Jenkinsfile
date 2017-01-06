 node {

    checkout scm

    // Store the commit id for use in manifest. Current jenkins version does not support correct version
    sh 'git rev-parse --short HEAD > GIT_CUSTOM_READ_SHA'
    GIT_CUSTOM_READ_SHA = readFile('GIT_CUSTOM_READ_SHA').trim();

    //Properties for gradle are stored in ($JENKINS_HOME directory, cd ~/):
    //----->  /var/lib/jenkins/.gradle/gradle.properties

    stage('Init gradle if not available') {
        sh './gradlew --no-daemon wrapper'
    }
    stage('Build') {
        sh './gradlew --no-daemon war'
        sh './gradlew --no-daemon createTomcatWar'
    }
    stage('Integration testing') {
        sh './gradlew --no-daemon test integration_tests_local jacocoTestReport'
    }
    stage('SonarQube analysis') {
        sh './gradlew --no-daemon sonarqube'
    }
    stage('Archive results') {
        archiveArtifacts artifacts: '**/build/libs/*.jar,**/build/libs/*.war', fingerprint: true
    }
 }
