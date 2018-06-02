package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.DatabaseHelper;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Measure.DoMeasureActivity;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class NewMedicineActivity extends AppCompatActivity {

    private DatabaseHelper healthCareDb;
    private Spinner medicineSpinner;
    private List<String> names;
    private List<Medicine> medicinesList;
    private Button addMedicineBtn, saveMedicine;
    private EditText frequency, portion, unit;
    private int selectedMed;

    private EditText medNotHours;
    private EditText medNotMin;
    private int hours;
    private int min;
    private Switch medNotSwitch;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent intent;
    private boolean wasNotOn = false;
    private boolean remind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);

        this.initAllFields();

        medNotSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remind =!remind;
                System.out.println(remind);
                medNotSwitch.setEnabled(remind);
            }
        });

        addMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineIntent = new Intent(getApplicationContext(), NewMedicineInDBActivity.class);
                startActivity(medicineIntent);
            }
        });

        saveMedicine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (remind) turnOnNotifications(true);
                CustomizedMedicine customizedMedicine = new CustomizedMedicine(-1,medicinesList.get(selectedMed),Integer.parseInt(frequency.getText().toString()),Integer.parseInt(portion.getText().toString()),unit.getText().toString());
                saveNewCustomMedicineToDB(customizedMedicine);
            }

        });


        healthCareDb = new DatabaseHelper(this);
        medicinesList = new ArrayList<>();
        names = new ArrayList<>();
        this.createMedicineDropDownList();

        medicineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos != 0) {
                    saveMedicine.setEnabled(true);
                    frequency.setEnabled(true);
                    portion.setEnabled(true);
                    unit.setEnabled(true);
                    selectedMed=pos-1;
                    Toast.makeText(NewMedicineActivity.this, parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    void turnOnNotifications(boolean ifOn) {
        if (ifOn)
        {
            if(Integer.parseInt(medNotHours.getText().toString())>23 || Integer.parseInt(medNotHours.getText().toString()) <0 || medNotHours.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Niepoprawnie wpisana godzina", Toast.LENGTH_SHORT).show();
                medNotHours.setText("");
                medNotSwitch.setChecked(false);

            }
            else if( Integer.parseInt(medNotMin.getText().toString())> 59 ||  Integer.parseInt(medNotMin.getText().toString()) <0 || medNotMin.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Niepoprawnie wpisana minuta", Toast.LENGTH_SHORT).show();
                medNotMin.setText("");
                medNotSwitch.setChecked(false);
            }
            else {
                hours = Integer.parseInt(medNotHours.getText().toString());
                min = Integer.parseInt(medNotMin.getText().toString());
                displayNotification("Uruchomiono przypominajke");
                scheduler(hours, min);
            }
        }
        else {
            schedulerStop();
        }
    }
    void displayNotification(String message)
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

    void scheduler(int hour, int minute)
    {
        Calendar my_calendar = Calendar.getInstance();
        my_calendar.setTimeInMillis(System.currentTimeMillis());
        my_calendar.set(Calendar.HOUR_OF_DAY, hour);
        my_calendar.set(Calendar.MINUTE, minute);

        long currentTime = System.currentTimeMillis();
        long oneMinute = 5 * 1000;
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                my_calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
        wasNotOn = true;
        //wykonajZrzutPamieci();

    }

    void schedulerStop()
    {
        alarmManager.cancel(pendingIntent);
    }
    void switchAndSchedulerConfig() {
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        intent = new Intent(this, MedicineNotifyActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        if(wasNotOn) {
            //scheduler wciąż jest wlaczony zgodnie z poprzednimi ustawieniami
            medNotSwitch.setChecked(true);
            medNotHours.setText(hours);
            medNotMin.setText(min);
        }
        else {
            schedulerStop();
            medNotSwitch.setChecked(false);
        }
    }



    private void saveNewCustomMedicineToDB(CustomizedMedicine customizedMedicine){
        boolean insertData = healthCareDb.addNewCustomMedicineToDB(customizedMedicine);
        if (insertData == true) {
            //Intent medicineIntent = new Intent(getApplicationContext(), NewMedicineActivity.class);
            //medicineIntent.putExtra("dbSuccess", "Lek dodany do bazy danych!");
           // startActivity(medicineIntent);
            Toast.makeText(this, "NOWY LEK Z DANYMI O DAWKOWANIU DODANY DO BAZY!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Wystapił błąd", Toast.LENGTH_LONG).show();
        }
    }

    private void initAllFields() {
        this.addMedicineBtn = (Button) findViewById(R.id.addNewMedBtn);
        this.saveMedicine = (Button) findViewById(R.id.saveMed);
        this.saveMedicine.setEnabled(false);
        this.frequency = (EditText) findViewById(R.id.frequency);
        this.portion = (EditText) findViewById(R.id.portion);
        this.unit = (EditText) findViewById(R.id.unit);
        this.medicineSpinner = (Spinner) findViewById(R.id.medicineSpinner);
        frequency.setEnabled(false);
        portion.setEnabled(false);
        unit.setEnabled(false);
        medNotHours = (EditText) findViewById(R.id.medNotHours);
        medNotMin = (EditText) findViewById(R.id.medNotMin);
        medNotSwitch = (Switch) findViewById(R.id.medNotSwitch);
        switchAndSchedulerConfig();

    }

    private void createMedicineDropDownList() {
        Cursor data = healthCareDb.getListContents();

        if (data.getCount() == 0) {
            Toast.makeText(this, "Brak leków w bazie! Zdefiniuj lek!", Toast.LENGTH_LONG).show();
        } else {
            names.add("Wybierz nazwę leku");
            while (data.moveToNext()) {
                names.add(data.getString(1));
                medicinesList.add(new Medicine(data.getString(1), data.getString(2)));

                final ArrayAdapter<String> medicineAdapter = new ArrayAdapter<String>(NewMedicineActivity.this, android.R.layout.simple_spinner_item, names) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.GRAY);
                        return view;
                    }
                };

                medicineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                medicineSpinner.setAdapter(medicineAdapter);
            }

        }
    }

}
