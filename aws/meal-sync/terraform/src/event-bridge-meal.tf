resource "aws_cloudwatch_event_bus" "meal-bridge" {
  name        = "meal-bridge"
  description = "Event bus that receives meal events."
}

resource "aws_cloudwatch_event_rule" "dessert-event" {
  name           = "dessert-event-to-messages-transformer"
  description    = "Trigger meal-messages-transformer on dessert events"
  event_bus_name = "meal-bridge"
  event_pattern = jsonencode({
    "source" : ["dessert-course-lambda"],
    "detail-type" : ["dessert"]
  })
}

resource "aws_cloudwatch_event_target" "meal-messages-transformer" {
  rule           = aws_cloudwatch_event_rule.dessert-event.name
  event_bus_name = "meal-bridge"
  target_id      = "dessert-to-meal-messages-transformer"
  arn            = aws_lambda_function.meal-messages-transformer.arn
}
