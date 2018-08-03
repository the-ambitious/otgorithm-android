package com.ambit.otgorithm.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StandbyActivity extends AppCompatActivity {

    TextView tv;

    @BindView(R.id.picker)
    BubblePicker picker;

    private static int selectedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standby);

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

        tv = (TextView) findViewById(R.id.toolbar_title);
        tv.setText("최종 확인");
        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(Color.WHITE);
        Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /****************************************************************/

        ButterKnife.bind(this);

        final String[] titles = getResources().getStringArray(R.array.countries);
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        final TypedArray images = getResources().obtainTypedArray(R.array.images);

        picker.setBubbleSize(3);
        picker.setCenterImmediately(true);
        picker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return titles.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(titles[position]);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                //item.setTypeface(mediumTypeface);
                item.setTextColor(ContextCompat.getColor(StandbyActivity.this, android.R.color.white));
                item.setBackgroundImage(ContextCompat.getDrawable(StandbyActivity.this, images.getResourceId(position, 0)));
                return item;
            }
        });

        picker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(PickerItem pickerItem) {
                selectedCount++;
                Toast.makeText(StandbyActivity.this,
                        "" + pickerItem.getTitle() + " 선택" + "\n" +
                        "selectedCount: " + selectedCount, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBubbleDeselected(PickerItem pickerItem) {
                selectedCount--;
                Toast.makeText(StandbyActivity.this,
                        "" + pickerItem.getTitle() + " 취소" + "\n" +
                                "selectedCount: " + selectedCount, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        picker.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        picker.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_completion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_completion:
                AlertDialog.Builder builder = new AlertDialog.Builder(StandbyActivity.this);
                builder.setTitle("확인")
                        .setMessage("선택된 품목이 다음과 같습니다." + "\n" +
                                "selectedCount: " + selectedCount + "\n"
                        )
                        .setCancelable(false)
                        .setPositiveButton("네",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // 네 클릭
                                        Intent intent = new Intent(StandbyActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 아니오 클릭. dialog 닫기.
                                dialog.cancel();
                            }
                        });
                builder.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
