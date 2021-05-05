package com.example.mygraphview;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Spinner letterSpinner;
    private Spinner categorySpinner;
    private GraphView graph;
    private GridLabelRenderer gridLabel;
    private FirebaseFirestore firebaseFirestore;
    private String TAG="Graph";
    private ArrayList<Attempt>attempts;
    private String selectedLetter ="All";
    private String selectedCategory ="Score";
    private LineGraphSeries<DataPoint> series;
    private static final int SCORE_TOTAL=100;
    private  ArrayList<String> arrayList=new ArrayList<>();
    private ArrayAdapter attemptsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graph=findViewById(R.id.graph);
        letterSpinner = findViewById(R.id.spinner1);
        categorySpinner = findViewById(R.id.spinner2);
        firebaseFirestore = FirebaseFirestore.getInstance();
        attempts=new ArrayList<>();
        listView=findViewById(R.id.listview);
        ArrayAdapter<String> letterAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.Letter));
        letterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        letterSpinner.setAdapter(letterAdapter);
        letterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedLetter=letterSpinner.getSelectedItem().toString();
                onStart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.Type));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory=categorySpinner.getSelectedItem().toString();
                onStart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        attemptsAdapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                arrayList);
        listView.setAdapter(attemptsAdapter);
        graph.setTitle(selectedCategory+" Graph");
        graph.setTitleTextSize(32);
        graph.setCursorMode(true);
        GridLabelRenderer gridLabel=graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Attempts");
        gridLabel.setHorizontalAxisTitleTextSize(24);
        gridLabel.setVerticalAxisTitle(selectedCategory);
        gridLabel.setVerticalAxisTitleTextSize(24);
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        if(selectedCategory.contentEquals("Score")){
            series.setColor(Color.MAGENTA);
        } else if(selectedCategory.contentEquals("No of Errors")){
            series.setColor(Color.RED);
        }else{
            series.setColor(Color.BLUE);
        }
        series.setTitle(selectedCategory);
        series.setDataPointsRadius(16);
        series.setThickness(8);
        series.setAnimated(true);
        firebaseFirestore.collection("Attempts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        arrayList.clear();
                        attempts.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Attempt attempt=document.toObject(Attempt.class);
                            if(selectedLetter.contentEquals("All")) {
                                attempts.add(attempt);
                                arrayList.add(attempts.size()+" - "+attempt.getAttemptedAt());
                            }else if(selectedLetter.contentEquals(attempt.getLetter())){
                                attempts.add(attempt);
                                arrayList.add(attempts.size()+" - "+attempt.getAttemptedAt());

                            }
                          }
                        if(selectedCategory.contentEquals("Score")) {
                            for (int i = 0; i < attempts.size(); i++) {
                                series.appendData(
                                        new DataPoint(i + 1,
                                                Integer.parseInt(attempts.get(i).getScore())),
                                        true,
                                        attempts.size());
                            }
                        }else if(selectedCategory.contentEquals("No of Errors")){
                            for (int i = 0; i < attempts.size(); i++) {
                                series.appendData(
                                        new DataPoint(i + 1,
                                                Integer.parseInt(attempts.get(i).getNe())),
                                        true,
                                        attempts.size());
                            }
                        }else if(selectedCategory.contentEquals("Time Taken")){
                            for (int i = 0; i < attempts.size(); i++) {
                                series.appendData(
                                        new DataPoint(i + 1,
                                                Integer.parseInt(String.valueOf(attempts.get(i).getTime().subSequence(0,2)))),
                                        true,
                                        attempts.size());
                            }
                        }else if(selectedCategory.contentEquals("Speed")){
                            for (int i = 0; i < attempts.size(); i++) {
                                series.appendData(
                                        new DataPoint(i + 1,
                                                Integer.parseInt(String.valueOf(attempts.get(i).getSpeed().subSequence(0,2)))),
                                        true,
                                        attempts.size());
                            }
                        }
                        attemptsAdapter.notifyDataSetChanged();
                        graph.addSeries(series);

                    } else {
                        showLog("Error getting documents: "+ task.getException());
                    }
                });

    }


    private void showLog(String data){
        Log.d(TAG, data);

    }



}