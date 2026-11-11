variable "aws_region" {
  type    = string
  default = "eu-west-1"
}
variable "project_name" {
  type    = string
  default = "ubuntubiz-ai"
}
variable "enable_cloud" {
  type        = bool
  default     = false
  description = "Opt in to billable AWS resources. Default creates nothing."
}
variable "enable_database" {
  type        = bool
  default     = false
  description = "Opt in to private RDS PostgreSQL; requires enable_cloud."
}
variable "branch_name" {
  type    = string
  default = "main"
}
