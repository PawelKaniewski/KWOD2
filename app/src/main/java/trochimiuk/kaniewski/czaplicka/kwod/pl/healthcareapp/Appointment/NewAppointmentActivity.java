package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Appointment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class NewAppointmentActivity extends AppCompatActivity{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
    private SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("dd.MM", Locale.getDefault());
    private Button saveAppointmentBtn;
    private EditText dateInsert;
    private EditText timeInsert;
    private EditText doctorInsert;
    private EditText placeInsert;
    private EditText infoInsert;
    private Switch switchReminder;
    private Spinner spinnerReminder;
    private boolean remind = false;
    private Date appointmentDate;
    private String appointmentDay;
    private String appointmentTime;
    private String appointmentDescription;
    private boolean validDay;
    private boolean validTime;
    private boolean validNotifyDate;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent intent;
    private String notifyMessage;
    private String validMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        Intent intent = getIntent();

        dateInsert = (EditText) findViewById(R.id.insertDate);
        dateInsert.setText(intent.getStringExtra("clickedDate"));

        timeInsert = (EditText) findViewById(R.id.insertTime);
        doctorInsert = (EditText) findViewById(R.id.insertDoctor);
        placeInsert = (EditText) findViewById(R.id.insertPlace);
        infoInsert = (EditText) findViewById(R.id.insertInfo);
        saveAppointmentBtn = (Button) findViewById(R.id.saveNewApp);
        switchReminder = (Switch) findViewById(R.id.switchReminder);
        spinnerReminder = (Spinner) findViewById(R.id.spinnerReminder);

        spinnerReminder.setEnabled(false);
        spinnerReminder.setSelection(5);
        initValid();

        saveAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();

                if (validDay && validTime && validNotifyDate) {
                    if (remind) turnOnNotify();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("dateLong", appointmentDate.getTime());
                    resultIntent.putExtra("description", appointmentDescription);
                    /*
                    resultIntent.putExtra("remeindBoolean", remind);
                    if (remind) resultIntent.putExtra("remeindTime", spinnerReminder.getSelectedItemId());
                    */
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), validMessage, Toast.LENGTH_LONG);
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
        validDay = true;
        validTime = true;
        validNotifyDate = true;
    }

    void getData() {
        try {
            appointmentDay = dateInsert.getText().toString();
            validDate();
            dateFormatDay.parse(appointmentDay);
        } catch (ParseException e) {
            e.printStackTrace();
            validDay = false;
            validMessage = validMessage+"Niepoprawna data wizyty. Wymagany format: dd.MM.yy.";
        }
        try {
            appointmentTime = timeInsert.getText().toString();
            dateFormatTime.parse(appointmentTime);
        } catch (ParseException e) {
            e.printStackTrace();
            validTime = false;
            if (!validDay) validMessage = validMessage+"\n";
            validMessage = validMessage+"Niepoprawna godzina wizyty. Wymagany format: HH:mm.";
        }
        if (validDay && validTime) {
            try {
                appointmentDate = dateFormat.parse(appointmentDay + " " + appointmentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date date = appointmentDate;
            date.setTime(date.getTime()-setTimeBeforeDate());
            if (date.getTime() > new Date().getTime()) {
                appointmentDescription = "Lekarz: " + doctorInsert.getText() + "\nMiejsce: " + placeInsert.getText() + "\nOpis: " + infoInsert.getText();
                notifyMessage = "Przypomnienie o wizycie dnia " + appointmentDay + " o godz. " + appointmentTime;
            }
            else {
                if (!validDay || !validTime) validMessage = validMessage+"\n";
                validMessage = validMessage+"Przypomnienie musi być ustawione na przyszłość.";
                validNotifyDate = false;
            }
        }
    }

    void validDate() {
        
    }


    void turnOnNotify() {
        showNotify("Uruchomiono przypomnienie o wizycie u lekarza");
        scheduler(appointmentDate, setTimeBeforeDate());
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

    void scheduler(Date date, long beforeDate)
    {
        intent = new Intent(this, AppointmentNotify.class);
        intent.putExtra("message",notifyMessage);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        System.out.println(date);
        date.setTime(date.getTime()-beforeDate);
        System.out.println(date);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

    }

    void schedulerStop() {
        intent = new Intent(this, AppointmentNotify.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

}
