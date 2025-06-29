terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 6.0.0"
    }
  }
}

provider "aws" {
  profile = "aws-cli"
  region  = "eu-central-1"
}

module "src" {
  source = "./src"
}
