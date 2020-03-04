package com.shawncockburn.PITools.data;

public class ImageData {
    private Integer id;
    private String productCode;
    private String productWebCode;
    private String productName;


    public ImageData(Integer id, String productCode, String productWebCode, String productName) {
        this.id = id;
        this.productCode = productCode;
        this.productWebCode = productWebCode;
        this.productName = productName;
    }
    public ImageData(String productCode, String productWebCode, String productName) {
        this.productCode = productCode;
        this.productWebCode = productWebCode;
        this.productName = productName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductWebCode() {
        return productWebCode;
    }

    public void setProductWebCode(String productWebCode) {
        this.productWebCode = productWebCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
