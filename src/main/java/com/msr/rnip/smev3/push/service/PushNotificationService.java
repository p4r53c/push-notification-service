package com.msr.rnip.smev3.push.service;

import com.msr.rnip.smev3.push.config.PushNotificationConfig;
import com.msr.rnip.smev3.push.dao.PushNotificationDao;
import com.msr.rnip.smev3.push.model.PushNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ru.gov.smev.artefacts.x.smev._1.*;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author p4r53c
 * @created 07.08.2020
 */
@Endpoint
public class PushNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationService.class);

    private static final String NAMESPACE_URI = "urn://x-artefacts-smev-gov-ru/smev/1.0";

    @Autowired
    private PushNotificationDao pushNotificationDao;

    //Типизированный JAXBElement потому, что в объектной модели нет анотаций @XMLRootElement. Либо биндинги либо так.
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "PushNotificationRequest")
    @ResponsePayload
    public JAXBElement<PushNotificationResponse> receiveSmevNotice(@RequestPayload JAXBElement<PushNotificationRequest> request) {
        try {
            String requstId = getBasicUUID();
            XMLGregorianCalendar smevInfoTimeStamp = request.getValue().getPushNotification().getInformationTimestamp();
            List<QueueInformation> queueInformationList = request.getValue().getPushNotification().getQueueInformation();
            LOGGER.debug("Receiving [PushNotificationRequest] and assigning [{}] as [REQUEST_ID]", requstId);
            for (QueueInformation queueInformation : queueInformationList) {
                PushNotification pushNotification = new PushNotification();
                pushNotification.setInternalId(getBasicUUID());
                pushNotification.setRequestId(requstId);
                pushNotification.setReceiveTimestamp(new Date());
                pushNotification.setSmevInfoTimestamp(smevInfoTimeStamp.toGregorianCalendar().getTime());
                pushNotification.setQueueName(queueInformation.getQueueName());
                pushNotification.setQueueSize(BigInteger.valueOf(queueInformation.getQueueSize()));
                savePushNotice(pushNotification);
                LOGGER.debug("Processing [QueueInformation] for [QueueName] [{}]", queueInformation.getQueueName());
            }
        } catch (Exception ex) {
            LOGGER.error("An exception was thrown when a notification received", ex);
        }

        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<String> jaxbResponseDescription = objectFactory.createDescriptionDescription("SUCCESS");

        Description responseDescription = new Description();
        responseDescription.setDescription(jaxbResponseDescription);

        PushNotificationResponse pushNotificationResponse = new PushNotificationResponse();
        pushNotificationResponse.setDescriptionResponse(responseDescription);
        LOGGER.debug("Inbound [PushNotificationRequest] was successfully processed. Sending response");
        return objectFactory.createPushNotificationResponse(pushNotificationResponse);
    }

    private void savePushNotice(PushNotification pushNotification) {
        this.pushNotificationDao.save(pushNotification);
    }

    /**
     * Метод формирующий internalId, при получении PUSH-уведомления
     * @return String - Random UUID.
     */
    private String getBasicUUID() {
        return UUID.randomUUID().toString();
    }
}
