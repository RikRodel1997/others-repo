terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 5.0.0"
    }
  }
}

provider "aws" {
  profile = "rik"
  region  = "eu-central-1"
}

module "src" {
  source = "./src"

}
