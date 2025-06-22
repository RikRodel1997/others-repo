data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

resource "aws_efs_file_system" "pet-efs" {
  creation_token  = "pet-efs"
  throughput_mode = "bursting"
}

resource "aws_efs_mount_target" "pet-mt" {
  for_each        = toset(data.aws_subnets.default.ids)
  file_system_id  = aws_efs_file_system.pet-efs.id
  subnet_id       = each.key
  security_groups = [aws_security_group.efs-sg.id]
}


resource "aws_security_group" "efs-sg" {
  name   = "efs-sg"
  vpc_id = data.aws_vpc.default.id
}

resource "aws_vpc_security_group_ingress_rule" "efs-sg-ingress" {
  security_group_id            = aws_security_group.efs-sg.id
  ip_protocol                  = "tcp"
  from_port                    = 2049
  to_port                      = 2049
  referenced_security_group_id = aws_security_group.web-server-sg.id
}
