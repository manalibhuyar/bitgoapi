package org.example;

public class BitGoAssessment {
    public static void main(String[] args) {
        GetBlockHash blockHash = new GetBlockHash("https://blockstream.info/api/block-height");
        blockHash.drive();

    }
}
