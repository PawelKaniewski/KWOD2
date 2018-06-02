package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);

        this.initAllFields();

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
