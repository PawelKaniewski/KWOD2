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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.DatabaseHelper;
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
    private Switch medNotSwitch;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent intent;
    private boolean remind = false;
    private String notifyMessage;
    int notifyHour, notifyMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);
        intent = getIntent();
        this.initAllFields();

        medNotSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remind = !remind;
                medNotSwitch.setChecked(remind);
            }
        });

        addMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineIntent = new Intent(getApplicationContext(), NewMedicineInDBActivity.class);
                startActivity(medicineIntent);
            }
        });

        saveMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(frequency.getText().toString()!="" || portion.getText().toString()!="" || unit.getText().toString()!="" || medNotHours.getText().toString()!="" ||
                        medNotMin.getText().toString()!="" ) {
                    if (remind) {
                        if (checkHoursAndMins()) {
                            CustomizedMedicine customizedMedicine = new CustomizedMedicine(-1, medicinesList.get(selectedMed), Integer.parseInt(frequency.getText().toString()),
                                    Integer.parseInt(portion.getText().toString()), unit.getText().toString(), remind, Integer.parseInt(medNotHours.getText().toString()),
                                    Integer.parseInt(medNotMin.getText().toString()));
                            notifyMessage = "Przypomnienie: zażyj lek o nazwie " + customizedMedicine.getMedicine().getName();
                            notifyHour = Integer.parseInt(medNotHours.getText().toString());
                            notifyMin = Integer.parseInt(medNotMin.getText().toString());
                            saveNewCustomMedicineToDB(customizedMedicine);
                        }
                    } else {
                        CustomizedMedicine customizedMedicine = new CustomizedMedicine(-1, medicinesList.get(selectedMed), Integer.parseInt(frequency.getText().toString()),
                                Integer.parseInt(portion.getText().toString()), unit.getText().toString(), remind, Integer.parseInt(medNotHours.getText().toString()),
                                Integer.parseInt(medNotMin.getText().toString()));
                        if (checkHoursAndMins()) saveNewCustomMedicineToDB(customizedMedicine);
                    }
                }
                else Toast.makeText(getApplicationContext(), "Wprowadź wszystkiego dane", Toast.LENGTH_SHORT).show();
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
                    selectedMed = pos - 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    void turnOnNotify() {
        showNotify("Uruchomiono przypomnienie o zażyciu leku");
        scheduler(notifyHour, notifyMin);
    }

    void showNotify(String message) {
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

    void scheduler(int notifyHour, int notifyMin) {
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
        intent = new Intent(this, MedicineNotifyActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }


    boolean checkHoursAndMins() {
        if (Integer.parseInt(medNotHours.getText().toString()) > 23 || Integer.parseInt(medNotHours.getText().toString()) < 0 || medNotHours.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Niepoprawnie wpisana godzina", Toast.LENGTH_SHORT).show();
            medNotHours.setText("");
            medNotSwitch.setChecked(false);
            return false;
        } else if (Integer.parseInt(medNotMin.getText().toString()) > 59 || Integer.parseInt(medNotMin.getText().toString()) < 0 || medNotMin.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Niepoprawnie wpisana minuta", Toast.LENGTH_SHORT).show();
            medNotMin.setText("");
            medNotSwitch.setChecked(false);
            return false;
        } else {
            return true;

        }
    }

    private long saveNewCustomMedicineToDB(CustomizedMedicine customizedMedicine) {
        long insertData = healthCareDb.addNewCustomMedicineToDB(customizedMedicine);
        if (insertData > 0) {
            turnOnNotify();
            Intent medicineIntent = new Intent(getApplicationContext(), MedicineActivity.class);
            startActivity(medicineIntent);
            Toast.makeText(this, "Nowy lek dodany do listy przyjmowanych leków", Toast.LENGTH_LONG).show();
            return insertData;
        } else {
            Toast.makeText(this, "Wystapił błąd", Toast.LENGTH_LONG).show();
            return insertData;
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
