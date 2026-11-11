output "amplify_app_id" {
  value = try(aws_amplify_app.web[0].id, null)
}
output "amplify_domain" {
  value = try(aws_amplify_app.web[0].default_domain, null)
}
output "database_endpoint" {
  value = try(aws_db_instance.postgres[0].endpoint, null)
}
output "database_secret_arn" {
  value     = try(aws_db_instance.postgres[0].master_user_secret[0].secret_arn, null)
  sensitive = true
}
