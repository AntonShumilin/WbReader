package com.WbReader.Services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestStuff {
    public static void main(String[] args) throws IOException {
        String awsKeyId = "AKIAQVBUHYI5OORKMPKS";
        String accessKey = "FopySciq0QrZw5HHSjf86bh+1jlI+0Fgy3Bc0Q/W";
        String bucketName = "wbreader-files";
        String awsRegion = "eu-central-1";

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsKeyId, accessKey);
        AmazonS3 client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(awsRegion))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

//        client.putObject(bucketName, "testFile", new File("logs/app.log"));

        S3Object obj =  client.getObject(bucketName, "testFile");
        BufferedReader br = new BufferedReader(new InputStreamReader(obj.getObjectContent()));

        String s = null;
        System.out.println("test");
        System.out.println();


        while ((s= br.readLine()) != null) {

            System.out.println("LOGGER " + s);

        }

        br.close();

    }

}
