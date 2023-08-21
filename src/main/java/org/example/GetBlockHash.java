package org.example;
//import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.testng.internal.collections.Pair;

import java.util.List;

import static io.restassured.RestAssured.given;
public class GetBlockHash {
    private String url = "";
    public String hashCodeValue = "";

    public GetBlockHash(String url) {
        this.url = url;
    }

    public void drive() {

        hashCodeValue = getBlockHash(680000);
        Integer ls = getTransactionCount();
        String transactionId = getTransactionId(100);
        Pair<Integer,Integer> getTransactioDetail = getTransactioDetail(transactionId);
    }

    private Pair<Integer,Integer> getTransactioDetail(String transactionId) {
        Response response = RestAssured.given().when().get("https://blockstream.info/api/tx/{transactionId}", transactionId);
        JsonPath js = response.jsonPath();
        Integer vinSize = js.getList("vin").size();
        Integer voutSize = js.getList("vout").size();
        return Pair.of(vinSize,voutSize);
    }

    private String getTransactionId(Integer transactionId) {
        Response response = RestAssured.given().when().get("https://blockstream.info/api/block/{hashcode}/txid/{transactionId}", hashCodeValue,transactionId);
        return  response.asString();
    }

    public Integer getTransactionCount() {
        Response response = RestAssured.given().baseUri("https://blockstream.info/api/block/")
                .basePath("{resorcePath}").pathParam("resorcePath", hashCodeValue)
                .when().get();
        JsonPath js = new JsonPath(response.getBody().asString());
        int size = Integer.parseInt(js.getString("tx_count"));
        System.out.println("size is response is - "+ size);
        return size;
    }

    private String getBlockHash(Integer blockNumber) {
        Response response = RestAssured.get("https://blockstream.info/api/block-height/{blockNumber}", blockNumber);
        System.out.println("response is : "+ response.getStatusCode());
        System.out.println("hashcode is : "+ response.getBody().asString());
        return  response.getBody().asString();
    }

}

