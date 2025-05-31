# Create the EC2 instance
resource "aws_instance" "db-server" {
  ami                         = data.aws_ami.amazon-linux.id
  instance_type               = "t3.micro"
  subnet_id                   = var.subnet-id
  vpc_security_group_ids      = [aws_security_group.db-server.id]
  associate_public_ip_address = true

  tags = {
    Name = "DB Server"
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


resource "aws_security_group" "db-server" {
  name        = "db-server-sg"
  description = "Allow HTTP to db server"
  vpc_id      = data.aws_vpc.default.id
}

resource "aws_vpc_security_group_ingress_rule" "db-server" {
  security_group_id = aws_security_group.db-server.id
  ip_protocol       = "tcp"
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 3306
  to_port           = 3306
}
