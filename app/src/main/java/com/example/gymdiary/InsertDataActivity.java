package com.example.gymdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InsertDataActivity extends AppCompatActivity {

    ListView listViewOfExercises;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        listViewOfExercises = findViewById(R.id.listViewOfExercises);
        final String date = getIntent().getStringExtra("date");

        adapter = new ArrayAdapter<>(this, R.layout.list_view_sample_layout1, R.id.txtLayoutRow, getResources().getStringArray(R.array.exercises));
        listViewOfExercises.setAdapter(adapter);

        listViewOfExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = listViewOfExercises.getItemAtPosition(position).toString();
                Intent intent = new Intent(InsertDataActivity.this, InsertData1Activity.class);
                intent.putExtra("category", category);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }
}