resource "aws_dynamodb_table" "user-video-history-table" {
  name           = "UserVideoHistory"
  hash_key       = "userId"
  range_key      = "lastDateWatched"
  read_capacity  = 1
  write_capacity = 1
  billing_mode   = "PROVISIONED"

  attribute {
    name = "userId"
    type = "S"
  }

  attribute {
    name = "lastDateWatched"
    type = "N"
  }
}

resource "aws_dynamodb_table_item" "example" {
  table_name = aws_dynamodb_table.user-video-history-table.name
  hash_key   = aws_dynamodb_table.user-video-history-table.hash_key
  range_key  = aws_dynamodb_table.user-video-history-table.range_key

  item = <<ITEM
    {
        "userId": {"S": "12345-abcd-6789"},
        "lastDateWatched": {"N": "1740086439"},
        "videoId": {"S": "9875-djac-1859"},
        "preferredLanguage": {"S": "en-US"},
        "supportedDeviceTypes": {"L": [{"S": "Amazon Fire TV"},{"S": "Amazon Fire Tablet"}]},
        "lastStopTime": {"N": "90"},
        "rating": {"N": "0"}
    }
    ITEM
}
