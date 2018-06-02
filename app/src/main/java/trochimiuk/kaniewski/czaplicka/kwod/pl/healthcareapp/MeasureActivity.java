package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * By skończyć działanie tej klasy potrzebne jest przechowywanie gdzieś w pliku kilku wartości
 *
 *  tablicy intów o pojemności 5 - private int[] pomiary = new int[5];
 *  flagi boolean przypominajkaBylaWlaczona
 *  int godzina //uwaga nie pomylic z EditText godziny
 *  int minuta // uwaga jw
 *
 * na samym dole nalezy uzupelnic funkcje init, po nadaniu przechowywanych wartosci należy sie modlic zeby dzialalo xd
 * trzeba tez uzupenil funkcje wykonaj zrzut pamieci. Chodzi o to by w tej funkcji zapisac do pliku wymieniona tam wartosci.
 *
 */

public class MeasureActivity extends AppCompatActivity {
    private final int  ILOSC_POMIAROW = 5;
    private int[] pomiary = new int[5];
    private boolean przypominajkaBylaWlaczona = false;
    private GraphView graph;
    private EditText wynik;
    private EditText godziny;
    private EditText minuty;
    private Button dodajPomiar;
    private Date d1;
    private Date d2;
    private Date d3;
    private Date d4;
    private Date d5;
    private Calendar cal;
    private int godzina;
    private int minuta ;
    private Switch przelacznikPrzypomnienia;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        dodajPomiar = (Button) findViewById(R.id.dodajPomiar);
        wynik = (EditText) findViewById(R.id.wartoscPomiaru);
        graph = (GraphView) findViewById(R.id.graph);
        cal = Calendar.getInstance();
        przelacznikPrzypomnienia = (Switch) findViewById(R.id.przelacznikPrzypomnienia);
        godziny = (EditText)    findViewById(R.id.hours);
        minuty = (EditText) findViewById(R.id.minutes);

        init();

        przelacznikPrzypomnienia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                Log.d("przypomnienia","wlaczono przypomnienie");
                if(isChecked)
                {
                    wlaczPowiadomienia(true);
                }
                else
                {
                    wlaczPowiadomienia(false);
                }
            }
        });












        dodajPomiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wynik.getText().toString().isEmpty() || Integer.parseInt(wynik.getText().toString())>1000 || Integer.parseInt(wynik.getText().toString()) < 10)
                {
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



    private void dodajPomiarDoWykresu(int pomiar)
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
   private  void graphConfig()
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
                new DataPoint(d1, pomiary[0]),
                new DataPoint(d2, pomiary[1]),
                new DataPoint(d3, pomiary[2]),
                new DataPoint(d4, pomiary[3]),
                new DataPoint(d5, pomiary[4])

        });
        graph.addSeries(series);
    }
    void wlaczPowiadomienia(boolean czyWlaczyc) {
        if (czyWlaczyc)
        {
            //TODO pobieranie godziny
            if(Integer.parseInt(godziny.getText().toString())>23 || Integer.parseInt(godziny.getText().toString()) <0)
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Niepoprawnie wpisana godzina", Toast.LENGTH_SHORT);
                toast.show();
                godziny.setText("");
                przelacznikPrzypomnienia.setChecked(false);

            }
            else if( Integer.parseInt(minuty.getText().toString())> 59 ||  Integer.parseInt(minuty.getText().toString()) <0 )
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Niepoprawnie wpisana minuta", Toast.LENGTH_SHORT);
                toast.show();
                minuty.setText("");
                przelacznikPrzypomnienia.setChecked(false);
            }
            else
            {
                godzina = Integer.parseInt(godziny.getText().toString());
                minuta = Integer.parseInt(minuty.getText().toString());
                wyswietlPowiadomienie("Uruchomiono przypominajke");
                scheduler(godzina, minuta);
            }
        }
        else
        {
            schedulerStop();
        }
    }
    void wyswietlPowiadomienie(String message)
    {

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

      //  MojaKlasa.setByloOdtwarzanePowiadomienie(true);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("HealtCare")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mBuilder.build());

    }


    //ta funkcja wywoluje  activity podane w konstruktore intent (w tym przypadku MeasureAtivity.class
    // wykonuje sie to cyklicznie co dobe o podanej godzinie
    void scheduler(int hour, int minute)
    {
        Calendar my_calendar = Calendar.getInstance();
        my_calendar.setTimeInMillis(System.currentTimeMillis());
        my_calendar.set(Calendar.HOUR_OF_DAY, hour);
        my_calendar.set(Calendar.MINUTE, minute);
//my_calendar.set

//        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//         intent = new Intent(this, DoMeasureActivity.class);
//         pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        long currentTime = System.currentTimeMillis();
        long oneMinute = 5 * 1000;
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                my_calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
        przypominajkaBylaWlaczona = true;
        wykonajZrzutPamieci();

    }

void schedulerStop()
{
    alarmManager.cancel(pendingIntent);
}
void switchAndSchedulerConfig()
{
    alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
    intent = new Intent(this, DoMeasureActivity.class);
    pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


    if(przypominajkaBylaWlaczona)
   {
       //scheduler wciąż jest wlaczony zgodnie z poprzednimi ustawieniami
       przelacznikPrzypomnienia.setChecked(true);
       godziny.setText(godzina);
       minuty.setText(minuta);

   }
   else
   {
       schedulerStop();
       przelacznikPrzypomnienia.setChecked(false);
   }
}
void init()
{

  //  pomiary = //wartosc z pliku
  //  przypominajkaBylaWlaczona = //wartosc z pliku
  //  godzina =
  //  minuta =

    graphInitialize();
    graphConfig();
    switchAndSchedulerConfig();

}
void wykonajZrzutPamieci()
{
    //tutaj trzeba zrobic zrzucanie do pliku

    //  = pomiary
    //  = przypominajkaBylaWlaczona
    //  = godzina
    //  = minuta
}


}
