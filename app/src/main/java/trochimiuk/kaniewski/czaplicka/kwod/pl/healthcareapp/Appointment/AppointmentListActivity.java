package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Appointment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

public class AppointmentListActivity extends AppCompatActivity implements AppointmentsListBig.SingleClickListener {
    private DatabaseHelper healthCareDb;
    private CompactCalendarView calendarView;
    private SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
    private Button addBtn;
    private Button editBtn;
    private Button deleteBtn;
    private TextView titleDate;
    private List<Event> events;
    private int[] remindTime = new int[]{1,2,3,4,5,6,7,8,9,10};
    private RecyclerView eventsRecycle;
    private AppointmentsListBig appointmentsAdapter;
    private int selectedPosition;
    private int eventColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_list);
        healthCareDb = new DatabaseHelper(this);
        calendarView = AppointmentActivity.getCalendarView();

        Intent intent = getIntent();
        Date date = new Date();
        try {
            date = dateFormatDay.parse(intent.getStringExtra("clickedDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        titleDate = (TextView) findViewById(R.id.myDate);
        titleDate.setText(intent.getStringExtra("clickedDate"));

        addBtn = (Button) findViewById(R.id.addAppBtn);
        editBtn = (Button) findViewById(R.id.editAppBtn);
        editBtn.setEnabled(false);
        deleteBtn = (Button) findViewById(R.id.deleteAppBtn);
        deleteBtn.setEnabled(false);
        eventsRecycle = (RecyclerView) findViewById(R.id.appointmentsList);
        eventColor = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        eventsRecycle.setLayoutManager(mLayoutManager);
        eventsRecycle.setItemAnimator(new DefaultItemAnimator());
        eventsRecycle.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        eventsRecycle.setAdapter(appointmentsAdapter);

        showListOfEvents(date);

        appointmentsAdapter.setOnItemClickListener(this);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appointmentIntent = new Intent(getApplicationContext(),NewAppointmentActivity.class);
                appointmentIntent.putExtra("clickedDate",titleDate.getText());
                startActivityForResult(appointmentIntent, 1);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (1) : {
                if (resultCode== Activity.RESULT_OK){
                    System.out.println("zapis wydarzenia!");
                    long dateL = data.getLongExtra("dateLong",-1);
                    Event event = new Event(eventColor, dateL, data.getStringExtra("description"));
                    calendarView.addEvent(event);
                    try {
                        showListOfEvents(dateFormatDay.parse(titleDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                break;
            }
        }
    }

    public void createRemindTimeArray() {
        for (int i=0;i<events.size();i++)
        {
            Event event = events.get(i);
            String day = dateFormatDay.format(event.getTimeInMillis());
            String time = dateFormatTime.format(event.getTimeInMillis());
            Cursor data = healthCareDb.getAppointmentWhereDate(day,time);
            if (data.getCount() == 0) {
                Toast.makeText(this, "Błąd!", Toast.LENGTH_LONG).show();
            } else {
                while (data.moveToNext()) {
                    boolean remind = Boolean.getBoolean(data.getString(6));
                    System.out.println("remind"+remind);
                    if (remind) remindTime[i] = data.getInt(7);
                    else remindTime[i] = 10;
                }
            }
        }
    }

    public void showListOfEvents(Date date) {
        events = calendarView.getEvents(date);
        createRemindTimeArray();
        appointmentsAdapter = new AppointmentsListBig(events, remindTime);
        eventsRecycle.setAdapter(appointmentsAdapter);
    }

    @Override
    public void onItemClickListener(int position, View view) {
        appointmentsAdapter.selectedItem();
        this.selectedPosition = position;
        editBtn.setEnabled(true);
        editBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
    }
}
