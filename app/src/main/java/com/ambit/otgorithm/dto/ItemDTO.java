package com.ambit.otgorithm.dto;

public class ItemDTO {

    String provincesTitle;
    String provincesImage;

    public ItemDTO() { }

    public ItemDTO(String provincesTitle, String provincesImage) {
        this.provincesTitle = provincesTitle;
        this.provincesImage = provincesImage;
    }

    public String getProvincesTitle() {
        return provincesTitle;
    }

    public void setProvincesTitle(String provincesTitle) {
        this.provincesTitle = provincesTitle;
    }

    public String getProvincesImage() {
        return provincesImage;
    }

    public void setProvincesImage(String provincesImage) {
        this.provincesImage = provincesImage;
    }

}
