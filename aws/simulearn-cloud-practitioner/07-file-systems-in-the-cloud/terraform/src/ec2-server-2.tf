resource "aws_instance" "pet-web-02" {
  ami                  = data.aws_ami.amazon-linux.id
  instance_type        = "t2.micro"
  subnet_id            = "subnet-0d9bccbab4e3d21ef" // subnet in eu-central-1a
  iam_instance_profile = aws_iam_instance_profile.pet-web-instance-profile.name

  user_data = <<-EOF
              #!/bin/bash
              set -xe
              yum install -y amazon-efs-utils
              mkdir -p /data
              mount -t efs -o tls ${aws_efs_file_system.pet-efs.id}:/ /data
              cd /data
              echo "efs-l mounted in site B" >> efs-l-setup.log
              EOF

  tags = {
    Name = "pet-web-02"
  }

  depends_on = [aws_security_group.web-server-sg, aws_efs_file_system.pet-efs]
}
