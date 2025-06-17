resource "aws_iam_group" "support-engineers" {
  name = "SupportEngineers"
}

resource "aws_iam_group_policy_attachment" "ec2-read-only-attach" {
  group      = aws_iam_group.support-engineers.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ReadOnlyAccess"
}

resource "aws_iam_group_policy_attachment" "rds-read-only-attach" {
  group      = aws_iam_group.support-engineers.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonRDSReadOnlyAccess"
}

resource "aws_iam_user" "engineer-1" {
  name = "engineer-1"

  tags = {
    job-title = "SupportEngineer"
  }
}

resource "aws_iam_user_group_membership" "example1" {
  user = aws_iam_user.engineer-1.name

  groups = [
    aws_iam_group.support-engineers.name
  ]
}
