resource "aws_iam_role" "amplify" {
  count              = var.enable_cloud ? 1 : 0
  name               = "${var.project_name}-amplify"
  assume_role_policy = jsonencode({ Version = "2012-10-17", Statement = [{ Effect = "Allow", Principal = { Service = "amplify.amazonaws.com" }, Action = "sts:AssumeRole" }] })
}
resource "aws_iam_role_policy" "logs" {
  count  = var.enable_cloud ? 1 : 0
  name   = "build-logs"
  role   = aws_iam_role.amplify[0].id
  policy = jsonencode({ Version = "2012-10-17", Statement = [{ Effect = "Allow", Action = ["logs:CreateLogGroup", "logs:CreateLogStream", "logs:PutLogEvents"], Resource = "arn:aws:logs:*:*:log-group:/aws/amplify/*" }] })
}
resource "aws_amplify_app" "web" {
  count                 = var.enable_cloud ? 1 : 0
  name                  = var.project_name
  platform              = "WEB_COMPUTE"
  iam_service_role_arn  = aws_iam_role.amplify[0].arn
  build_spec            = file("${path.module}/../amplify.yml")
  environment_variables = { AMPLIFY_MONOREPO_APP_ROOT = "web", OPENAI_ENABLED = "false" }
  # Connect your repository via the Amplify Console GitHub App flow after creation.
}
resource "aws_amplify_branch" "main" {
  count             = var.enable_cloud ? 1 : 0
  app_id            = aws_amplify_app.web[0].id
  branch_name       = var.branch_name
  framework         = "Next.js - SSR"
  stage             = "DEVELOPMENT"
  enable_auto_build = true
}
locals {
  database_enabled = var.enable_cloud && var.enable_database
}
data "aws_availability_zones" "available" {
  count = local.database_enabled ? 1 : 0
  state = "available"
}
resource "aws_vpc" "data" {
  count                = local.database_enabled ? 1 : 0
  cidr_block           = "10.42.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true
}
resource "aws_subnet" "private" {
  count                   = local.database_enabled ? 2 : 0
  vpc_id                  = aws_vpc.data[0].id
  cidr_block              = cidrsubnet("10.42.0.0/16", 8, count.index)
  availability_zone       = data.aws_availability_zones.available[0].names[count.index]
  map_public_ip_on_launch = false
}
resource "aws_db_subnet_group" "data" {
  count      = local.database_enabled ? 1 : 0
  name       = "${var.project_name}-private"
  subnet_ids = aws_subnet.private[*].id
}
resource "aws_security_group" "database" {
  count       = local.database_enabled ? 1 : 0
  name_prefix = "${var.project_name}-db-"
  description = "No ingress; connect only a trusted future VPC backend."
  vpc_id      = aws_vpc.data[0].id
}
resource "aws_db_instance" "postgres" {
  count                       = local.database_enabled ? 1 : 0
  identifier                  = var.project_name
  engine                      = "postgres"
  instance_class              = "db.t4g.micro"
  allocated_storage           = 20
  storage_encrypted           = true
  db_name                     = "ubuntubiz"
  username                    = "ubuntubiz_admin"
  manage_master_user_password = true
  publicly_accessible         = false
  db_subnet_group_name        = aws_db_subnet_group.data[0].name
  vpc_security_group_ids      = [aws_security_group.database[0].id]
  backup_retention_period     = 7
  deletion_protection         = true
  skip_final_snapshot         = false
  final_snapshot_identifier   = "${var.project_name}-final"
  auto_minor_version_upgrade  = true
}
