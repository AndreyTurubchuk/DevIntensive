package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.RestService;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {
    private Button mSignIn;
    private TextView mRememberPassword;
    private EditText mLogin, mPassword;
    private CoordinatorLayout mCoordinatorLayout;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mDataManager = DataManager.getInstance();
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mSignIn = (Button) findViewById(R.id.login_btn);
        mRememberPassword = (TextView) findViewById(R.id.remember_txt);
        mLogin = (EditText) findViewById(R.id.login_email_et);
        mPassword = (EditText) findViewById(R.id.login_password_et);

        mRememberPassword.setOnClickListener(this);
        mSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                signIn();
                //loginSuccess();
                break;
            case R.id.remember_txt:
                rememberPassword();
                break;
        }
    }

    private void loginSuccess(Response<UserModelRes> response) {
        showSnackBar("Вход");
    }

    private void showSnackBar(String message){
        Snackbar.make(mCoordinatorLayout,message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword(){
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void signIn(){
        Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq("email", "password"));
        call.enqueue(new Callback<UserModelRes>() {
            @Override
            public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                if (response.code() == 200){
                    loginSuccess(response);
                } else if (response.code() == 403){
                    showSnackBar("Неверный логин или пароль");
                }

            }

            @Override
            public void onFailure(Call<UserModelRes> call, Throwable t) {
                //TODO обработать ошибки
            }
        });

    }
}
