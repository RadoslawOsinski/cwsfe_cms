 node {

    checkout scm

    //Properties for gradle are stored in ($JENKINS_HOME directory, cd ~/):
    //----->  /var/lib/jenkins/.gradle/gradle.properties

    stage('Build') {
        sh './gradlew --no-daemon wrapper'
        sh './gradlew --no-daemon war'
        sh './gradlew --no-daemon createTomcatWar'
    }
    stage('Integration testing') {
        sh './gradlew --no-daemon test integration_tests_local jacocoTestReport'
        [$class: 'JUnitResultArchiver', testResults: '**/build/test-results/test/TEST-*.xml']
        [$class: 'JUnitResultArchiver', testResults: '**/build/integration_tests_local/test/TEST-*.xml']
    }
    stage('SonarQube analysis') {
        sh './gradlew --no-daemon sonarqube'
    }
    stage('Archive results') {
        archiveArtifacts artifacts: '**/build/libs/*.jar,**/build/libs/*.war', fingerprint: true
    }
 }
