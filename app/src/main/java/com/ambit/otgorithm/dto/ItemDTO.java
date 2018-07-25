package com.ambit.otgorithm.dto;

public class ItemDTO {

    String provincesTitle;
    int provincesImage;

    public ItemDTO(String provincesTitle, int provincesImage) {
        this.provincesTitle = provincesTitle;
        this.provincesImage = provincesImage;
    }

    public String getProvincesTitle() {
        return provincesTitle;
    }

    public int getProvincesImage() {
        return provincesImage;
    }

}
