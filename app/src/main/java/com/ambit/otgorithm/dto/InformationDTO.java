package com.ambit.otgorithm.dto;

public class InformationDTO {

    public int imageId;
    public String title;

    public InformationDTO() { }
    public InformationDTO(int imageId, String title) {
        this.imageId = imageId;
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}