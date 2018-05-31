package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button medicineBtn = (Button) findViewById(R.id.medicineBtn);
        Button measureBtn = (Button) findViewById(R.id.measureBtn);
        Button appointmentBtn = (Button) findViewById(R.id.appointmentBtn);

        medicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineIntent = new Intent(getApplicationContext(),MedicineActivity.class);
                startActivity(medicineIntent);
            }
        });

        measureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent measureIntent = new Intent(getApplicationContext(),MeasureActivity.class);
                startActivity(measureIntent);
            }
        });

        appointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appointmentIntent = new Intent(getApplicationContext(),AppointmentActivity.class);
                startActivity(appointmentIntent);
            }
        });


    }
}
