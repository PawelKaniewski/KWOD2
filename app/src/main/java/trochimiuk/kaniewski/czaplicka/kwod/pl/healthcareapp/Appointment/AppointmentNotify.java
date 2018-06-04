package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Appointment;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class AppointmentNotify extends AppCompatActivity {
    private String notifyMessage;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        notifyMessage = intent.getStringExtra("message");
        id = intent.getIntExtra("notifyID", 1);
        String date = notifyMessage.substring(29,37);
        System.out.println("substring = "+date);
        System.out.println("Odczytana: "+notifyMessage);

        generateNotification();
        Intent appointmentListIntent = new Intent(getApplicationContext(),AppointmentListActivity.class);
        appointmentListIntent.putExtra("clickedDate",date);
        startActivity(appointmentListIntent);
    }

    void generateNotification()
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
                .setContentText(notifyMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, mBuilder.build());

    }
}
