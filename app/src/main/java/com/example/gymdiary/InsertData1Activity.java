package com.example.gymdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InsertData1Activity extends AppCompatActivity {

    ListView listViewOfExercises1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data1);

        listViewOfExercises1 = findViewById(R.id.listViewOfExercises1);
        ArrayAdapter adapter = new ArrayAdapter<>(InsertData1Activity.this,R.layout.list_view_sample_layout1,R.id.txtLayoutRow,getResources().getStringArray(R.array.Back));
        String category = getIntent().getStringExtra("category");
        final String date = getIntent().getStringExtra("date");

        switch (category){
            case "Back": adapter = new ArrayAdapter<>(InsertData1Activity.this,R.layout.list_view_sample_layout1,R.id.txtLayoutRow,getResources().getStringArray(R.array.Back)); break;
            case "Chest": adapter = new ArrayAdapter<>(InsertData1Activity.this,R.layout.list_view_sample_layout1,R.id.txtLayoutRow,getResources().getStringArray(R.array.Chest)); break;
            case "Legs": adapter = new ArrayAdapter<>(InsertData1Activity.this,R.layout.list_view_sample_layout1,R.id.txtLayoutRow,getResources().getStringArray(R.array.Legs)); break;
            case "Biceps": adapter = new ArrayAdapter<>(InsertData1Activity.this,R.layout.list_view_sample_layout1,R.id.txtLayoutRow,getResources().getStringArray(R.array.Biceps)); break;
            case "Triceps": adapter = new ArrayAdapter<>(InsertData1Activity.this,R.layout.list_view_sample_layout1,R.id.txtLayoutRow,getResources().getStringArray(R.array.Triceps)); break;
            case "Shoulders": adapter = new ArrayAdapter<>(InsertData1Activity.this,R.layout.list_view_sample_layout1,R.id.txtLayoutRow,getResources().getStringArray(R.array.Shoulders)); break;
            case "Abs": adapter = new ArrayAdapter<>(InsertData1Activity.this,R.layout.list_view_sample_layout1,R.id.txtLayoutRow,getResources().getStringArray(R.array.Abs)); break;
        }

        listViewOfExercises1.setAdapter(adapter);

        listViewOfExercises1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String exercise = listViewOfExercises1.getItemAtPosition(position).toString();
                Intent intent = new Intent(InsertData1Activity.this, InsertDetailsActivity.class);
                intent.putExtra("exercise", exercise);
                intent.putExtra("id",0);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
    }
}
