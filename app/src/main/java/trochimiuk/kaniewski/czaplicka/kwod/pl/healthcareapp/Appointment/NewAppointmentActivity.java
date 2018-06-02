package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Appointment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class NewAppointmentActivity extends AppCompatActivity{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
    //private SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
    //private SimpleDateFormat dateFormatDay = new SimpleDateFormat("dd.MM", Locale.getDefault());
    private Button saveAppointmentBtn;
    private EditText dateInsert;
    private EditText timeInsert;
    private EditText doctorInsert;
    private EditText placeInsert;
    private EditText infoInsert;
    private Switch switchRemeinder;
    private Spinner spinnerRemeinder;
    private boolean remeind = false;
    private Date appointmentDate;
    private String appointmentDay;
    private String appointmentTime;
    private String appointmentDescription;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent intent;
    private String notifyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        dateInsert = (EditText) findViewById(R.id.insertDate);
        dateInsert.setText(AppointmentActivity.getClickedDate().getText());

        timeInsert = (EditText) findViewById(R.id.insertTime);
        doctorInsert = (EditText) findViewById(R.id.insertDoctor);
        placeInsert = (EditText) findViewById(R.id.insertPlace);
        infoInsert = (EditText) findViewById(R.id.insertInfo);
        saveAppointmentBtn = (Button) findViewById(R.id.saveNewApp);
        switchRemeinder = (Switch) findViewById(R.id.switchRemeinder);
        spinnerRemeinder = (Spinner) findViewById(R.id.spinnerRemeinder);

        spinnerRemeinder.setEnabled(false);

        saveAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    appointmentDay = dateInsert.getText().toString();
                    appointmentTime = timeInsert.getText().toString();
                    appointmentDate = dateFormat.parse(dateInsert.getText().toString()+" "+timeInsert.getText().toString());
                    System.out.println(appointmentDate.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                appointmentDescription = "Lekarz: "+doctorInsert.getText()+"\nMiejsce: "+placeInsert.getText()+"\nOpis: "+infoInsert.getText();
                notifyMessage = "Przypomnienie o wizycie dnia "+appointmentDay+" o godz. "+appointmentTime;
                turnOnNotify(remeind);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("dateLong",appointmentDate.getTime());
                resultIntent.putExtra("description",appointmentDescription);
                resultIntent.putExtra("remeindBoolean",remeind);
                if (remeind) {
                    resultIntent.putExtra("remeindTime",spinnerRemeinder.getPrompt());
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });


        switchRemeinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remeind=!remeind;
                System.out.println(remeind);
                spinnerRemeinder.setEnabled(remeind);
            }
        });

    }


    void turnOnNotify(boolean turnOnOff) {
        if (turnOnOff)
        {
            showNotify("Uruchomiono przypomnienie o wizycie u lekarza");
            scheduler(appointmentDate);
        }
        else
        {
            schedulerStop();
        }
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
        intent = new Intent(this, AppointmentNotify.class);
        intent.putExtra("message",notifyMessage);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        System.out.println(date.getTime());
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

    }

    void schedulerStop() {
        intent = new Intent(this, AppointmentNotify.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

}
