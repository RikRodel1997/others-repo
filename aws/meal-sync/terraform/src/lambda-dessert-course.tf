data "archive_file" "dessert-zip" {
  source_dir  = "../lambdas/dessert-course/src"
  output_path = "src/build/dessert-course.zip"
  type        = "zip"
}

resource "aws_lambda_function" "dessert-course" {
  filename         = "src/build/dessert-course.zip"
  source_code_hash = filebase64sha256("src/build/dessert-course.zip")
  function_name    = "dessert-course"
  role             = aws_iam_role.lambda-iam.arn
  handler          = "index.handler"
  description      = "Lambda that sends a dessert course event per table."

  runtime = "nodejs22.x"
}
