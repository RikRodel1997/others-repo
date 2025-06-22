# Setup for all AZ's in eu-central-1 with public subnets
resource "aws_lb" "web-alb" {
  name               = "WebALB"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = var.subnets

  enable_deletion_protection = false
}

resource "aws_lb_target_group" "web-alb-tg" {
  name     = "WebAlbTargetGroup"
  port     = 80
  protocol = "HTTP"
  health_check {
    protocol            = "HTTP"
    path                = "/health"
    timeout             = 2
    unhealthy_threshold = 2
    interval            = 5
    matcher             = 200
  }
  vpc_id = data.aws_vpc.default.id
}

resource "aws_security_group" "alb" {
  name        = "ApplicationLoadBalancerGroup"
  description = "Allows HTTP traffic via the load balancer"
  vpc_id      = data.aws_vpc.default.id
}

resource "aws_vpc_security_group_ingress_rule" "alb-http-ingress" {
  security_group_id = aws_security_group.alb.id
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 80
  ip_protocol       = "tcp"
  to_port           = 80
}

resource "aws_vpc_security_group_egress_rule" "alb-ec2-egress" {
  security_group_id            = aws_security_group.alb.id
  referenced_security_group_id = aws_security_group.web-sg.id
  from_port                    = 80
  ip_protocol                  = "tcp"
  to_port                      = 80
}
