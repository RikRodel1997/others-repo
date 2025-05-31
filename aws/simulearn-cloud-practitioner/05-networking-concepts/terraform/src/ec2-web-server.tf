resource "aws_instance" "web-server" {
  ami                         = data.aws_ami.amazon-linux.id
  instance_type               = "t3.micro"
  subnet_id                   = var.subnet-id
  vpc_security_group_ids      = [aws_security_group.web-server.id]
  associate_public_ip_address = true

  tags = {
    Name = "Web Server"
  }
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

data "aws_vpc" "default" {
  default = true
}


resource "aws_security_group" "web-server" {
  name        = "web-server-sg"
  description = "Allow HTTP to web server"
  vpc_id      = data.aws_vpc.default.id
}

resource "aws_vpc_security_group_egress_rule" "web-server" {
  security_group_id = aws_security_group.web-server.id
  ip_protocol       = "-1"
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 3306
  to_port           = 3306
}

resource "aws_vpc_security_group_ingress_rule" "web-server" {
  security_group_id = aws_security_group.web-server.id
  ip_protocol       = "tcp"
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 80
  to_port           = 80
}
