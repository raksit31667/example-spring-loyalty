awslocal kinesis create-stream --stream-name activity-performed --shard-count 1

awslocal dynamodb create-table \
--table-name example-spring-loyalty-lock \
--attribute-definitions AttributeName=lockKey,AttributeType=S AttributeName=sortKey,AttributeType=S \
--key-schema AttributeName=lockKey,KeyType=HASH AttributeName=sortKey,KeyType=RANGE \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
--tags Key=Owner,Value=localstack

awslocal dynamodb create-table \
--table-name example-spring-loyalty-checkpoint \
--attribute-definitions AttributeName=KEY,AttributeType=S \
--key-schema AttributeName=KEY,KeyType=HASH \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
--tags Key=Owner,Value=localstack

awslocal s3api create-bucket --bucket old-loyalty-transactions --region ap-southeast-1 --create-bucket-configuration LocationConstraint=ap-southeast-1
