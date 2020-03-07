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

public class RegistrationActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword;
    Button btnRegister;
    TextView txtAlreadyReg;
    SharedPreferences sharedPreferences;
    RequestService service;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtAlreadyReg = findViewById(R.id.txtAlreadyReg);
        service = RequestService.getInstance(this);
        sharedPreferences = getSharedPreferences("loginDetails",MODE_PRIVATE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextName.getText().toString().isEmpty() || !editTextEmail.getText().toString().isEmpty() || !editTextPassword.getText().toString().isEmpty()){
                    final String name = editTextName.getText().toString();
                    final String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();
                    service.insertOneUser(name, email, password, new ServerCallback() {
                        @Override
                        public void onSuccess(List<JSONObject> list) {}
                        @Override
                        public void onSuccess(JSONObject object) {}
                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(RegistrationActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                            findUserByEmail(email);
                        }
                        @Override
                        public void onFail() {
                            Toast.makeText(RegistrationActivity.this,"Error: cant register with these credentials",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(RegistrationActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtAlreadyReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findUserByEmail(String email){
        service.findUserByEmail(email, new ServerCallback() {
            @Override
            public void onSuccess(List<JSONObject> list) {}
            @Override
            public void onSuccess(JSONObject object) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try {
                    editor.putInt("userId",object.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.apply();
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onSuccess(String response) {}
            @Override
            public void onFail() {}
        });
    }
}
