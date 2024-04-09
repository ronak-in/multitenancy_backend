package com.assignment.multitenant.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.assignment.multitenant.bean.DeviceData;
import com.assignment.multitenant.interceptor.TenantContext;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class KafkaConsumer {

    @Autowired
    private DeviceDataService deviceDataService;

    @Autowired
    private ApplicationContext context;

    @Value("${spring.application.accesskey}")
    private String accessKey;

    @Value("${spring.application.secretkey}")
    private String secretKey;

    @KafkaListener(topics = "${spring.kafka.listener.topics}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        // Parse the Kafka message to extract the bucket name and file key
        // Assume the message format is "bucketName:fileKey"
        String[] parts = message.split(":");
        String bucketName = parts[0];
        String fileKey = parts[1];

        // Retrieve the file from the tenant-specific S3 bucket
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withForceGlobalBucketAccessEnabled(true)
                .build();
        S3Object s3Object = s3Client.getObject(bucketName, fileKey);

        try (S3ObjectInputStream inputStream = s3Object.getObjectContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            for (CSVRecord record : parser) {
                // Process each CSV record here
                DeviceData object = parseCsvRecord(record);
                TenantContext.setCurrentTenant(object.getTenantId());
                deviceDataService.save(object);
                TenantContext.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DeviceData parseCsvRecord(CSVRecord record) {
        DeviceData deviceData = new DeviceData();
        deviceData.setTenantId(record.get(0).trim());
        deviceData.setDeviceId(Long.parseLong(record.get(1).trim()));
        deviceData.setModel(record.get(2).trim());
        deviceData.setManufacturer(record.get(3).trim());
        deviceData.setDeviceType(record.get(4).trim());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate approvalDate = LocalDate.parse(record.get(5).trim(), formatter);
        deviceData.setApprovalDate(approvalDate);
        return deviceData;
    }
}