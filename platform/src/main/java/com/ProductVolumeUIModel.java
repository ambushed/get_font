package com;

public class ProductVolumeUIModel {

    public final String productCode;
    public final String baseAssetName;
    public final String exchange;
    public Integer buyAmount = 0;
    public Integer sellAmount = 0;

    public ProductVolumeUIModel(String productCode, String baseAssetName, String exchange) {
        this.productCode = productCode;
        this.baseAssetName = baseAssetName;
        this.exchange = exchange;
    }

    @Override public String toString() {
        return "InDayProductVolume{" +
                "productCode='" + productCode + '\'' +
                ", baseAssetName='" + baseAssetName + '\'' +
                ", exchange='" + exchange + '\'' +
                ", buyAmount=" + buyAmount +
                ", sellAmount=" + sellAmount +
                '}';
    }
}
