package com.cch.codechallengehub.service;

import com.cch.codechallengehub.web.exception.custom.AwsSesException;
import com.cch.codechallengehub.web.exception.custom.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class AwsSesService {

    private final SesV2Client client;

    @Value("${aws.ses.send-mail-to}")
    private String sendMailTo;

    private String UTF8 = "UTF-8";

    public void sendEmail(String recipient, String subject, String bodyHTML) {
        Destination destination = Destination.builder()
                .toAddresses(recipient)
                .build();

        Content sub = Content.builder()
                .charset(UTF8)
                .data(subject)
                .build();

        Content content = Content.builder()
                .charset(UTF8)
                .data(bodyHTML)
                .build();

        Body body = Body.builder()
                .html(content)
                .build();

        Message msg = Message.builder()
                .subject(sub)
                .body(body)
                .build();

        EmailContent emailContent = EmailContent.builder()
                .simple(msg)
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .content(emailContent)
                .fromEmailAddress(sendMailTo)
                .build();
        try {
            client.sendEmail(emailRequest);
        } catch (SesV2Exception e) {
            throw new AwsSesException(e.awsErrorDetails().errorMessage()
                    , HttpStatus.valueOf(e.awsErrorDetails().sdkHttpResponse().statusCode()));
        }

    }

    public void sendEmailTemplate(String recipient, String templateName, Map<String,String> templateData){
        Destination destination = Destination.builder()
                .toAddresses(recipient)
                .build();

        Template myTemplate = Template.builder()
                .templateName(templateName)
                .templateData(modelDataSerializer(templateData))
                .build();

        EmailContent emailContent = EmailContent.builder()
                .template(myTemplate)
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .content(emailContent)
                .fromEmailAddress(sendMailTo)
                .build();

        try {
            client.sendEmail(emailRequest);
        } catch (SesV2Exception e) {
            throw new AwsSesException(e.awsErrorDetails().errorMessage()
                    , HttpStatus.valueOf(e.awsErrorDetails().sdkHttpResponse().statusCode()));
        }
    }

    private String modelDataSerializer(Map<String, String> templateData) {
        ObjectMapper mapperObj = new ObjectMapper();
        try {
            return mapperObj.writeValueAsString(templateData);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Template Date error");
        }
    }
}
