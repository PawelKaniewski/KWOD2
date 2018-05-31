package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine.CustomizedMedicine;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine.Medicine;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class MedicineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CustomizedMedicine> currentMedicinesList = new ArrayList<>();
    private MedicineAdapter medicineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_medicine);
        recyclerView = (RecyclerView) findViewById(R.id.currentMedicinesList);

        medicineAdapter = new MedicineAdapter(currentMedicinesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(medicineAdapter);

        Medicine medicine1 = new Medicine("testLek1","");
        Medicine medicine2 = new Medicine("testLek2","");
        Medicine medicine3 = new Medicine("testLek3","");
        CustomizedMedicine customizedMedicine1 = new CustomizedMedicine(medicine1,2,1,"tabletki");
        CustomizedMedicine customizedMedicine2 = new CustomizedMedicine(medicine2,3,2,"tabletki");
        CustomizedMedicine customizedMedicine3 = new CustomizedMedicine(medicine3,1,3,"tabletki");
        CustomizedMedicine customizedMedicine4 = new CustomizedMedicine(medicine2,3,2,"tabletki");
        CustomizedMedicine customizedMedicine5 = new CustomizedMedicine(medicine3,1,3,"tabletki");

        currentMedicinesList.add(customizedMedicine1);
        currentMedicinesList.add(customizedMedicine2);
        currentMedicinesList.add(customizedMedicine3);
        currentMedicinesList.add(customizedMedicine4);
        currentMedicinesList.add(customizedMedicine5);

        medicineAdapter.notifyDataSetChanged();

        Button addMedicineBtn = (Button) findViewById(R.id.addMedicineBtn);
        addMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineIntent = new Intent(getApplicationContext(),NewMedicineActivity.class);
                startActivity(medicineIntent);
            }
        });


    }
}
