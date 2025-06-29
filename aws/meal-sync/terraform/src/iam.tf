resource "aws_iam_role" "lambda-iam" {
  name = "lambda-iam"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_policy" "eventbridge-put-policy" {
  name        = "EventBridgePutEvents"
  description = "Allow Lambda to put events on EventBridge"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect   = "Allow",
        Action   = "events:PutEvents",
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "lambda-eventbridge-attach" {
  role       = aws_iam_role.lambda-iam.name
  policy_arn = aws_iam_policy.eventbridge-put-policy.arn
}

resource "aws_iam_role_policy_attachment" "lambda-basic-execution-role" {
  role       = aws_iam_role.lambda-iam.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}
