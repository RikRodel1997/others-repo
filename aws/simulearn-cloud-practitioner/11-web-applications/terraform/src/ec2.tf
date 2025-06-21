# create new sg
# new ingress: HTTP | ALB SG
data "aws_vpc" "default" {
  default = true
}

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
  instance_type               = "t2.micro"
  associate_public_ip_address = true
}

resource "aws_launch_template" "launch-template" {
  name          = "ServerLaunchTemplate"
  instance_type = "t2.micro"
  image_id      = data.aws_ami.amazon-linux.id
}

resource "aws_security_group" "web-sg" {
  name        = "WebSecurityGroup"
  description = "Allow TLS inbound traffic and all outbound traffic"
  vpc_id      = data.aws_vpc.default.id
}

resource "aws_vpc_security_group_ingress_rule" "ec2-alb-ingress" {
  security_group_id            = aws_security_group.web-sg.id
  referenced_security_group_id = aws_security_group.alb.id
  from_port                    = 80
  ip_protocol                  = "tcp"
  to_port                      = 80
}
