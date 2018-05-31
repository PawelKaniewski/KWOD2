package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

public class AppointmentActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView myDate;
    Button addAppointmentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        myDate = (TextView) findViewById(R.id.myDate);
        addAppointmentBtn = (Button) findViewById(R.id.addAppBtn);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = "Data: "+dayOfMonth+"/"+month+"/"+year;
                myDate.setText(date);
            }
        });


    }
}
