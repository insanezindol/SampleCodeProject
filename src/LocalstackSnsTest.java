import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalstackSnsTest {

    private static final String _ACCESS_KEY = "test-access-key";
    private static final String _SECRET_KEY = "test-secret-key";
    private static final String _ENDPOINT = "http://localhost:4566";
    private static final String _REGION = "ap-northeast-2";

    private static final String _LUNA_TOPIC = "arn:aws:sns:ap-northeast-2:000000000000:lunasoft-topic";
    private static final String _GNG_TOPIC = "arn:aws:sns:ap-northeast-2:000000000000:gng-topic";

    public static void main(String[] args) {
        // credentials
        AWSCredentials credentials = new BasicAWSCredentials(_ACCESS_KEY, _SECRET_KEY);
        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(_ENDPOINT, _REGION);
        AmazonSNS amazonSNS = AmazonSNSClientBuilder.standard()
                .withEndpointConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        // send message
        sendMessage(amazonSNS, _LUNA_TOPIC);
        sendMessage(amazonSNS, _GNG_TOPIC);
    }

    private static void sendMessage(AmazonSNS amazonSNS, String topicArn) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String now = sdf.format(new Date());
        String message = "SAMPLE:::" + now;

        PublishResult publishResult = amazonSNS.publish(new PublishRequest()
                .withTopicArn(topicArn)
                .withMessage(message));
        if (200 == publishResult.getSdkHttpMetadata().getHttpStatusCode()) {
            System.out.println("[success] send message ::: " + topicArn);
        } else {
            System.out.println("[fail] send message ::: " + topicArn);
        }
    }


}
