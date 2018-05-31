package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.DatabaseHelper;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class NewMedicineInDBActivity extends AppCompatActivity {

    private DatabaseHelper healthCareDb;
    private Button saveNewMed;
    private EditText newMedName, newDescName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine_in_db);
        newMedName = (EditText) findViewById(R.id.newMedName);
        newDescName = (EditText) findViewById(R.id.newDescName);
        saveNewMed = (Button) findViewById(R.id.saveNewMed);

        healthCareDb = new DatabaseHelper(this);

        saveNewMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newMedName.length() != 0 && newDescName.length() != 0) {
                    AddData(new Medicine(newMedName.getText().toString(), newDescName.getText().toString()));
                    newMedName.setText("");
                    newDescName.setText("");
                } else {
                    Toast.makeText(NewMedicineInDBActivity.this, "Wprowadź wszystkie dane!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void AddData(Medicine medicine) {
        boolean insertData = healthCareDb.addData(medicine);
        if (insertData == true) {
            Intent medicineIntent = new Intent(getApplicationContext(), NewMedicineActivity.class);
            medicineIntent.putExtra("dbSuccess", "Lek dodany do bazy danych!");
            startActivity(medicineIntent);
        } else {
            Toast.makeText(this, "Wystapił błąd", Toast.LENGTH_LONG).show();
        }
    }


}
