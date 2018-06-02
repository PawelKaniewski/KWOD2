package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Appointment.AppointmentActivity;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class MedicineNotifyActivity extends AppCompatActivity {

    private String notifyMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        notifyMessage = intent.getStringExtra("message");
        System.out.println("Odczytana: "+notifyMessage);

        generateNotification();
        Intent appointmentIntent = new Intent(getApplicationContext(),MedicineActivity.class);
        startActivity(appointmentIntent);
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
        notificationManager.notify(1, mBuilder.build());

    }
}
