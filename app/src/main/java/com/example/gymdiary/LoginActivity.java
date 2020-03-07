package com.example.gymdiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button btnLogin;
    TextView txtRegisterMe;
    SharedPreferences sharedPreferences;
    RequestService service;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegisterMe = findViewById(R.id.txtRegisterMe);
        service = RequestService.getInstance(this);
        sharedPreferences = getSharedPreferences("loginDetails",MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId",0);
        if(userId != 0){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextEmail.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty()){
                    final String email = editTextEmail.getText().toString();
                    final String password = editTextPassword.getText().toString();
                    service.findUserByEmail(email, new ServerCallback() {
                        @Override
                        public void onSuccess(List<JSONObject> list) {}
                        @Override
                        public void onSuccess(JSONObject object) {
                            try{
                                if(email.equals(object.getString("email")) && password.equals(object.getString("password"))){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("userId",object.getInt("id"));
                                    editor.apply();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(LoginActivity.this,"Invalid password",Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onSuccess(String response) {}
                        @Override
                        public void onFail() {
                            Toast.makeText(LoginActivity.this,"Invalid email",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }

            }
        });

        txtRegisterMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
