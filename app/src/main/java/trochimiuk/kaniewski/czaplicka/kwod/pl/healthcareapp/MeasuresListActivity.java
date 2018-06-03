package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Measure.MeasureActivity;

public class MeasuresListActivity extends AppCompatActivity {
    private Button powrot;
    private EditText lista;
    Calendar cal;
    Calendar cal1;
    Calendar cal2;
    Calendar cal3;
    Calendar cal4;
    Calendar cal5;
    Calendar cal6;
    Calendar cal7;
    Calendar cal8;
    Calendar cal9;
    Calendar cal10;
    Calendar cal11;
    Calendar cal12;
    Calendar cal13;
    Calendar cal14;
    Calendar cal15;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measures_list);

        powrot = (Button) findViewById(R.id.przyciskPowrot);
        lista = (EditText) findViewById(R.id.listaPomiarow);

        cal = Calendar.getInstance();
        cal1 = Calendar.getInstance();
        cal2 = Calendar.getInstance();
        cal3 = Calendar.getInstance();
        cal4 = Calendar.getInstance();
        cal5 = Calendar.getInstance();
        cal6 = Calendar.getInstance();
        cal7 = Calendar.getInstance();
        cal8 = Calendar.getInstance();
        cal9 = Calendar.getInstance();
        cal10 = Calendar.getInstance();
        cal11 = Calendar.getInstance();
        cal12 = Calendar.getInstance();
        cal13 = Calendar.getInstance();
        cal14 = Calendar.getInstance();
        cal15 = Calendar.getInstance();

        cal1.add(Calendar.DATE,-1);
        cal2.add(Calendar.DATE,-2);
        cal3.add(Calendar.DATE,-3);
        cal4.add(Calendar.DATE,-4);
        cal5.add(Calendar.DATE,-5);
        cal6.add(Calendar.DATE,-6);
        cal7.add(Calendar.DATE,-7);
        cal8.add(Calendar.DATE,-8);
        cal9.add(Calendar.DATE,-9);
        cal10.add(Calendar.DATE,-10);
        cal11.add(Calendar.DATE,-11);
        cal12.add(Calendar.DATE,-12);
        cal13.add(Calendar.DATE,-13);
        cal14.add(Calendar.DATE,-14);
        cal15.add(Calendar.DATE,-15);


        String text =
               (Integer.toString(cal15.get(Calendar.DATE)) + "." + Integer.toString(cal15.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[14]) + "\n"
               +Integer.toString(cal14.get(Calendar.DATE)) + "." + Integer.toString(cal14.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[15]) + "\n"
               +Integer.toString(cal13.get(Calendar.DATE)) + "." + Integer.toString(cal13.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[16]) + "\n"
               +Integer.toString(cal12.get(Calendar.DATE)) + "." + Integer.toString(cal12.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[17]) + "\n"
               +Integer.toString(cal11.get(Calendar.DATE)) + "." + Integer.toString(cal11.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[18]) + "\n"
               +Integer.toString(cal10.get(Calendar.DATE)) + "." + Integer.toString(cal10.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[19]) + "\n"
               +Integer.toString(cal9.get(Calendar.DATE)) + "." + Integer.toString(cal9.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[20]) + "\n"
               +Integer.toString(cal8.get(Calendar.DATE)) + "." + Integer.toString(cal8.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[21]) + "\n"
               +Integer.toString(cal7.get(Calendar.DATE)) + "." + Integer.toString(cal7.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[22]) + "\n"
               +Integer.toString(cal6.get(Calendar.DATE)) + "." + Integer.toString(cal6.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[23]) + "\n"
               +Integer.toString(cal5.get(Calendar.DATE)) + "." + Integer.toString(cal5.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[24]) + "\n"
               +Integer.toString(cal4.get(Calendar.DATE)) + "." + Integer.toString(cal4.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[25]) + "\n"
               +Integer.toString(cal3.get(Calendar.DATE)) + "." + Integer.toString(cal3.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[26]) + "\n"
               +Integer.toString(cal2.get(Calendar.DATE)) + "." + Integer.toString(cal2.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[27]) + "\n"
               +Integer.toString(cal1.get(Calendar.DATE)) + "." + Integer.toString(cal1.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[28]) + "\n"
               +Integer.toString(cal.get(Calendar.DATE)) + "." + Integer.toString(cal.get(Calendar.MONTH) + 1)+ ": " + Integer.toString(MeasureActivity.getPomiary()[29]) + "\n");

lista.setText(text);

        powrot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent measureIntent = new Intent(getApplicationContext(),MeasureActivity.class);
                startActivity(measureIntent);
            }
        });
    }
}
