package com.example.gymdiary;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

public class InsertDetailsActivity extends AppCompatActivity {

    TextView txtExerciseName;
    EditText editTextReps, editTextWeight;
    Button btnAddNew, btnClear;
    RequestService requestService;
    SharedPreferences sharedPreferences;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_details);

        txtExerciseName = findViewById(R.id.txtExerciseName);
        editTextReps = findViewById(R.id.editTextReps);
        editTextWeight = findViewById(R.id.editTextWeight);
        btnAddNew = findViewById(R.id.btnAddNew);
        btnClear = findViewById(R.id.btnClear);
        requestService = RequestService.getInstance(this);
        sharedPreferences = getSharedPreferences("loginDetails",MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId",0);


        String exercise = getIntent().getStringExtra("exercise");
        final int id = getIntent().getIntExtra("id",0);
        final String date = getIntent().getStringExtra("date");

        txtExerciseName.setText(exercise);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextReps.getText().toString().isEmpty() && !editTextWeight.getText().toString().isEmpty()){
                    try{
                        int reps = Integer.parseInt(editTextReps.getText().toString());
                        double weight = Double.parseDouble(editTextWeight.getText().toString());
                        if(id==0){
                            insertOne(reps, weight,date);
                        }else{
                            updateOne(id,reps,weight,date);
                        }

                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        Toast.makeText(InsertDetailsActivity.this,"Enter valid number",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(InsertDetailsActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextReps.getText().clear();
                editTextWeight.getText().clear();
            }
        });
    }

    private void insertOne(int reps, double weight, String date){
        requestService.insertOneExercise(userId, txtExerciseName.getText().toString(), reps , weight , date, new ServerCallback() {
            @Override
            public void onSuccess(List<JSONObject> list) {}
            @Override
            public void onSuccess(JSONObject object) {}
            @Override
            public void onSuccess(String response) {
                Toast.makeText(InsertDetailsActivity.this,"Exercise inserted",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFail() {
                Toast.makeText(InsertDetailsActivity.this,"Exercise not inserted",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOne(int id, int reps, double weight, String date){
        requestService.updateOneExercise(userId , id, txtExerciseName.getText().toString(), reps, weight, date, new ServerCallback() {
            @Override
            public void onSuccess(List<JSONObject> list) {}
            @Override
            public void onSuccess(JSONObject object) {}
            @Override
            public void onSuccess(String response) {
                Toast.makeText(InsertDetailsActivity.this,"Exercise updated",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFail() {
                Toast.makeText(InsertDetailsActivity.this,"Error: Exercise not updated",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
