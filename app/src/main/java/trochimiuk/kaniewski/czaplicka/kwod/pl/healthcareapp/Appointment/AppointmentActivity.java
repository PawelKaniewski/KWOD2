package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Appointment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.DatabaseHelper;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class AppointmentActivity extends AppCompatActivity {

    private DatabaseHelper healthCareDb;
    private static CompactCalendarView calendarView;
    private SimpleDateFormat dateFormatFull = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
    private TextView clickedDate;
    private Date prevDateClicked;
    private Button addAppointmentBtn;
    private Button prevMonthBtn;
    private Button nextMonthBtn;
    private TextView acctualMonth;
    private List<Event> events;
    //private Event event;
    private RecyclerView eventsRecycle;
    private AppointmentsList appointmentsAdapter;
    private int eventColor;


    public static CompactCalendarView getCalendarView() {
        return calendarView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate method");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        healthCareDb = new DatabaseHelper(this);

        calendarView = (CompactCalendarView) findViewById(R.id.calendarView);
        clickedDate = (TextView) findViewById(R.id.myDate);
        acctualMonth = (TextView) findViewById(R.id.acctualMonth);
        addAppointmentBtn = (Button) findViewById(R.id.addAppBtn);
        prevMonthBtn = (Button) findViewById(R.id.prevMonthBtn);
        nextMonthBtn = (Button) findViewById(R.id.nextMonthBtn);
        eventsRecycle = (RecyclerView) findViewById(R.id.appointmentsList);

        clickedDate.setText(dateFormat.format(new Date()));
        acctualMonth.setText(dateFormatMonth.format(new Date()));

        eventColor = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);

        Date today = new Date();
        Event event = new Event(eventColor, today.getTime(), "description\nlinia\nlinia");
        calendarView.addEvent(event);
        event = new Event(eventColor, today.getTime(), "description\n123\n123");
        calendarView.addEvent(event);

        events = calendarView.getEvents(new Date());
        appointmentsAdapter = new AppointmentsList(events);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        eventsRecycle.setLayoutManager(mLayoutManager);
        eventsRecycle.setItemAnimator(new DefaultItemAnimator());
        eventsRecycle.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        eventsRecycle.setAdapter(appointmentsAdapter);
        //appointmentsAdapter.notifyDataSetChanged();

        calendarView.setUseThreeLetterAbbreviation(false);
        loadEvents();

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                System.out.println("dateClicked:"+dateClicked);
                System.out.println("prevDateClicked:"+prevDateClicked);
                if (!dateClicked.equals(prevDateClicked)) {
                    System.out.println("kliknięcie daty 1. raz");
                    clickedDate.setText(dateFormat.format(dateClicked));
                    showListOfEvents(dateClicked);
                    prevDateClicked = dateClicked;
                }
                else {
                    System.out.println("kliknięcie daty 2. raz !!!");
                    Intent appointmentListIntent = new Intent(getApplicationContext(),AppointmentListActivity.class);
                    appointmentListIntent.putExtra("clickedDate",clickedDate.getText().toString());
                    startActivity(appointmentListIntent);
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                acctualMonth.setText(dateFormatMonth.format(firstDayOfNewMonth));
                clickedDate.setText(dateFormat.format(firstDayOfNewMonth));
                showListOfEvents(firstDayOfNewMonth);

            }
        });

        addAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appointmentIntent = new Intent(getApplicationContext(),NewAppointmentActivity.class);
                appointmentIntent.putExtra("clickedDate",clickedDate.getText());
                startActivityForResult(appointmentIntent, 1);
            }

        });

        prevMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.showPreviousMonth();
            }
        });

        nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.showNextMonth();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (1) : {
                if (resultCode==Activity.RESULT_OK){
                    System.out.println("zapis wydarzenia!");
                    long dateL = data.getLongExtra("dateLong",-1);
                    Event event = new Event(eventColor, dateL, data.getStringExtra("description"));
                    calendarView.addEvent(event);
                    try {
                        showListOfEvents(dateFormat.parse(clickedDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    public void showListOfEvents(Date date) {
        events = calendarView.getEvents(date);
        appointmentsAdapter = new AppointmentsList(events);
        eventsRecycle.setAdapter(appointmentsAdapter);
    }

    public void loadEvents() {
        Cursor data = healthCareDb.getAppointmentsListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "Brak wizyt w bazie! Dodaj wizytę!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                String appointmentDay = data.getString(1);
                String appointmentTime = data.getString(2);
                Date appointmentDate = new Date();
                try {
                    appointmentDate = dateFormatFull.parse(appointmentDay + " " + appointmentTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String appointmentDescription = "Lekarz: "+data.getString(3)+ "\nMiejsce: " +data.getString(4)+"\nOpis: "+data.getString(5);
                Event event = new Event(eventColor,appointmentDate.getTime(),appointmentDescription);
                calendarView.addEvent(event);
            }
        }
    }

}
