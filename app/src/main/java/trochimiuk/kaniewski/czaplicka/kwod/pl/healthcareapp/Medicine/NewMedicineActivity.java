package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.DatabaseHelper;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class NewMedicineActivity extends AppCompatActivity {

    private DatabaseHelper healthCareDb;
    private Spinner medicineSpinner;
    private List<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);
        Button addMedicineBtn = (Button) findViewById(R.id.addNewMedBtn);

        addMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineIntent = new Intent(getApplicationContext(),NewMedicineInDBActivity.class);
                startActivity(medicineIntent);
            }
        });

        medicineSpinner = (Spinner) findViewById(R.id.medicineSpinner);
        healthCareDb = new DatabaseHelper(this);
        ArrayList<Medicine> medicinesList = new ArrayList<>();
        Cursor data = healthCareDb.getListContents();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                names.add(data.getString(1));
                medicinesList.add(new Medicine(data.getString(1),data.getString(2)));
                ArrayAdapter<String> medicineAdapter = new ArrayAdapter<String>(NewMedicineActivity.this,android.R.layout.simple_spinner_item,names);
                medicineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                medicineSpinner.setAdapter(medicineAdapter);
            }

        }
        /*Intent intent = getIntent();
        Toast.makeText(this,intent.getStringExtra("dbSuccess"),  Toast.LENGTH_LONG).show();
        /*if(!(intent.getStringExtra("dbSuccess").isEmpty())){
            Toast.makeText(getApplicationContext(),intent.getStringExtra("dbSuccess"),  Toast.LENGTH_LONG).show();
        }*/

    }
}
