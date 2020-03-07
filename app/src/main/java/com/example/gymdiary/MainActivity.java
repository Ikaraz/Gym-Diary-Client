package com.example.gymdiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txtDate, txtLogout;
    Button btnAdd, btnPrevious, btnNext;
    RequestService requestService;
    LocalDate date;
    ListView listViewOfExercises;
    List<Exercise> exercises;
    SharedPreferences sharedPreferences;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDate = findViewById(R.id.txtDate);
        txtLogout = findViewById(R.id.txtLogout);
        btnAdd = findViewById(R.id.btnAdd);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        requestService = RequestService.getInstance(this);
        listViewOfExercises = findViewById(R.id.listViewOfExercises);
        sharedPreferences = getSharedPreferences("loginDetails",MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId",0);

        date = LocalDate.now();
        txtDate.setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yy")));
        exercises = new ArrayList<>();

        getData();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDate.setText(dateManipulation(1, date));
                getData();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDate.setText(dateManipulation(-1, date));
                getData();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertDataActivity.class);
                intent.putExtra("date",txtDate.getText().toString());
                startActivity(intent);
            }
        });

        listViewOfExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = listViewOfExercises.getItemAtPosition(position).toString();
                Intent intent = new Intent(MainActivity.this,ExerciseDetailsActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("userId",0);
                editor.apply();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }

    // Get All Data From Database
    private void getData(){
        exercises.clear();
        requestService.findExerciseByUserIdAndDate(userId, txtDate.getText().toString(), new ServerCallback() {
            @Override
            public void onSuccess(List<JSONObject> list) {
                List<String> namesList = new ArrayList<>();

                for(JSONObject object : list){
                    try {
                        int id = object.getInt("id");
                        String name = object.getString("name");
                        int reps = object.getInt("reps");
                        double weight = object.getDouble("weight");
                        exercises.add(new Exercise(id,name,reps,weight));

                        if(!namesList.contains(name)){
                            namesList.add(name);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(namesList.isEmpty()) namesList.add("Workout log is empty");
                ArrayAdapter adapter = new ArrayAdapter<>(MainActivity.this,R.layout.list_view_sample_layout1,R.id.txtLayoutRow,namesList);
                listViewOfExercises.setAdapter(adapter);
            }
            @Override
            public void onSuccess(JSONObject object) {}
            @Override
            public void onSuccess(String response) {}
            @Override
            public void onFail() {}
        });
    }

    private String dateManipulation(int number, LocalDate dateLocal){
        if(number > 0){
            date = dateLocal.plusDays(1L);
        } else if(number < 0){
            date = dateLocal.minusDays(1L);
        }
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yy"));
    }
}
