package com.ambit.otgorithm.modules;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.bumptech.glide.Glide;

import java.net.URI;

public class ExpansionDialog extends Dialog {

    ImageView expansion;
    ImageView close;
    GalleryDTO galleryDTO;
    Context context;

    public ExpansionDialog(@NonNull Context context, GalleryDTO galleryDTO) {
        super(context);
        this.context = context;
        this.context.setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.galleryDTO = galleryDTO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //context.setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setContentView(R.layout.expansion_dialog);

        expansion = findViewById(R.id.image_expansion);
        close = findViewById(R.id.dialog_close);

//        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT);

        Glide.with(context).load(galleryDTO.imageUrl).into(expansion);
       /* Uri myUri = Uri.parse(galleryDTO.imageUrl);
        expansion.setImageURI(myUri);*/
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
