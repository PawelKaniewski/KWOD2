package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MeasureActivity extends AppCompatActivity {
    private final int  ILOSC_POMIAROW = 5;
    private int[] pomiary = new int[5];
    private GraphView graph;
    private EditText wynik;
    private Button dodajPomiar;
    Date d1;
    Date d2;
    Date d3;
    Date d4;
    Date d5;

    Calendar cal = Calendar.getInstance();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        dodajPomiar = (Button) findViewById(R.id.dodajPomiar);
        wynik = (EditText) findViewById(R.id.wartoscPomiaru);
        graph = (GraphView) findViewById(R.id.graph);

        graphInitialize();
        graphConfig();


        dodajPomiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wynik.getText().toString().isEmpty() || Integer.parseInt(wynik.getText().toString())>1000 || Integer.parseInt(wynik.getText().toString()) < 10)
                {
                  //Log.d("pomiar","is empty");
                    Toast toast = Toast.makeText(getApplicationContext(), "Błędna wartość", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    dodajPomiarDoWykresu(Integer.parseInt(wynik.getText().toString())); // dodanie do wykresu
                }

                //ponizej dwie linijki chowaja klawiature
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                //usuniecie wpisanej wartosci
                wynik.setText("");

            }
        });
    }



    protected void dodajPomiarDoWykresu(int pomiar)
    {
        for(int i = 0; i<=ILOSC_POMIAROW - 2; i++)
        {
            pomiary[i] = pomiary[i + 1]; //stare pomiary przesuwaja sie o jeden w lewo (najstarszy jest tracony)
        }
        pomiary[ILOSC_POMIAROW - 1] = pomiar; //nowy pomiar jest wpisywany na ostatnie miejsce

        LineGraphSeries<DataPoint> nowaSeria = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, pomiary[0]),
                new DataPoint(d2, pomiary[1]),
                new DataPoint(d3, pomiary[2]),
                new DataPoint(d4, pomiary[3]),
                new DataPoint(d5, pomiary[4])

        });
        graph.removeAllSeries();
        graph.addSeries(nowaSeria);
        graphConfig();
    }
    void graphConfig()
    {
        DateFormat dateformat =  new SimpleDateFormat("dd.MM");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(),dateformat ) );
        graph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 4 because of the space
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d5.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false,true);
    }
    void graphInitialize()
    {
        cal.add(Calendar.DATE, -4);
        d1 = cal.getTime();
        cal.add(Calendar.DATE, 1);
        d2 = cal.getTime();
        cal.add(Calendar.DATE, 1);
        d3 = cal.getTime();
        cal.add(Calendar.DATE, 1);
        d4 = cal.getTime();
        cal.add(Calendar.DATE, 1);
        d5 = cal.getTime();


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 0),
                new DataPoint(d2, 0),
                new DataPoint(d3, 0),
                new DataPoint(d4, 0),
                new DataPoint(d5, 0)

        });
        graph.addSeries(series);
    }
}
