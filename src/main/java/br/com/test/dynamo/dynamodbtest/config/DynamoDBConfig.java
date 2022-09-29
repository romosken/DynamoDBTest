package br.com.test.dynamo.dynamodbtest.config;


import br.com.test.dynamo.dynamodbtest.model.Task;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@EnableDynamoDBRepositories("br.com.test.dynamo.dynamodbtest.repository")
public class DynamoDBConfig {

    @Value("${aws.access.key.id:fakeId}")
    private String awsAccessKeyId;
    @Value("${aws.access.key.secret:fakeSecret}")
    private String awsAccessKeySecret;
    @Value("${dynamodb.service.endpoint:http://localhost:8000/}")
    private String dynamoDBServiceEndPoint;
    @Value("${dynamodb.service.region:sa-east-1}")
    private String dynamoDBRegion;


//    @Bean
//    public DynamoDBMapper mapperBuilder(AmazonDynamoDB amazonDynamoDB){
//        return new DynamoDBMapper(amazonDynamoDB);
//    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB(){
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration())
                .withCredentials(credentialsProvider())
                .build();
    }

    private AWSCredentialsProvider credentialsProvider() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsAccessKeyId, awsAccessKeySecret)
        );
    }

    private AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(dynamoDBServiceEndPoint, dynamoDBRegion);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDB(ApplicationReadyEvent event){
        var db = event.getApplicationContext().getBean(AmazonDynamoDB.class);
        var mapper = event.getApplicationContext().getBean(DynamoDBMapper.class);
        var createRequest = mapper.generateCreateTableRequest(Task.class);
        System.out.println("Start!");

        if (db.listTables().getTableNames().contains(createRequest.getTableName())){
            return;
//            db.deleteTable(createRequest.getTableName());
        }

        createRequest.setProvisionedThroughput(new ProvisionedThroughput(1L,1L));
        db.createTable(createRequest);
        System.out.println("Tables created!");
    }
}
