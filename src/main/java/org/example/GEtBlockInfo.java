//package org.example;
//
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//
//public class GEtBlockInfo {
//    public static void main(String[] args) {
//
//        Response response = RestAssured.given().baseUri("https://blockstream.info/api/block/")
//                .basePath("resorcePath").pathParam("resorcePath", blockHash.hashCodeValue)
//                .when().get();
//        System.out.println("hashcode is : "+ response.getBody().asString());
//    }
//}
