import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;

import java.util.List;

public class LocalstackSqsTest {

    private static final String _ACCESS_KEY = "test-access-key";
    private static final String _SECRET_KEY = "test-secret-key";
    private static final String _ENDPOINT = "http://localhost:4566";
    private static final String _REGION = "ap-northeast-2";

    private static final String _DEAN_QUEUE = "http://localhost:4566/000000000000/dean-queue";
    private static final String _BRIAN_QUEUE = "http://localhost:4566/000000000000/brian-queue";
    private static final String _BRANDON_QUEUE = "http://localhost:4566/000000000000/brandon-queue";
    private static final String _JACOB_QUEUE = "http://localhost:4566/000000000000/jacob-queue";
    private static final String _NEO_QUEUE = "http://localhost:4566/000000000000/neo-queue";
    private static final String _HARVEY_QUEUE = "http://localhost:4566/000000000000/harvey-queue";

    public static void main(String[] args) {
        // credentials
        AWSCredentials credentials = new BasicAWSCredentials(_ACCESS_KEY, _SECRET_KEY);
        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(_ENDPOINT, _REGION);
        AmazonSQS amazonSQS = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        // receive message
        while (true) {
            receiveMessage(amazonSQS, _DEAN_QUEUE);
            receiveMessage(amazonSQS, _BRIAN_QUEUE);
            receiveMessage(amazonSQS, _BRANDON_QUEUE);
            receiveMessage(amazonSQS, _JACOB_QUEUE);
            receiveMessage(amazonSQS, _NEO_QUEUE);
            receiveMessage(amazonSQS, _HARVEY_QUEUE);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void receiveMessage(AmazonSQS amazonSQS, String queueUrl) {
        List<Message> messages = amazonSQS.receiveMessage(queueUrl).getMessages();
        for (Message message : messages) {
            System.out.println("url : " + queueUrl + ", id : " + message.getMessageId() + ", body : " + message.getBody());
            amazonSQS.deleteMessage(queueUrl, message.getReceiptHandle());
        }
    }


}
