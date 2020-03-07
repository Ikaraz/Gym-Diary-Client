package com.example.gymdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDetailsActivity extends AppCompatActivity {

    TextView txtExerciseName;
    ListView listViewDetails;
    RequestService requestService;
    String name;
    List<Exercise> exercises;
    SharedPreferences sharedPreferences;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        txtExerciseName = findViewById(R.id.txtExerciseName);
        listViewDetails = findViewById(R.id.listViewDetails);
        requestService = RequestService.getInstance(this);
        sharedPreferences = getSharedPreferences("loginDetails",MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId",0);

        name = getIntent().getStringExtra("name");
        txtExerciseName.setText(name);
        exercises = new ArrayList<>();

        findByName(name);

        listViewDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExerciseDetailsActivity.this, InsertDetailsActivity.class);
                intent.putExtra("exercise",name);
                intent.putExtra("id",exercises.get(position).getId());
                startActivity(intent);
            }
        });

        listViewDetails.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                new AlertDialog.Builder(ExerciseDetailsActivity.this).setMessage("Delete record?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteOne(exercises.get(position).getId());
                                findByName(name);
                            }
                        }).setNegativeButton("No",null).show();
                return true;
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        findByName(name);
    }

    private void findByName(String name){
        exercises.clear();
        requestService.findExerciseByUserIdAndName(userId ,name, new ServerCallback() {
            @Override
            public void onSuccess(List<JSONObject> list) {
                List<String> details = new ArrayList<>();
                for(JSONObject object : list){
                    try {
                        int id = object.getInt("id");
                        String name = object.getString("name");
                        int reps = object.getInt("reps");
                        double weight = object.getDouble("weight");
                        exercises.add(new Exercise(id,name,reps,weight));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for(Exercise exercise : exercises){
                    details.add("reps:  "+exercise.getReps()+"  weight:  "+exercise.getWeight());
                }
                ArrayAdapter adapter = new ArrayAdapter<>(ExerciseDetailsActivity.this,R.layout.list_view_sample_layout1,R.id.txtLayoutRow,details);
                listViewDetails.setAdapter(adapter);
            }

            @Override
            public void onSuccess(JSONObject object) {}
            @Override
            public void onSuccess(String response) {}
            @Override
            public void onFail() {}
        });
    }

    private void deleteOne(int id){
        requestService.deleteOneExercise(id, new ServerCallback() {
            @Override
            public void onSuccess(List<JSONObject> list) {}
            @Override
            public void onSuccess(JSONObject object) {}
            @Override
            public void onSuccess(String response) {
                Toast.makeText(ExerciseDetailsActivity.this,"Exercise deleted",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFail() {
                Toast.makeText(ExerciseDetailsActivity.this,"Error: Exercise not deleted",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
