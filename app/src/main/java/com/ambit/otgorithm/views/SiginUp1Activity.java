package com.ambit.otgorithm.views;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ambit.otgorithm.R;

public class SiginUp1Activity extends AppCompatActivity {

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;

    private Toolbar mSignUpToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin_up1);

        setTitle("회원가입");

        textInputEmail = findViewById(R.id.text_input_email);
        textInputUsername = findViewById(R.id.text_input_passwd);
        textInputPassword = findViewById(R.id.text_input_passwd2);

        mSignUpToolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        // getWindow().setStatusBarColor(getResources().getColor(R.color.menu_color));
        //getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.menu_color));
        setSupportActionBar(mSignUpToolbar);

        // 뒤로가기 버튼, default로 true만 해도 백버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next, menu);
        return true;
    }

    /**
     * This will make finish the current activity and move back
     * to previous activity on toolbar back button press.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.nextBtn:
                intent = new Intent(SiginUp1Activity.this, SignUp2Activity.class);
                startActivity(intent);
                Log.d("SignUp 테스트: ", "case R.menu.next 진입");
                break;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        Log.d("SignUp 테스트: ", "case 문 탈출 후 startActivity 실행 후");
        return true;
    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("username Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            textInputUsername.setError("username too long");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("password Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) {
        Intent intent = null;

        if (!validateEmail() | !validateUsername() | !validatePassword()) {
            return;
        }

        String input = "Email: " + textInputEmail.getEditText().getText().toString();
        input += "\n";
        input += "Username: " + textInputUsername.getEditText().getText().toString();
        input += "\n";
        input += "Password: " + textInputPassword.getEditText().getText().toString();

        // 값 확인 테스트용; 들고가려면 intent로 넘기기
        // Toast.makeText(this, input, Toast.LENGTH_SHORT).show();

        switch (v.getId()) {
            /*case R.id.confirm:
                intent = new Intent(this, MainActivity.class);
                break;*/
        }
        startActivity(intent);
    }
}
