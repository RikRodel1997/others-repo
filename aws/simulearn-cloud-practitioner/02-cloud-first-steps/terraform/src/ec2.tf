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

resource "aws_instance" "island-web" {
  ami                         = data.aws_ami.amazon-linux.id
  instance_type               = "t3.micro"
  user_data                   = file("../user-data.txt")
  subnet_id                   = var.subnet-id
  vpc_security_group_ids      = [aws_security_group.island-web-sg.id]
  associate_public_ip_address = true

  tags = {
    Name = "IslandStabilization01"
  }
}

resource "aws_security_group" "island-web-sg" {
  name        = "island-web-sg"
  description = "Allow HTTP to web server"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    description = "HTTP ingress"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
