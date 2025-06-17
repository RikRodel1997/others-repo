# create simple ec2 instance
# use image from earlier made ec2 instance
# create launch template from image with t2.micro instance
data "aws_ami" "amazon-linux" {
  most_recent = true

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["137112412989"] # Amazon
}

resource "aws_instance" "server" {
  ami                         = data.aws_ami.amazon-linux.id
  instance_type               = "t3.micro"
  associate_public_ip_address = true
}

resource "aws_launch_template" "launch-template" {
  name          = "ServerLaunchTemplate"
  instance_type = "t2.micro"
  image_id      = data.aws_ami.amazon-linux.id
}
