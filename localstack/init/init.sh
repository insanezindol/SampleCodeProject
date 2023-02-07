#!/bin/sh

# s3 버킷 만들기
echo "Init localstack ::: s3"
awslocal s3 mb s3://test-bucket

# dynamodb 테이블 만들기
echo "Init localstack ::: dynamodb"
awslocal dynamodb create-table \
    --table-name user \
    --attribute-definitions \
        AttributeName=mallId,AttributeType=S \
        AttributeName=userId,AttributeType=S \
    --key-schema \
        AttributeName=mallId,KeyType=HASH \
        AttributeName=userId,KeyType=RANGE \
    --provisioned-throughput \
        ReadCapacityUnits=10,WriteCapacityUnits=5 \
    --region ap-northeast-2

# sns 토픽 만들기
echo "Init localstack ::: sns ::: create topics"
lunasoft_topic_arn=$(awslocal sns create-topic --name lunasoft-topic --region ap-northeast-2 --output text)
echo "$lunasoft_topic_arn"
gng_topic_arn=$(awslocal sns create-topic --name gng-topic --region ap-northeast-2 --output text)
echo "$gng_topic_arn"

# sqs 큐 만들기
echo "Init localstack ::: sqs ::: create queues"
dean_queue_url=$(awslocal sqs create-queue --queue-name dean-queue --region ap-northeast-2 --output text)
dean_queue_arn=$(awslocal sqs get-queue-attributes --queue-url "$dean_queue_url" --attribute-names QueueArn --output text | awk '{print $2}')
echo "$dean_queue_arn"
brian_queue_url=$(awslocal sqs create-queue --queue-name brian-queue --region ap-northeast-2 --output text)
brian_queue_arn=$(awslocal sqs get-queue-attributes --queue-url "$brian_queue_url" --attribute-names QueueArn --output text | awk '{print $2}')
echo "$brian_queue_arn"
brandon_queue_url=$(awslocal sqs create-queue --queue-name brandon-queue --region ap-northeast-2 --output text)
brandon_queue_arn=$(awslocal sqs get-queue-attributes --queue-url "$brandon_queue_url" --attribute-names QueueArn --output text | awk '{print $2}')
echo "$brandon_queue_arn"
jacob_queue_url=$(awslocal sqs create-queue --queue-name jacob-queue --region ap-northeast-2 --output text)
jacob_queue_arn=$(awslocal sqs get-queue-attributes --queue-url "$jacob_queue_url" --attribute-names QueueArn --output text | awk '{print $2}')
echo "$jacob_queue_arn"
neo_queue_url=$(awslocal sqs create-queue --queue-name neo-queue --region ap-northeast-2 --output text)
neo_queue_arn=$(awslocal sqs get-queue-attributes --queue-url "$neo_queue_url" --attribute-names QueueArn --output text | awk '{print $2}')
echo "$neo_queue_arn"
harvey_queue_url=$(awslocal sqs create-queue --queue-name harvey-queue --region ap-northeast-2 --output text)
harvey_queue_arn=$(awslocal sqs get-queue-attributes --queue-url "$harvey_queue_url" --attribute-names QueueArn --output text | awk '{print $2}')
echo "$harvey_queue_arn"

# sns 구독 설정하기
echo "Init localstack ::: create subscriptions"
subscription1_arn=$(awslocal sns subscribe --topic-arn "$lunasoft_topic_arn" --protocol sqs --notification-endpoint "$dean_queue_arn" --output text)
echo "$subscription1_arn"
subscription2_arn=$(awslocal sns subscribe --topic-arn "$lunasoft_topic_arn" --protocol sqs --notification-endpoint "$brian_queue_arn" --output text)
echo "$subscription2_arn"
subscription3_arn=$(awslocal sns subscribe --topic-arn "$lunasoft_topic_arn" --protocol sqs --notification-endpoint "$brandon_queue_arn" --output text)
echo "$subscription3_arn"
subscription4_arn=$(awslocal sns subscribe --topic-arn "$gng_topic_arn" --protocol sqs --notification-endpoint "$jacob_queue_arn" --output text)
echo "$subscription4_arn"
subscription5_arn=$(awslocal sns subscribe --topic-arn "$gng_topic_arn" --protocol sqs --notification-endpoint "$neo_queue_arn" --output text)
echo "$subscription5_arn"
subscription6_arn=$(awslocal sns subscribe --topic-arn "$gng_topic_arn" --protocol sqs --notification-endpoint "$harvey_queue_arn" --output text)
echo "$subscription6_arn"

# sns 구독 속성 설정하기
echo "Init localstack ::: set subscription attributes"
awslocal sns set-subscription-attributes --subscription-arn $subscription1_arn --attribute-name RawMessageDelivery --attribute-value true
awslocal sns set-subscription-attributes --subscription-arn $subscription2_arn --attribute-name RawMessageDelivery --attribute-value true
awslocal sns set-subscription-attributes --subscription-arn $subscription3_arn --attribute-name RawMessageDelivery --attribute-value true
awslocal sns set-subscription-attributes --subscription-arn $subscription4_arn --attribute-name RawMessageDelivery --attribute-value true
awslocal sns set-subscription-attributes --subscription-arn $subscription5_arn --attribute-name RawMessageDelivery --attribute-value true
awslocal sns set-subscription-attributes --subscription-arn $subscription6_arn --attribute-name RawMessageDelivery --attribute-value true
