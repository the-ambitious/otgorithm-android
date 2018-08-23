package com.ambit.otgorithm.modules;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.ambit.otgorithm.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FirstAdDialog extends Dialog {

    //Button first_ad_cancel;
    AdView first_adView;

    ImageView closeAd;

    public FirstAdDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_ad_dialog);

        closeAd = findViewById(R.id.close_ad);
        closeAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        //first_ad_cancel = findViewById(R.id.first_ad_cancel);
        first_adView = findViewById(R.id.first_adView);


        AdRequest request = new AdRequest.Builder().build();
        first_adView.loadAd(request);

      /*  first_ad_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/
    }
}
