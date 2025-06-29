resource "aws_dynamodb_table" "meal-messages-table" {
  name           = "meal-messages"
  billing_mode   = "PROVISIONED"
  read_capacity  = 20
  write_capacity = 20
  hash_key       = "table"

  attribute {
    name = "table"
    type = "S"
  }

  stream_enabled   = true
  stream_view_type = "NEW_IMAGE"
}
