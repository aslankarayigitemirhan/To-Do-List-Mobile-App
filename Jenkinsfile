pipeline {
    agent any // Or specify a particular agent with Docker installed
    triggers {
        // Requires the GitHub Integration Plugin
        githubPush()
    }
    environment {
        AWS_ACCOUNT_ID = '194722421717' // Replace with your AWS Account ID
        AWS_REGION = 'eu-north-1'         // Replace with your AWS Region (e.g., us-east-1)
        ECR_REPOSITORY_NAME = '12factor_todo' // Replace with your ECR repository name
        DOCKER_IMAGE_NAME = "${ECR_REPOSITORY_NAME}" // Or a more specific image name if needed
        // Optional: Define a tag, e.g., using the build number or git commit
        IMAGE_TAG = "latest-${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/main']], // Specify your branch
                    userRemoteConfigs: [[
                        url: 'https://github.com/ByTengaii/Cloud-12Factor-todo.git',
                        credentialsId: 'github-jenkins' // Your Jenkins credentials ID
                    ]]
                ])
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                cd todolist
                pwd
                ls -la
                docker build -t ${DOCKER_IMAGE_NAME}:${IMAGE_TAG} .
                echo "Docker image built: ${DOCKER_IMAGE_NAME}:${IMAGE_TAG}"
                '''
            }
        }

        stage('Push to Amazon ECR') {
            steps {
                
                script {
                    // Construct the full ECR image URI
                    def ecrImageFullName = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${DOCKER_IMAGE_NAME}:${IMAGE_TAG}"
                    def ecrImageLatestFullName = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${DOCKER_IMAGE_NAME}:latest"

                    // Use the Amazon ECR plugin for login
                    // The 'aws-ecr-credentials' should match the ID you gave your AWS credentials in Jenkins
                    // The AWS_REGION environment variable will be used by the plugin
                    docker.withRegistry("https://194722421717.dkr.ecr.eu-north-1.amazonaws.com/", "ecr:${AWS_REGION}:194722421717") {

                        // Tag the image with the full ECR URI
                        sh "docker tag ${DOCKER_IMAGE_NAME}:${IMAGE_TAG} ${ecrImageFullName}"
                        echo "Tagged image as ${ecrImageFullName}"

                        // Push the image
                        sh "docker push ${ecrImageFullName}"
                        echo "Successfully pushed ${ecrImageFullName} to ECR"

                        // Optionally, also tag and push as 'latest'
                        sh "docker tag ${DOCKER_IMAGE_NAME}:${IMAGE_TAG} ${ecrImageLatestFullName}"
                        sh "docker push ${ecrImageLatestFullName}"
                        echo "Successfully pushed ${ecrImageLatestFullName} to ECR as latest"
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean up local Docker images if needed (be careful with this)
            // sh "docker rmi ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${DOCKER_IMAGE_NAME}:${IMAGE_TAG}"
            // sh "docker rmi ${DOCKER_IMAGE_NAME}:${IMAGE_TAG}"
            echo "Pipeline finished."
        }
        success {
            echo "Pipeline succeeded!"
        }
        failure {
            echo "Pipeline failed."
        }
    }
}