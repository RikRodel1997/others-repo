resource "aws_launch_template" "server-launch-template" {
  name_prefix   = "server-launch-conf"
  image_id      = data.aws_ami.amazon-linux.id
  instance_type = "t2.micro"
}

resource "aws_autoscaling_group" "server-scaling" {
  availability_zones = ["eu-central-1a", "eu-central-1b", "eu-central-1c"]
  desired_capacity   = 2
  min_size           = 1
  max_size           = 2

  launch_template {
    id      = aws_launch_template.server-launch-template.id
    version = "$Latest"
  }
}
