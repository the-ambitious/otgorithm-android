package com.ambit.otgorithm.dto;

public class RankerDTO {

    private int mProfileThumbnail;
    private String mUserId;
    private String mUserDesc;

    public RankerDTO(int mProfileThumbnail, String mUserId, String mUserDesc) {
        this.mProfileThumbnail = mProfileThumbnail;
        this.mUserId = mUserId;
        this.mUserDesc = mUserDesc;
    }

    public int getmProfileThumbnail() {
        return mProfileThumbnail;
    }

    public String getmUserId() {
        return mUserId;
    }

    public String getmUserDesc() {
        return mUserDesc;
    }

}
