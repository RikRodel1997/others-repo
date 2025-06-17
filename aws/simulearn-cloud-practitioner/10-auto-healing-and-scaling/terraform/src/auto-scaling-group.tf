# create autoscaling group
# desired: 2 | min: 2 | max: 4
# auto scaling: Avg CPU usage | 70%
# scheduled scaling:
# desired: 3 | min: 3 | max: 4
# Every week from a date at 20:00 UTC

resource "aws_launch_template" "server-launch-template" {
  name_prefix   = "server-launch-conf"
  image_id      = data.aws_ami.amazon-linux.id
  instance_type = "t2.micro"
}

resource "aws_autoscaling_group" "server-scaling" {
  availability_zones = ["eu-central-1a"]
  desired_capacity   = 2
  max_size           = 4
  min_size           = 2

  launch_template {
    id      = aws_launch_template.server-launch-template.id
    version = "$Latest"
  }
}

resource "aws_autoscaling_schedule" "regular" {
  scheduled_action_name  = "RegularSchedule"
  min_size               = 3
  max_size               = 4
  desired_capacity       = 3
  start_time             = "2025-12-31T00:00:00Z"
  recurrence             = "0 0 * * *"
  autoscaling_group_name = aws_autoscaling_group.server-scaling.name
}

resource "aws_autoscaling_policy" "cpu-scaling" {
  name                   = "ServerScalingCpuPolicy"
  scaling_adjustment     = 1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300
  autoscaling_group_name = aws_autoscaling_group.server-scaling.name
}

resource "aws_cloudwatch_metric_alarm" "cpu-usage" {
  alarm_name          = "CpuUsageAlarm"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = "120"
  statistic           = "Average"
  threshold           = "70"
  alarm_description   = "This metric monitors ec2 cpu utilization"
  alarm_actions       = [aws_autoscaling_policy.cpu-scaling.arn]

  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.server-scaling.name
  }
}
