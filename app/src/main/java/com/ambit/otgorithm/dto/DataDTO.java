package com.ambit.otgorithm.dto;

import com.ambit.otgorithm.R;

import java.util.ArrayList;

public class DataDTO {

    public static ArrayList<InformationDTO> getData() {
        ArrayList<InformationDTO> data = new ArrayList<>();

        int[] images = {
                R.drawable.ic_accessory2,
                R.drawable.ic_accessory3,
                R.drawable.ic_accessory4,
                R.drawable.ic_accessory5,
                R.drawable.ic_accessory6,
                R.drawable.ic_accessory7,
        };

        String[] Categories = {"cat 2", "cat 3", "cat 4", "cat 5", "cat 6", "cat 8"};

        for (int i = 0; i < images.length; i++) {
            InformationDTO current = new InformationDTO();
            current.title = Categories[i];
            current.imageId = images[i];

            data.add(current);
        }

        return data;
    }

}
