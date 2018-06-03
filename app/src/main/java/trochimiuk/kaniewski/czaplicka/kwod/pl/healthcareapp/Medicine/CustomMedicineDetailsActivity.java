package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class CustomMedicineDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_medicine_details);
        TextView medTakenName, medTakenDes, medTakenF, medTakenP, medTakenU, medTakenNot, medTakenNotIs;
        medTakenName = (TextView) findViewById(R.id.medTakenName);
        medTakenDes = (TextView) findViewById(R.id.medTakenDes);
        medTakenF = (TextView) findViewById(R.id.medTakenF);
        medTakenP = (TextView) findViewById(R.id.medTakenP);
        medTakenU = (TextView) findViewById(R.id.medTakenU);
        medTakenNot = (TextView) findViewById(R.id.medTakenNot);
        medTakenNotIs = (TextView) findViewById(R.id.medTakenNotIs);
        Bundle extras = getIntent().getExtras();
        medTakenName.setText(extras.getString("medicineName"));
        medTakenDes.setText(extras.getString("medicineDescription"));
        medTakenF.setText(Integer.toString(extras.getInt("medicineFrequency")));
        medTakenP.setText(Integer.toString(extras.getInt("medicinePortion")));
        medTakenU.setText(extras.getString("medicineUnit"));
        //medTakenNot.setText(extras.getString("medicineUnit"));

    }
}
