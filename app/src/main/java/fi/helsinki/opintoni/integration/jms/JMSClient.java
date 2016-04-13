package fi.helsinki.opintoni.integration.jms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class JMSClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(JMSClient.class);

    private static final int SYNC_TIMEOUT = 10000;

    private final JmsTemplate jmsTemplate;
    private final String requestQueueName;
    private final String responseQueueName;

    protected final ObjectMapper objectMapper;

    public JMSClient(JmsTemplate jmsTemplate, ObjectMapper objectMapper, String requestQueueName, String responseQueueName) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
        this.requestQueueName = requestQueueName;
        this.responseQueueName = responseQueueName;
    }

    private TextMessage sendMessage(String method, Map<String, String> parameters) {
        return (TextMessage) jmsTemplate.execute(
            new SynchronousMessage(
                parameters,
                method,
                jmsTemplate.getDestinationResolver(),
                requestQueueName,
                responseQueueName,
                SYNC_TIMEOUT),
            true);
    }

    protected String queryMethodWithParameters(String method, Map<String, String> parameters) {
        LOGGER.info("Querying method  " + method + " synchronously over JMS with parameters " + Arrays.toString(parameters.entrySet().toArray()));

        try {
            return sendMessage(method, parameters).getText();
        } catch (JMSException e) {
            throw new RuntimeException("JMS query failed for method " + method);
        }
    }

    protected <T> T parseResponse(String response, TypeReference<T> typeReference) {
        LOGGER.info("Parsing single response object " + response );
        try {
            return objectMapper.readValue(getDataFromResponse(response), typeReference);
        } catch (Exception e) {
            throw new RuntimeException("JMS response parsing failed");
        }
    }

    protected <T> List<T> parseListResponse(String response, TypeReference<List<T>> typeReference ) {
        LOGGER.info("Parsing list of response objects " + response );
        try {
            return objectMapper.readValue(getDataFromResponse(response), typeReference);
        } catch (Exception e) {
            throw new RuntimeException("JMS response parsing failed");
        }
    }

    protected abstract String getDataFromResponse(String response);
}
