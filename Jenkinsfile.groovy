pipeline{
    agent any
    stages {
        stage('Checkout from Git'){
            steps{
                git 'https://github.com/MV2322/projecteks-repo.git'
            }
        }
        stage('Terraform version'){
             steps{
                 sh 'terraform --version'
             }
        }
        stage('Terraform init'){
             steps{
                 dir('eks-tf') {
                      sh 'terraform init'
                   }
             }
        }
        stage('Terraform validate'){
             steps{
                 dir('eks-tf') {
                      sh 'terraform validate'
                   }
             }
        }
        stage('Terraform plan'){
             steps{
                 dir('eks-tf') {
                      sh 'terraform plan'
                   }
             }
        }
        stage('Terraform apply/destroy'){
             steps{
                 dir('eks-tf') {
                      sh 'terraform apply --auto-approve'
                   }
             }
        }
        stage('Build Docker Image'){
             steps{
                  sh 'docker build -t dhiv123/myweb:0.0.2 .'
             }  
        }
        stage('Docker Image Push'){
             steps{
               withCredentials([string(credentialsId: 'dockerPass', variable: 'dockerPassword')]) {
               sh "docker login -u dhiv123 -p ${dockerPassword}"
              }
              sh 'docker push dhiv123/myweb:0.0.2'
             }
        }
        stage('Deploy'){
             steps{
                 sh 'aws eks update-kubeconfig --name EKS_CLOUD --region us-east-2'
             }
        }
        stage('Deploy to Eks'){
             steps{
                 sh 'kubectl apply -f pod.yaml'
                 sh 'kubectl get pods'
                 sh 'kubectl get svc'
             }
        }
    }
}