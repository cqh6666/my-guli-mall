package com.haige.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.haige.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class GulimallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    void contextLoads() {

        System.out.println(client);
    }

    /**
     * 测试保存数据到es
     */
    @Test
    void indexTestData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        // 第一种方式
        // indexRequest.source("userName","haige","age",18);

        // 第二种方式
        User user = new User("haige",19);
        String jsonString = JSON.toJSONString(user);
        System.out.println(jsonString);
        indexRequest.source(jsonString, XContentType.JSON);

        // 执行操作
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);

        // 提取有用的响应数据
        System.out.println(response);
    }

    @Test
    void searchData() throws IOException {

        SearchRequest bankRequest = new SearchRequest("bank");
        // 构造条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("address","mill"));
//        sourceBuilder.from();
//        sourceBuilder.size();

        // 聚合条件
        TermsAggregationBuilder aggAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(aggAgg);
        AvgAggregationBuilder avgAgg = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(avgAgg);

        // DSL
        bankRequest.source(sourceBuilder);
        System.out.println("检索条件======>"+sourceBuilder.toString());
        // 执行
        SearchResponse searchResponse = client.search(bankRequest, RequestOptions.DEFAULT);

        // 分析结果
//        System.out.println("分析结果======>"+searchResponse.toString());
        // 获取所有查到的数据
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] dataHits = searchHits.getHits();
        for (SearchHit hit : dataHits) {
            System.out.println("hit-id:["+hit.getId()+"]======>"+hit.getSourceAsString());
        }

        Aggregations aggregations = searchResponse.getAggregations();
        Terms aggAggregation = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : aggAggregation.getBuckets()) {
            System.out.println("年龄=======>"+bucket.getKeyAsString());
        }

    }
    @Data
    class User{
        private String userName;
        private Integer age;

        public User(String userName, Integer age) {
            this.userName = userName;
            this.age = age;
        }
    }

}
