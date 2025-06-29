data "archive_file" "meal-messages-transformer-zip" {
  source_dir  = "../lambdas/meal-messages-transformer/src"
  output_path = "src/build/meal-messages-transformer.zip"
  type        = "zip"
}

resource "aws_lambda_function" "meal-messages-transformer" {
  filename         = "src/build/meal-messages-transformer.zip"
  function_name    = "meal-messages-transformer"
  role             = aws_iam_role.lambda-iam.arn
  handler          = "index.handler"
  source_code_hash = filebase64sha256("src/build/meal-messages-transformer.zip")

  runtime = "nodejs22.x"

  environment {
    variables = {
      DYNAMO_DB_TABLE = aws_dynamodb_table.meal-messages-table.name
    }
  }
}

resource "aws_lambda_permission" "allow-eventbridge" {
  statement_id  = "AllowExecutionFromEventBridge"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.meal-messages-transformer.function_name
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.dessert-event.arn
}

resource "aws_iam_policy" "dynamodb-write-policy" {
  name = "dynamodb-write-access"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "dynamodb:PutItem",
          "dynamodb:UpdateItem",
          "dynamodb:GetItem",
        ],
        Effect   = "Allow",
        Resource = aws_dynamodb_table.meal-messages-table.arn
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach-dynamodb-write" {
  role       = aws_iam_role.lambda-iam.name
  policy_arn = aws_iam_policy.dynamodb-write-policy.arn
}
