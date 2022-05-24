awslocal kinesis wait stream-exists --stream-name activity-performed

awslocal dynamodb wait table-exists --table-name example-spring-loyalty-lock
awslocal dynamodb wait table-exists --table-name example-spring-loyalty-checkpoint

awslocal s3api wait bucket-exists --bucket old-loyalty-transactions
