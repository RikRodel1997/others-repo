terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 5.0.0"
    }
  }
}

provider "aws" {
  profile = "aws-cli"
  region  = "eu-central-1"
}

module "src" {
  source = "./src"
  subnets = [
    "subnet-046145ceb0178bd0e",
    "subnet-06a0b6ce6bfb86141",
    "subnet-0d9bccbab4e3d21ef"
  ]
}
