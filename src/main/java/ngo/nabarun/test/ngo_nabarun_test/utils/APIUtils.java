package ngo.nabarun.test.ngo_nabarun_test.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class APIUtils {
    private static final Logger logger = LogManager.getLogger(APIUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static void addCommonHeaders(HttpRequestBase request, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request.addHeader("accept", "application/json");
    }

    public static String httpGet(String url, Map<String, String> headers) {
        HttpGet request = new HttpGet(url);
        addCommonHeaders(request, headers);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            logger.error("Error calling GET endpoint: {}", url, e);
        }
        return null;
    }

    public static String httpPost(String url, Map<String, String> headers, Object body) {
        HttpPost request = new HttpPost(url);
        addCommonHeaders(request, headers);
        request.addHeader("Content-Type", "application/json");
        try {
            String jsonBody = (body instanceof String) ? (String) body : objectMapper.writeValueAsString(body);
            request.setEntity(new StringEntity(jsonBody));
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            }
        } catch (IOException e) {
            logger.error("Error calling POST endpoint: {}", url, e);
        }
        return null;
    }

    public static String httpPut(String url, Map<String, String> headers, Object body) {
        HttpPut request = new HttpPut(url);
        addCommonHeaders(request, headers);
        request.addHeader("Content-Type", "application/json");
        try {
            String jsonBody = (body instanceof String) ? (String) body : objectMapper.writeValueAsString(body);
            request.setEntity(new StringEntity(jsonBody));
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            }
        } catch (IOException e) {
            logger.error("Error calling PUT endpoint: {}", url, e);
        }
        return null;
    }

    public static String httpDelete(String url, Map<String, String> headers) {
        HttpDelete request = new HttpDelete(url);
        addCommonHeaders(request, headers);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            logger.error("Error calling DELETE endpoint: {}", url, e);
        }
        return null;
    }

    public static <T> T httpGet(String url, Map<String, String> headers, Class<T> responseType) {
        String response = httpGet(url, headers);
        if (response != null) {
            try {
                return objectMapper.readValue(response, responseType);
            } catch (IOException e) {
                logger.error("Error parsing GET response: {}", url, e);
            }
        }
        return null;
    }

    public static <T> T httpPost(String url, Map<String, String> headers, Object body, Class<T> responseType) {
        String response = httpPost(url, headers, body);
        if (response != null) {
            try {
                return objectMapper.readValue(response, responseType);
            } catch (IOException e) {
                logger.error("Error parsing POST response: {}", url, e);
            }
        }
        return null;
    }

    public static <T> T httpPut(String url, Map<String, String> headers, Object body, Class<T> responseType) {
        String response = httpPut(url, headers, body);
        if (response != null) {
            try {
                return objectMapper.readValue(response, responseType);
            } catch (IOException e) {
                logger.error("Error parsing PUT response: {}", url, e);
            }
        }
        return null;
    }

    public static <T> T httpDelete(String url, Map<String, String> headers, Class<T> responseType) {
        String response = httpDelete(url, headers);
        if (response != null) {
            try {
                return objectMapper.readValue(response, responseType);
            } catch (IOException e) {
                logger.error("Error parsing DELETE response: {}", url, e);
            }
        }
        return null;
    }

    public static <T> T httpGet(String url, Map<String, String> headers, TypeReference<T> typeReference) {
        String response = httpGet(url, headers);
        if (response != null) {
            try {
                return objectMapper.readValue(response, typeReference);
            } catch (IOException e) {
                logger.error("Error parsing GET response: {}", url, e);
            }
        }
        return null;
    }

    public static <T> T httpPost(String url, Map<String, String> headers, Object body, TypeReference<T> typeReference) {
        String response = httpPost(url, headers, body);
        if (response != null) {
            try {
                return objectMapper.readValue(response, typeReference);
            } catch (IOException e) {
                logger.error("Error parsing POST response: {}", url, e);
            }
        }
        return null;
    }

    public static <T> T httpPut(String url, Map<String, String> headers, Object body, TypeReference<T> typeReference) {
        String response = httpPut(url, headers, body);
        if (response != null) {
            try {
                return objectMapper.readValue(response, typeReference);
            } catch (IOException e) {
                logger.error("Error parsing PUT response: {}", url, e);
            }
        }
        return null;
    }

    public static <T> T httpDelete(String url, Map<String, String> headers, TypeReference<T> typeReference) {
        String response = httpDelete(url, headers);
        if (response != null) {
            try {
                return objectMapper.readValue(response, typeReference);
            } catch (IOException e) {
                logger.error("Error parsing DELETE response: {}", url, e);
            }
        }
        return null;
    }
}