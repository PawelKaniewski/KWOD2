package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Appointment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.DatabaseHelper;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class EditAppointmentActivity extends AppCompatActivity{

    private DatabaseHelper healthCareDb;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
    private SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("dd.MM", Locale.getDefault());
    private Button saveAppointmentBtn;
    private TextView title;
    private EditText dateInsert;
    private EditText timeInsert;
    private EditText doctorInsert;
    private EditText placeInsert;
    private EditText infoInsert;
    private Switch switchReminder;
    private Spinner spinnerReminder;
    private boolean remind = false;
    private boolean remindBefore;
    private Date appointmentDate;
    private String appointmentDay;
    private String appointmentTime;
    private String appointmentDescription;
    private boolean validDate;
    private boolean validNotifyDate;
    private boolean validDoctor;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent intent;
    private Date notifyDate;
    private String notifyMessage;
    private String validMessage;
    private Cursor eventData;
    private String day;
    private String time;
    private int notifyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        intent = getIntent();
        healthCareDb = new DatabaseHelper(this);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        //long dateL = intent.getLongExtra("dateLong",-1);
        day = intent.getStringExtra("day");
        time = intent.getStringExtra("time");

        title = (TextView) findViewById(R.id.titleAddApp);
        title.setText("Edytuj termin wizyty w kalendarzu");

        dateInsert = (EditText) findViewById(R.id.insertDate);
        dateInsert.setText(day);
        timeInsert = (EditText) findViewById(R.id.insertTime);
        timeInsert.setText(time);

        doctorInsert = (EditText) findViewById(R.id.insertDoctor);
        placeInsert = (EditText) findViewById(R.id.insertPlace);
        infoInsert = (EditText) findViewById(R.id.insertInfo);
        saveAppointmentBtn = (Button) findViewById(R.id.saveNewApp);
        switchReminder = (Switch) findViewById(R.id.switchReminder);
        spinnerReminder = (Spinner) findViewById(R.id.spinnerReminder);

        eventData = healthCareDb.getAppointmentWhereDate(day,time);
        if (eventData.getCount() == 0) {
            Toast.makeText(EditAppointmentActivity.this, "Błąd!", Toast.LENGTH_LONG).show();
        } else {
            while (eventData.moveToNext()) {
                doctorInsert.setText(eventData.getString(3));
                placeInsert.setText(eventData.getString(4));
                infoInsert.setText(eventData.getString(5));
                if (eventData.getString(6).equals("true")) {
                    remind = true;
                    spinnerReminder.setSelection(eventData.getInt(7));
                }
                else {
                    remind = false;
                    spinnerReminder.setSelection(5);
                }
                remindBefore = remind;
                switchReminder.setChecked(remind);
                spinnerReminder.setEnabled(remind);
                notifyID = eventData.getInt(8);
            }
        }

        initValid();
        notifyDate = new Date();

        saveAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();

                if (validDate && validNotifyDate && validDoctor) {
                    turnOnNotify(remind, remindBefore);

                    if(deleteAppointmentFromDB() && addAppointmentToDB()) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("dateLong", appointmentDate.getTime());
                        resultIntent.putExtra("description", appointmentDescription);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), validMessage, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    initValid();
                }
            }
        });


        switchReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remind =!remind;
                System.out.println(remind);
                spinnerReminder.setEnabled(remind);
            }
        });

    }

    void initValid()
    {
        validMessage = "";
        validDate = true;
        validNotifyDate = true;
        validDoctor = true;
    }

    void getData() {
        try {
            appointmentDay = dateInsert.getText().toString();
            dateFormatDay.parse(appointmentDay);
            validDate();
        } catch (ParseException e) {
            e.printStackTrace();
            validMessage = validMessage+"Niepoprawna data wizyty. Wymagany format: dd.MM.yy.";
            validDate = false;
        }
        try {
            appointmentTime = timeInsert.getText().toString();
            dateFormatTime.parse(appointmentTime);
            validTime();
        } catch (ParseException e) {
            e.printStackTrace();
            if (!validDate) validMessage = validMessage+"\n";
            validMessage = validMessage+"Niepoprawna godzina wizyty. Wymagany format: HH:mm.";
            validDate = false;
        }
        if (validDate) {
            try {
                appointmentDate = dateFormat.parse(appointmentDay + " " + appointmentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (appointmentDate.getTime() > new Date().getTime()) {
                if (doctorInsert.getText().toString().equals("")) {
                    if (!validDate) validMessage = validMessage+"\n";
                    validMessage = validMessage+"Pole \"Lekarz\" nie może być puste.";
                    validDoctor = false;
                }
                appointmentDescription = "Lekarz: " + doctorInsert.getText() + "\nMiejsce: " + placeInsert.getText() + "\nOpis: " + infoInsert.getText();
            }
            else {
                validMessage = validMessage+"Wizyta musi zostać ustawiona na przyszłość.";
                validDate = false;
            }
            if (remind) {
                notifyDate.setTime(appointmentDate.getTime()-setTimeBeforeDate());
                if (notifyDate.getTime() > new Date().getTime())
                    notifyMessage = "Przypomnienie o wizycie dnia " + appointmentDay + " o godz. " + appointmentTime;
                else {
                    if (!validDate) validMessage = validMessage+"\n";
                    validMessage = validMessage+"Przypomnienie musi zostać ustawione na przyszłość.";
                    validNotifyDate = false;
                }
            }

        }
    }

    void validDate() {
        if (appointmentDay.length()==8) {
            int day = Integer.parseInt(appointmentDay.substring(0,2));
            int month = Integer.parseInt(appointmentDay.substring(3,5));
            System.out.println("data: "+appointmentDay.substring(0,2)+"."+appointmentDay.substring(3,5));
            System.out.println("data: "+day+"."+month);
            if (day<1 || day>31 || month<1 || month>12) {
                validMessage = validMessage+"Niepoprawna data wizyty. Wymagany format: dd.MM.yy.";
                validDate = false;
            }
        }
        else {
            validMessage = validMessage+"Niepoprawna data wizyty. Wymagany format: dd.MM.yy.";
            validDate = false;
        }
    }

    void validTime() {
        if (appointmentTime.length()==5) {
            int hour = Integer.parseInt(appointmentTime.substring(0,2));
            int min = Integer.parseInt(appointmentTime.substring(3,5));
            System.out.println("godzina: "+appointmentTime.substring(0,2)+":"+appointmentTime.substring(3,5));
            System.out.println("godzina: "+hour+":"+min);
            if (hour<0 || hour>23 || min<0 || min>59) {
                if (!validDate) validMessage = validMessage+"\n";
                validMessage = validMessage+"Niepoprawna godzina wizyty. Wymagany format: HH:mm.";
                validDate = false;
            }
        }
        else {
            if (!validDate) validMessage = validMessage+"\n";
            validMessage = validMessage+"Niepoprawna godzina wizyty. Wymagany format: HH:mm.";
            validDate = false;
        }
    }


    void turnOnNotify(boolean onOff, boolean onBefore) {
        //TODO Usuwanie powiadomienia
        if (onBefore && notifyID!=100) {
            schedulerStop(notifyID);
            showNotify("Wyłączono przypomnienie o wizycie u lekarza");
        }
        if (onOff) {
            showNotify("Uruchomiono przypomnienie o wizycie u lekarza");
            scheduler(notifyDate);
        }
        else notifyID=100;

    }

    long setTimeBeforeDate() {
        long timeBefore = 0;
        switch(spinnerReminder.getSelectedItemPosition()) {
            case 0:
                timeBefore=2*7*24*3600*1000;
                break;
            case 1:
                timeBefore=7*24*3600*1000;
                break;
            case 2:
                timeBefore=5*24*3600*1000;
                break;
            case 3:
                timeBefore=3*24*3600*1000;
                break;
            case 4:
                timeBefore=2*24*3600*1000;
                break;
            case 5:
                timeBefore=24*3600*1000;
                break;
            case 6:
                timeBefore=5*3600*1000;
                break;
            case 7:
                timeBefore=2*3600*1000;
                break;
            case 8:
                timeBefore=3600*1000;
                break;
        }
        return timeBefore;
    }


    void showNotify(String message)
    {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("HealtCare")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mBuilder.build());
    }

    void scheduler(Date date)
    {
        notifyID = (int)System.currentTimeMillis();
        intent = new Intent(this, AppointmentNotify.class);
        intent.putExtra("message",notifyMessage);
        intent.putExtra("notifyID",notifyID);
        pendingIntent = PendingIntent.getActivity(this, notifyID, intent, PendingIntent.FLAG_ONE_SHOT);

        //alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

    }

    void schedulerStop(int id) {
        intent = new Intent(this, AppointmentNotify.class);
        pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }


    boolean addAppointmentToDB() {
        boolean insertData = healthCareDb.addAppointmentToDB(appointmentDay,appointmentTime,
                doctorInsert.getText().toString(),placeInsert.getText().toString(),infoInsert.getText().toString(),
                Boolean.toString(remind),spinnerReminder.getSelectedItemPosition(),notifyID);
        if (!insertData) Toast.makeText(this, "Wystapił błąd zapisu edytowanej wizyty", Toast.LENGTH_LONG).show();
        return insertData;
    }

    boolean deleteAppointmentFromDB() {
        boolean deleteOK = false;
        eventData = healthCareDb.getAppointmentWhereDate(day,time);
        if (eventData.getCount() == 0) {
            Toast.makeText(EditAppointmentActivity.this, "Wystąpił błąd usunięcia edytowanej wizyty!", Toast.LENGTH_LONG).show();
        } else {
            while (eventData.moveToNext()) {
                int id = eventData.getInt(0);
                deleteOK = healthCareDb.deleteAppointmentByID(id);
            }
        }
        return deleteOK;
    }

}
