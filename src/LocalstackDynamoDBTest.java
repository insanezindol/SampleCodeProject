import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LocalstackDynamoDBTest {

    private static final String _ACCESS_KEY = "test-access-key";
    private static final String _SECRET_KEY = "test-secret-key";
    private static final String _ENDPOINT = "http://localhost:4566";
    private static final String _REGION = "ap-northeast-2";

    private static final String _TABLE_NAME = "user";
    private static final String _MALL_ID = "10";
    private static final String _USER_ID = "test-user-id";
    private static final String _CONTENT = "test contents";

    public static void main(String[] args) {
        // credentials
        AWSCredentials credentials = new BasicAWSCredentials(_ACCESS_KEY, _SECRET_KEY);
        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(_ENDPOINT, _REGION);
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        // table list 출력
        getTableList(amazonDynamoDB);

        // item 등록
        putItem(amazonDynamoDB);

        // item 조회 (key)
        getItemByKey(amazonDynamoDB);

        // item 조회 (query)
        getItemByQuery(amazonDynamoDB);

        // item 삭제
//        removeItem(amazonDynamoDB);
    }

    public static void getTableList(AmazonDynamoDB amazonDynamoDB) {
        List<String> tableNames = amazonDynamoDB.listTables().getTableNames();
        System.out.println("========== TABLE LIST ==========");
        for (String tableName : tableNames) {
            System.out.println(tableName);
        }
        System.out.println("================================");
    }

    public static void putItem(AmazonDynamoDB amazonDynamoDB) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("mallId", (new AttributeValue()).withS(_MALL_ID));
        item.put("userId", (new AttributeValue()).withS(_USER_ID));
        item.put("content", (new AttributeValue()).withS(_CONTENT));
        item.put("deleted", (new AttributeValue()).withBOOL(false));
        item.put("createdAt", (new AttributeValue()).withS("2023-02-06T02:21:30.536Z"));

        PutItemRequest putItemRequest = (new PutItemRequest())
                .withTableName(_TABLE_NAME)
                .withItem(item);

        PutItemResult putItemResult = amazonDynamoDB.putItem(putItemRequest);
        if (200 == putItemResult.getSdkHttpMetadata().getHttpStatusCode()) {
            System.out.println("[success] put item");
        } else {
            System.out.println("[fail] put item");
        }
    }

    public static void getItemByKey(AmazonDynamoDB amazonDynamoDB) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("mallId", (new AttributeValue()).withS(_MALL_ID));
        key.put("userId", (new AttributeValue()).withS(_USER_ID));

        GetItemRequest getItemRequest = (new GetItemRequest())
                .withTableName(_TABLE_NAME)
                .withKey(key);

        GetItemResult getItemResult = amazonDynamoDB.getItem(getItemRequest);

        Map<String, AttributeValue> result = getItemResult.getItem();
        String mallId = result.get("mallId").getS();
        String userId = result.get("userId").getS();
        String content = result.get("content").getS();
        boolean deleted = result.get("deleted").getBOOL();
        String createdAt = result.get("createdAt").getS();

        System.out.println("======== GET ITEM BY KEY ========");
        System.out.println("mallId : " + mallId);
        System.out.println("userId : " + userId);
        System.out.println("content : " + content);
        System.out.println("deleted : " + deleted);
        System.out.println("createdAt : " + createdAt);
        System.out.println("================================");
    }

    public static void getItemByQuery(AmazonDynamoDB amazonDynamoDB) {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table user = dynamoDB.getTable(_TABLE_NAME);

        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("mallId = :mallId and userId = :userId")
                .withFilterExpression("content = :content")
                .withValueMap(new ValueMap()
                        .withString(":mallId", _MALL_ID)
                        .withString(":userId", _USER_ID)
                        .withString(":content", _CONTENT))
                .withConsistentRead(true);
        ItemCollection<QueryOutcome> items = user.query(spec);

        Iterator<Item> iterator = items.iterator();
        Item item = null;
        while (iterator.hasNext()) {
            item = iterator.next();
            long mallId = item.getLong("mallId");
            String userId = item.getString("userId");
            String content = item.getString("content");
            boolean deleted = item.getBOOL("deleted");
            String createdAt = item.getString("createdAt");

            System.out.println("======= GET ITEM BY QUERY =======");
            System.out.println("mallId : " + mallId);
            System.out.println("userId : " + userId);
            System.out.println("content : " + content);
            System.out.println("deleted : " + deleted);
            System.out.println("createdAt : " + createdAt);
            System.out.println("================================");
        }
    }

    public static void removeItem(AmazonDynamoDB amazonDynamoDB) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("mallId", (new AttributeValue()).withS(_MALL_ID));
        key.put("userId", (new AttributeValue()).withS(_USER_ID));

        DeleteItemRequest deleteItemRequest = (new DeleteItemRequest())
                .withTableName(_TABLE_NAME)
                .withKey(key);

        DeleteItemResult deleteItemResult = amazonDynamoDB.deleteItem(deleteItemRequest);

        if (200 == deleteItemResult.getSdkHttpMetadata().getHttpStatusCode()) {
            System.out.println("[success] delete item");
        } else {
            System.out.println("[fail] delete item");
        }
    }

}
