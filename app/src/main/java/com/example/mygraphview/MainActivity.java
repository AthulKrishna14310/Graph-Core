package com.example.mygraphview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GraphView graph=(GraphView) findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series =
                new LineGraphSeries<>();
        double y=0;


        for (int x=0;x<90;x++){
            y++;
            series.appendData(new DataPoint(x,y), true,90);

        }
        graph.addSeries(series);
        series.setColor(Color.RED);
        series.setTitle("Score");

        series.setDataPointsRadius(16);
        series.setThickness(8);
        series.setAnimated(true);
        graph.setTitle("Score Graph");
        graph.setTitleTextSize(90);
        graph.setTitleColor(Color.BLUE);


        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        GridLabelRenderer gridLabel=graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Date");
        gridLabel.setHorizontalAxisTitleTextSize(50);
        gridLabel.setVerticalAxisTitle("Score");
        gridLabel.setVerticalAxisTitleTextSize(50);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Letter));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        Spinner mySpinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Type));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);

        listView=(ListView)findViewById(R.id.listview);
        ArrayList<String> arrayList=new ArrayList<>();

        arrayList.add("80 12/10/20 2:55");
        arrayList.add("60 12/10/20 2:55");
        arrayList.add("40 12/10/20 2:55");
        arrayList.add("20 12/10/20 2:55");
        ArrayAdapter arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

    }

}