# MariaDB
# Template: Dev/Test
# Master username: admin
# Credentials mgt: Self
# master password: XXXXX
# Instance: burstable class | db.t3.micro
# Storage: gp3 | 20GB
# Enable autoscaling
# Max storage threshold 1000
# disable enhanced monitoring
# my-maria-db-name
# backups: enable automated | 7 days retention
# encryption: AWS KMS key: aws/rds

data "aws_vpc" "default" {
  default = true
}


resource "aws_db_instance" "my-mariadb-rds" {
  identifier                 = "main-mariadb-prod"
  allocated_storage          = 20
  engine                     = "mariadb"
  engine_version             = "11.4.5"
  instance_class             = "db.t3.micro"
  auto_minor_version_upgrade = false
  backup_retention_period    = 7
  multi_az                   = false
  username                   = "foo"
  password                   = "definitely-not-a-real-password"
  skip_final_snapshot        = true
  storage_encrypted          = true
  storage_type               = "gp3"
  vpc_security_group_ids     = [var.security-group-id]
  parameter_group_name       = aws_db_parameter_group.mariadb-parameter-group.name
}

resource "aws_db_instance" "my-mariadb-rds-read-replica" {
  identifier             = "main-mariadb-prod-read-replica"
  replicate_source_db    = aws_db_instance.my-mariadb-rds.identifier
  instance_class         = "db.t3.micro"
  apply_immediately      = true
  publicly_accessible    = false
  skip_final_snapshot    = true
  vpc_security_group_ids = [var.security-group-id]
  parameter_group_name   = aws_db_parameter_group.mariadb-parameter-group.name
}

resource "aws_db_parameter_group" "mariadb-parameter-group" {
  name   = "mariadb-rds"
  family = "mariadb11.4"
}
