package com.ambit.otgorithm.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.ambit.otgorithm.R;

public class SignUp2Activity extends AppCompatActivity {

    private Toolbar mSignUpToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        mSignUpToolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        setSupportActionBar(mSignUpToolbar);

        // 뒤로가기 버튼, default로 true만 해도 백버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * This will make finish the current activity and move back
     * to previous activity on toolbar back button press.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            /*case R.id.nextBtn:
                intent = new Intent(SiginUp1Activity.this, SignUp2Activity.class);
                startActivity(intent);
                Log.d("SignUp 테스트: ", "case R.menu.menu_next 진입");
                break;*/
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        // Log.d("SignUp 테스트: ", "case 문 탈출 후 startActivity 실행 후");
        // return true;
    }

}
