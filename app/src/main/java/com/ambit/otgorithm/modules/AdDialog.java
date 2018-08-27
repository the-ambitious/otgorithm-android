package com.ambit.otgorithm.modules;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdDialog extends Dialog {

    AdView dialog_adview;
    TextView dialog_cancel;
    TextView dialog_yes;

    public AdDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_dialog);

        dialog_adview = findViewById(R.id.dialog_adView);
        dialog_cancel = findViewById(R.id.dialog_cancel);
        dialog_yes = findViewById(R.id.dialog_yes);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        AdRequest request = new AdRequest.Builder().build();
        dialog_adview.loadAd(request);

        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.runFinalization();
                System.exit(0);
            }
        });
    }
}
