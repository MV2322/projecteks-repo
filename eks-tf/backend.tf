terraform {
  backend "s3" {
    bucket = "malar123" # Replace with your actual S3 bucket name
    key    = "EKS/terraform.tfstate"
    region = "us-east-2"
  }
}