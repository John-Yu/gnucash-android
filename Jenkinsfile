pipeline {
    agent { docker { image 'maven:3.3.3' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        },
        stage('Test') {
            steps {
                sh './gradlew check'
            }
        }
    },
    post {
        always {
            junit 'build/reports/**/*.xml'
        }
    }
}