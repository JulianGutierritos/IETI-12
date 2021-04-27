package com.ieti.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ieti.myapplication.model.LoginWrapper;
import com.ieti.myapplication.model.Token;
import com.ieti.myapplication.service.AuthService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newFixedThreadPool( 1 );
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http:/10.0.2.2:8080") //localhost for emulator
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authService = retrofit.create(AuthService.class);
    }

    public void onLoginClicked( View view ) {
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            if (email.getText().toString().isEmpty()){
                email.setError("Error, el email no puede estar vacio.");
            }
            else {
                password.setError("Error, el password no puede estar vacio.");
            }
        }
        else {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response<Token> response =
                                authService.login(new LoginWrapper(email.getText().toString(), password.getText().toString())).execute();
                        Token token = response.body();
                        if (token != null) {
                            SharedPreferences sharedPref =
                                    getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPref.edit();
                            edit.putString("TOKEN_KEY", token.getAccessToken());
                            edit.apply();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    email.setError("Incorrecto");
                                    Snackbar.make(view, "Credenciales incorrectas", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


}