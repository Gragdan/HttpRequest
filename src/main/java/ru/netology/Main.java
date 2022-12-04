package ru.netology;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks" +
                "/master/http/task1/cats");
        CloseableHttpResponse response;

        String body;
        {
            try {
                response = httpClient.execute(request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

          //  Arrays.stream(response.getAllHeaders()).
                //    forEach(System.out::println);
            try {
                body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                //  System.out.println(body);
            } catch (
                    IOException e) {
                throw new RuntimeException(e);
            }
        }
        httpClient.close();
        readAnswer(body);
    }

    static void readAnswer(String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<FromServer> fromServers = mapper.readValue(body, new TypeReference<>() {
        });
        // fromServers.forEach(System.out::println);

        for (int i = 0; i < fromServers.size(); i++) {
            Integer myInt = fromServers.get(i).getUpvotes();
            if (myInt > 0 && myInt != null) {
                System.out.println(i+1 + " " + fromServers.get(i).toString());
            }
        }

    }
}