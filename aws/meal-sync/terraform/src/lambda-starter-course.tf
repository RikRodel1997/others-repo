data "archive_file" "starter-zip" {
  source_dir  = "../lambdas/starter-course/src"
  output_path = "src/build/starter-course.zip"
  type        = "zip"
}

resource "aws_lambda_function" "starter-course" {
  filename      = "src/build/starter-course.zip"
  function_name = "starter-course"
  role          = aws_iam_role.lambda-iam.arn
  handler       = "index.handler"
  description   = "Lambda that sends a starter course event per table."

  runtime = "nodejs22.x"
}
