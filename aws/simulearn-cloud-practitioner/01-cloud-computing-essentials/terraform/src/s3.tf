resource "aws_s3_bucket" "cloud-computing-essentials" {
  bucket = "cloud-computing-essentials-bucket"

  tags = {
    Name        = "cloud-computing-essentials-bucket"
    Environment = "dev"
  }
}

resource "aws_s3_object" "dist" {
  for_each = fileset("${var.static-dir}", "*")

  bucket = aws_s3_bucket.cloud-computing-essentials.bucket
  key    = each.value
  source = "${var.static-dir}${each.value}"
  etag   = filemd5("${var.static-dir}${each.value}")
}

resource "aws_s3_bucket_public_access_block" "example" {
  bucket = aws_s3_bucket.cloud-computing-essentials.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_bucket_policy" "allow_access_from_another_account" {
  bucket = aws_s3_bucket.cloud-computing-essentials.id
  policy = jsonencode(
    {
      "Version" : "2012-10-17",
      "Statement" : [
        {
          "Sid" : "S3GetObjectAllow",
          "Effect" : "Allow",
          "Principal" : "*",
          "Action" : "s3:GetObject",
          "Resource" : "arn:aws:s3:::cloud-computing-essentials-bucket/*"
        }
      ]
    }
  )
}

resource "aws_s3_bucket_website_configuration" "example" {
  bucket = aws_s3_bucket.cloud-computing-essentials.id

  index_document {
    suffix = "index.html"
  }

  error_document {
    key = "error.html"
  }

  routing_rule {
    condition {
      key_prefix_equals = "docs/"
    }
    redirect {
      replace_key_prefix_with = "documents/"
    }
  }
}
