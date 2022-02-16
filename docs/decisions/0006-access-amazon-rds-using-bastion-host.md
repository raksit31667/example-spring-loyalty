# 6. Access Amazon RDS using Bastion host

Date: 2022-02-16

## Status

Accepted

## Context

We have an Amazon Relational Database Service (Amazon RDS) DB instance that is not publicly accessible. I would like to
connect to it from my Linux/macOS machine, where:

1. Database server will allow connection from local client only.
2. Attackers who already got credentials can't access local client.
3. Application connecting to a database is secure.
4. Leaked credentials is not being used by other services.

## Decision

We decided to improve these following security enhancements:

1. Create Amazon RDS with credentials from Amazon Secrets Manager.
2. Create a bastion host - launch the smallest available EC2 instance in the same VPC as our DB instance, accessible
   from internet with public subnets.
3. Create security group for bastion host with whitelist as public IP address of client
4. Create security group for Amazon RDS with whitelist as private IP address of bastion host
5. Add shell script for creating Linux user for client connection to bastion host by SSH

References: <https://aws.amazon.com/premiumsupport/knowledge-center/rds-connect-using-bastion-host-linux/>

## Consequences

Bastion host is considered as an additional one point-of-failure.
