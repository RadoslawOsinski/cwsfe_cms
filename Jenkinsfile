 node {

    checkout scm

    stage('Init gradle if not available') {
        sh './gradlew wrapper'
    }
    stage('Build') {
        sh './gradlew war'
        sh './gradlew createTomcatWar'
    }
    stage('Integration testing') {
        sh './gradlew integration_tests_local'
    }
    //stage('SonarQube analysis') {
        // requires SonarQube Scanner 2.8+
        //def scannerHome = tool 'SonarQube Scanner 2.8';
        //withSonarQubeEnv('CWSFE_CMS_SONAR') {
        //            sh "${scannerHome}/bin/sonar-scanner"
        //}
        //withSonarQubeEnv('CWSFE_CMS_SONAR') {
            // requires SonarQube Scanner for Gradle 2.1+
          //  sh './gradlew sonarqube'
        //}
    //
    //}
    stage('Archive results') {
        archiveArtifacts artifacts: '**/build/libs/*.jar,**/build/libs/*.war', fingerprint: true
    }
 }
