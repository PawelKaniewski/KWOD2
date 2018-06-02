package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Appointment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class AppointmentNotify extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            generateNotification(context);
        }catch(Exception e){
            e.printStackTrace();
        }
        Intent appointmentIntent = new Intent(context,NewAppointmentActivity.class);
        context.startActivity(appointmentIntent);
    }

    void generateNotification(Context context)
    {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "Channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("HealtCare")
                .setContentText("Przypomnienie o wizycie!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, mBuilder.build());

        /*
        Toast toast = Toast.makeText(getApplicationContext(), "Wykonaj pomiar cukru", Toast.LENGTH_LONG);
        toast.show();
        */
    }
}
