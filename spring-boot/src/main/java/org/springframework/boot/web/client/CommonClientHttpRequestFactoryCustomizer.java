package org.springframework.boot.web.client;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;

import static org.springframework.util.ReflectionUtils.invokeMethod;

public class CommonClientHttpRequestFactoryCustomizer implements RestTemplateCustomizer {
    private static final String SET_CONNECT_TIMEOUT_METHOD_NAME = "setConnectTimeout";
    private static final String SET_READ_TIMEOUT_METHOD = "setReadTimeout";

    private int connectTimeout;
    private int readTimeout;

    @Override
    public void customize(RestTemplate restTemplate) {
        ClientHttpRequestFactory clientHttpRequestFactory = restTemplate.getRequestFactory();
        configureTimeout(clientHttpRequestFactory);
    }

    private void configureTimeout(ClientHttpRequestFactory clientHttpRequestFactory) {
        Class<? extends ClientHttpRequestFactory> factoryClass = clientHttpRequestFactory.getClass();

        Method setConnectTimeoutMethod = ReflectionUtils.findMethod(factoryClass,
                SET_CONNECT_TIMEOUT_METHOD_NAME, int.class);
        if (setConnectTimeoutMethod != null && connectTimeout != 0) {
            try {
                invokeMethod(setConnectTimeoutMethod, clientHttpRequestFactory, connectTimeout);
            } catch (Exception e) {
                // ClientHttpRequestFactory does not have setConnectTimeout method
                e.printStackTrace();
            }
        }

        Method setReadTimeoutMethod = ReflectionUtils.findMethod(factoryClass,
                SET_READ_TIMEOUT_METHOD, int.class);
        if (setReadTimeoutMethod != null && readTimeout != 0) {
            try {
                invokeMethod(setReadTimeoutMethod, clientHttpRequestFactory, readTimeout);
            } catch (Exception e) {
                // ClientHttpRequestFactory does not have setReadTimeout method
                e.printStackTrace();
            }
        }
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
