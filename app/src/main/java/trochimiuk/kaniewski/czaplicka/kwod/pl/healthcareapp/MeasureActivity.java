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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MeasureActivity extends AppCompatActivity {
    private final int  ILOSC_POMIAROW = 5;
    private int[] pomiary = new int[5];
    private GraphView graph;
    private EditText wynik;
    Button dodajPomiar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        dodajPomiar = (Button) findViewById(R.id.dodajPomiar);
        wynik = (EditText) findViewById(R.id.wartoscPomiaru);
        graph = (GraphView) findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, 0),
                new DataPoint(2, 0),
                new DataPoint(3, 0),
                new DataPoint(4, 0),
                new DataPoint(5, 0)
        });
        graph.addSeries(series);



        dodajPomiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO funkcja do wywolania

                if (wynik.getText().toString().isEmpty())
                {
                  Log.d("pomiar","is empty");
                }
                else
                {
                    dodajPomiarDoWykresu(Integer.parseInt(wynik.getText().toString()));
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
        Log.d("POMIAR","test");
        for(int i = 0; i<=ILOSC_POMIAROW - 2; i++)
        {
            pomiary[i] = pomiary[i + 1]; //stare pomiary przesuwaja sie o jeden w lewo (najstarszy jest tracony)

            Log.d("POMIAR", Integer.toString(pomiary[i]) + "   i: " + Integer.toString(i));
        }
        pomiary[ILOSC_POMIAROW - 1] = pomiar; //nowy pomiar jest wpisywany na ostatnie miejsce
        Log.d("POMIAR", Integer.toString(pomiary[ILOSC_POMIAROW-1]));


        LineGraphSeries<DataPoint> nowaSeria = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, pomiary[0]),
                new DataPoint(1, pomiary[1]),
                new DataPoint(2, pomiary[2]),
                new DataPoint(3, pomiary[3]),
                new DataPoint(4, pomiary[4])
              //  new DataPoint(5, pomiary[5])
        });
        graph.removeAllSeries();
        graph.addSeries(nowaSeria);
    }
}
