# SampleCodeProject
AWS Java SDK Sample Code Project

### summary
- `LocalstackS3Test.java`
  - upload object
  - download object
  - rename object
  - remove object
- `LocalstackDynamoDBTest.java`
  - print table list
  - put item
  - get item by key
  - get item by query
  - remove item
- `LocalstackSnsTest.java`
  - send message to sns
- `LocalstackSqsTest.java`
  - receive message from sqs 

### environment
```shell
$ brew install awscli
```

```shell
$ aws --version
```

```shell
$ aws configure --profile default
AWS Access Key ID [None] : test-access-key
AWS Secret Access Key [None] : test-secret-key
Default region name [None] : ap-northeast-2
Default output format [None] : json
```

### how to use
- run localstack with docker
```shell
$ cd localstack
$ docker-compose up -d 
```
- check localstack with aws-cli
```shell
$ aws s3 ls --endpoint-url=http://localhost:4566
```

```shell
$ aws dynamodb list-tables --endpoint-url=http://localhost:4566 --region ap-northeast-2
```

```shell
$ aws sns list-topics --endpoint-url=http://localhost:4566 --region=ap-northeast-2
```

```shell
$ aws sqs list-queues --endpoint-url=http://localhost:4566 --region=ap-northeast-2
```

```shell
$ aws sns list-subscriptions --endpoint-url=http://localhost:4566 --region=ap-northeast-2
```

- execute test code
  - aws s3 test : `LocalstackS3Test.java`
  - aws dynamodb test : `LocalstackDynamoDBTest.java`
  - aws sns test : `LocalstackSnsTest.java`
  - aws sqs test : `LocalstackSqsTest.java`
