package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetBlockHashTest {

    List<Integer> ls = new ArrayList<>(Arrays.asList(100, 200, 300));
     Integer blockHash =680000;


    private String getBlockHash(Integer blockNumber) {
        Response response = RestAssured.get("https://blockstream.info/api/block-height/{blockNumber}", blockNumber);
        System.out.println("response is : " + response.getStatusCode());
        System.out.println("hashcode is : " + response.getBody().asString());
        return response.getBody().asString();
    }

    private String getTransactionId(String hashCodeValue, Integer transactionIndex) {
        Response response = RestAssured.given().when().get("https://blockstream.info/api/block/{hashcode}/txid/{transactionIndex}", hashCodeValue, transactionIndex);
        return response.asString();
    }

    private Pair<Integer, Integer> getTransactioDetail(String transactionId) {
        Response response = RestAssured.given().when().get("https://blockstream.info/api/tx/{transactionId}", transactionId);
        JsonPath js = response.jsonPath();
        Integer vinSize = js.getList("vin").size();
        Integer voutSize = js.getList("vout").size();
        return Pair.of(vinSize, voutSize);
    }

    @Test
    public void testTotalTransactionCount() {
        String hashCodeValue = getBlockHash(blockHash);
        Response response = RestAssured.given().baseUri("https://blockstream.info/api/block/")
                .basePath("{resorcePath}").pathParam("resorcePath", hashCodeValue)
                .when().get();

        JsonPath js = new JsonPath(response.getBody().asString());
        int count = Integer.parseInt(js.getString("tx_count"));
        Assert.assertEquals(count, 2875);
        Assert.assertNotEquals(count, 2874);
    }

    public int testInputCount(int transactionIndex) {
        String hashCodeValue = getBlockHash(blockHash);
        String transactionId = getTransactionId(hashCodeValue, transactionIndex);
        var result = getTransactioDetail(transactionId);
        return result.first();
    }

    public int testOutputCount(int transactionIndex) {
        String hashCodeValue = getBlockHash(blockHash);
        String transactionId = getTransactionId(hashCodeValue, transactionIndex);
        var result = getTransactioDetail(transactionId);
        return result.second();
    }

    @Test
    public void testInput() {
        int sum = 0;
        for (int i = 0; i < ls.size(); i++) {
            sum = sum + testInputCount(ls.get(i));
        }
        Assert.assertEquals(sum, 5);
    }

    @Test
    public void testOutput() {
        int sum = 0;
        for (int i = 0; i < ls.size(); i++) {
            sum = sum + testOutputCount(ls.get(i));
        }
        Assert.assertEquals(sum, 4);
    }
}
