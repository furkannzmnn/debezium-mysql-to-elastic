package com.example.debeziummysqltoelastic;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Service
public class ElasticSearchService {

    private static final String ELASTIC_SEARCH_HOST = "localhost";
    private static final int ELASTIC_SEARCH_PORT = 9200;

    private final RestClient restClient;

    public ElasticSearchService(RestClient restClient) {
        this.restClient = restClient;
    }


    public void readDataFromElasticSearch() throws IOException {
        Request re = new Request("GET", "/customers/_search");
        Response response = restClient.performRequest(re);
        response.getEntity().writeTo(System.out);
    }

}

@RestController
@RequestMapping("/api/v1/elastic")
@RequiredArgsConstructor
class SearchApi {
    private final ElasticSearchService elasticSearchService;

    @RequestMapping("/read")
    public void readDataFromElasticSearch() throws IOException {
        elasticSearchService.readDataFromElasticSearch();
    }
}
