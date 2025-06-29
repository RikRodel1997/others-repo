data "archive_file" "main-zip" {
  source_dir  = "../lambdas/main-course/src"
  output_path = "src/build/main-course.zip"
  type        = "zip"
}

resource "aws_lambda_function" "main-course" {
  filename      = "src/build/main-course.zip"
  function_name = "main-course"
  role          = aws_iam_role.lambda-iam.arn
  handler       = "index.handler"
  description   = "Lambda that sends a main course event per table."

  runtime = "nodejs22.x"
}
