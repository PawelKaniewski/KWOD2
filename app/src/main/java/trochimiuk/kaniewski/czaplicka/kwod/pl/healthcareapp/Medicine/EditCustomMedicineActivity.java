package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.DatabaseHelper;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class EditCustomMedicineActivity extends AppCompatActivity {

    private EditText editFrequency;
    private EditText medicinePortionEdit, medicineUnitEdit, medicineEditHour, medicineEditMin;
    private Button saveEditBtn,cancelBtn;
    private DatabaseHelper healthCareDb;
    private Switch medicineSwitchEdit;
    private PendingIntent pendingIntent;
    private Intent intent;
    private String notifyMessage;
    private boolean remind = false;
    private boolean remindBefore;
    private AlarmManager alarmManager;
    private int notifyHour, notifyMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custom_medicine);

        saveEditBtn = (Button) findViewById(R.id.saveEditBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        medicineSwitchEdit = (Switch) findViewById(R.id.medicineSwitchEdit);
        medicineEditHour = (EditText) findViewById(R.id.medicineEditHour);
        medicineEditMin = (EditText) findViewById(R.id.medicineEditMin);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineIntent = new Intent(getApplicationContext(), MedicineActivity.class);
                startActivity(medicineIntent);
            }
        });

        medicineSwitchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remind =!remind;
                medicineSwitchEdit.setChecked(remind);
            }
        });

        healthCareDb = new DatabaseHelper(this);
        final TextView medicineNameEdit = (TextView) findViewById(R.id.medicineNameEdit);
        this.editFrequency = (EditText) findViewById(R.id.editFrequency);
        this.medicinePortionEdit = (EditText) findViewById(R.id.medicinePortionEdit);
        medicineUnitEdit = (EditText) findViewById(R.id.medicineEditUnit);
        Bundle extras = getIntent().getExtras();
        final int medicineId = Integer.parseInt(extras.getString("medicineEditId"));
        final String description = extras.getString("medicineDescriptionEdit");
        medicineNameEdit.setText(extras.getString("medicineNameEdit"));
        editFrequency.setText(extras.getString("medicineFrequencyEdit"));
        medicinePortionEdit.setText(extras.getString("medicinePortionEdit"));
        medicineUnitEdit.setText(extras.getString("medicineUnitEdit"));
        medicineSwitchEdit.setChecked(Boolean.parseBoolean(extras.getString("medicineRemindOn")));

        if(Boolean.parseBoolean(extras.getString("medicineRemindOn"))) remind = true;
        else remind = false;
        remindBefore = remind;
        medicineEditHour.setText(extras.getString("medicineHours"));
        medicineEditMin.setText(extras.getString("medicineMins"));
        saveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editFrequency.getText().toString()!="" || medicinePortionEdit.getText().toString()!="" || medicineUnitEdit.getText().toString()!="" || medicineEditHour.getText().toString()!="" ||
                        medicineEditMin.getText().toString()!="" ) {
                    Medicine medicine = new Medicine(medicineNameEdit.getText().toString(), description);
                    CustomizedMedicine customizedMedicine = new CustomizedMedicine(medicineId, medicine, Integer.parseInt(editFrequency.getText().toString()),
                            Integer.parseInt(medicinePortionEdit.getText().toString()), medicineUnitEdit.getText().toString(), medicineSwitchEdit.isChecked(),
                            Integer.parseInt(medicineEditHour.getText().toString()), Integer.parseInt(medicineEditMin.getText().toString()));
                    boolean result = healthCareDb.updateCustomMedicine(customizedMedicine);
                    if (remind) {
                        notifyMessage = "Przypomnienie: zażyj lek o nazwie " + customizedMedicine.getMedicine().getName();
                        notifyHour = customizedMedicine.getHours();
                        notifyMin = customizedMedicine.getMins();
                    }
                    if (result == false) {
                        Toast.makeText(EditCustomMedicineActivity.this, "Błąd edycji!", Toast.LENGTH_LONG).show();
                    } else {
                        turnOnNotify(remind, remindBefore);
                        Intent medicineIntent = new Intent(getApplicationContext(), MedicineActivity.class);
                        startActivity(medicineIntent);
                        Toast.makeText(EditCustomMedicineActivity.this, "Lek został edytowany", Toast.LENGTH_LONG).show();
                    }
                }
                else Toast.makeText(EditCustomMedicineActivity.this, "Wprowadź wszystkie wartości", Toast.LENGTH_LONG).show();
            }
        });
    }

    void turnOnNotify(boolean onOff, boolean onBefore) {
        if (onOff) {
            showNotify("Uruchomiono przypomnienie o zażyciu leku");
            scheduler(notifyHour, notifyMin);
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

    void scheduler(int notifyHour, int notifyMin)
    {
        Calendar my_callendar = Calendar.getInstance();
        my_callendar.setTimeInMillis(System.currentTimeMillis());
        my_callendar.set(Calendar.HOUR_OF_DAY, notifyHour);
        my_callendar.set(Calendar.MINUTE, notifyMin);
        intent = new Intent(this, MedicineNotifyActivity.class);
        intent.putExtra("message", notifyMessage);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, my_callendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    void schedulerStop() {
        //intent = new Intent(beforeContext, MedicineNotifyActivity.class);
        //pendingIntent = PendingIntent.getActivity(beforeContext, 0, intent, 0);
        //alarmManager.cancel(pendingIntent);
    }



}
