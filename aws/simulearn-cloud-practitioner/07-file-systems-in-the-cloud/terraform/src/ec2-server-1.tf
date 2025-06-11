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

resource "aws_security_group" "web-server-sg" {
  name   = "web-server-sg"
  vpc_id = data.aws_vpc.default.id
}

resource "aws_vpc_security_group_egress_rule" "web-sg-egress" {
  from_port         = 0
  to_port           = 0
  ip_protocol       = "-1"
  cidr_ipv4         = "0.0.0.0/0"
  description       = "Enable access to the internet"
  security_group_id = aws_security_group.web-server-sg.id

}

resource "aws_iam_role" "web-role" {
  name = "pet-web-iam-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_instance_profile" "pet-web-instance-profile" {
  name = "pet-web-instance-profile"
  role = aws_iam_role.web-role.name
}

resource "aws_iam_role_policy_attachment" "this" {
  role       = aws_iam_role.web-role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_instance" "pet-web-01" {
  ami                  = data.aws_ami.amazon-linux.id
  instance_type        = "t2.micro"
  subnet_id            = "subnet-046145ceb0178bd0e" // subnet in eu-central-1a
  iam_instance_profile = aws_iam_instance_profile.pet-web-instance-profile.name

  user_data = <<-EOF
              #!/bin/bash
              set -xe
              yum install -y amazon-efs-utils
              mkdir -p /data
              mount -t efs -o tls ${aws_efs_file_system.pet-efs.id}:/ /data
              cd /data
              echo "efs-l mounted in site A" >> efs-l-setup.log
              EOF

  tags = {
    Name = "pet-web-01"
  }

  depends_on = [aws_security_group.web-server-sg, aws_efs_file_system.pet-efs]
}
