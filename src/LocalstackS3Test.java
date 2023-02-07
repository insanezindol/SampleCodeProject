import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalstackS3Test {

    private static final String _ACCESS_KEY = "test-access-key";
    private static final String _SECRET_KEY = "test-secret-key";
    private static final String _ENDPOINT = "http://localhost:4566";
    private static final String _REGION = "ap-northeast-2";
    private static final String _S3_BUCKET_NAME = "test-bucket";
    private static final String _LOCAL_FILE_PATH = "file";

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String now = sdf.format(new Date());

        File file = new File(_LOCAL_FILE_PATH + "/sample.txt");
        String fileKey = now + "_" + file.getName();

        // credentials
        AWSCredentials credentials = new BasicAWSCredentials(_ACCESS_KEY, _SECRET_KEY);
        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(_ENDPOINT, _REGION);
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(config)
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        // s3 업로드
        uploadToAWS(amazonS3, file, fileKey);

        // s3 다운로드
        downloadToAWS(amazonS3, file.getAbsolutePath(), fileKey);

        // s3 이름변경
        renameToAWS(amazonS3, fileKey, "test-file-key.txt");

        // s3 삭제
        removeToAWS(amazonS3, fileKey);
    }

    public static void uploadToAWS(AmazonS3 amazonS3, File file, String fileKey) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            PutObjectRequest request = new PutObjectRequest(_S3_BUCKET_NAME, fileKey, file);
            request.setMetadata(metadata);
            request.withCannedAcl(CannedAccessControlList.AuthenticatedRead);
            amazonS3.putObject(request);
            System.out.println("upload success : " + fileKey);
        } catch (SdkClientException e) {
            System.out.println("upload fail : " + e.getMessage());
        }
    }

    public static void downloadToAWS(AmazonS3 amazonS3, String filePath, String fileKey) {
        try {
            String absolutePath = filePath.substring(0, filePath.lastIndexOf(File.separator));
            S3Object fullObject = amazonS3.getObject(_S3_BUCKET_NAME, fileKey);
            InputStream in = fullObject.getObjectContent();
            Files.copy(in, Paths.get(absolutePath + File.separator + fileKey));
            System.out.println("download success : " + fileKey);
        } catch (AmazonS3Exception e) {
            System.out.println("download fail : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("file copy fail : " + e.getMessage());
        }
    }

    public static void renameToAWS(AmazonS3 amazonS3, String sourceKey, String destinationKey) {
        try {
            amazonS3.copyObject(_S3_BUCKET_NAME, sourceKey, _S3_BUCKET_NAME, destinationKey);
            amazonS3.deleteObject(_S3_BUCKET_NAME, sourceKey);
            System.out.println("rename success : " + sourceKey + " to " + destinationKey);
        } catch (SdkClientException e) {
            System.out.println("rename fail : " + e.getMessage());
        }
    }

    public static void removeToAWS(AmazonS3 amazonS3, String fileKey) {
        try {
            amazonS3.deleteObject(_S3_BUCKET_NAME, fileKey);
            System.out.println("remove success : " + fileKey);
        } catch (SdkClientException e) {
            System.out.println("remove fail : " + e.getMessage());
        }
    }

}
