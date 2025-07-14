variable "subnets" {
  description = "Subnets that an ALB can target"
  type        = list(string)
  default     = [""]
}

variable "vpc_id" {
  description = "VPC ID"
  type        = string
  default     = ""
}
