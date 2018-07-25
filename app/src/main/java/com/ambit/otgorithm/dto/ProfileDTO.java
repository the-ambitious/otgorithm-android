package com.ambit.otgorithm.dto;

import java.util.ArrayList;
import java.util.List;

public class ProfileDTO {

    private String name;
    private String description;
    private String firstLetter;

    public ProfileDTO(String name, String description) {
        this.name = name;
        this.firstLetter = String.valueOf(name.charAt(0));
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public static List<ProfileDTO> prepareProfiles(String[] names, String[] descriptions) {
        List<ProfileDTO> profiles = new ArrayList<>(names.length);

        for (int i = 0; i < names.length; i++) {
            ProfileDTO profile = new ProfileDTO(names[i], descriptions[i]);
            profiles.add(profile);
        }

        return profiles;
    }

}
