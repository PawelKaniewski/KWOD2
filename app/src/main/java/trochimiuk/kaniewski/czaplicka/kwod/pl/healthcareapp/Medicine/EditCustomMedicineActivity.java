package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.DatabaseHelper;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class EditCustomMedicineActivity extends AppCompatActivity {

    private EditText editFrequency;
    private EditText medicinePortionEdit, medicineUnitEdit;
    private Button saveEditBtn,cancelBtn;
    private DatabaseHelper healthCareDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custom_medicine);

        saveEditBtn = (Button) findViewById(R.id.saveEditBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineIntent = new Intent(getApplicationContext(), MedicineActivity.class);
                startActivity(medicineIntent);
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
        Toast.makeText(EditCustomMedicineActivity.this, "Częstotliwość: " + editFrequency.getText() + " i dawka: " + medicinePortionEdit.getText(), Toast.LENGTH_LONG).show();
        editFrequency.setText(extras.getString("medicineFrequencyEdit"));
        medicinePortionEdit.setText(extras.getString("medicinePortionEdit"));
        medicineUnitEdit.setText(extras.getString("medicineUnitEdit"));

        saveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicine medicine = new Medicine(medicineNameEdit.getText().toString(),description);
                CustomizedMedicine customizedMedicine = new CustomizedMedicine(medicineId,medicine,Integer.parseInt(editFrequency.getText().toString()),
                        Integer.parseInt(medicinePortionEdit.getText().toString()),medicineUnitEdit.getText().toString(),true,12,50);
                boolean result = healthCareDb.updateCustomMedicine(customizedMedicine);
                if(result==false){
                    Toast.makeText(EditCustomMedicineActivity.this, "Błąd edycji!", Toast.LENGTH_LONG).show();
                }else{
                    Intent medicineIntent = new Intent(getApplicationContext(), MedicineActivity.class);
                    startActivity(medicineIntent);
                }
            }
        });
    }
}
