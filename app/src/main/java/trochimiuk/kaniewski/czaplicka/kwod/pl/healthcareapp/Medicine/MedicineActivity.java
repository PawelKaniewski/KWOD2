package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.DatabaseHelper;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class MedicineActivity extends AppCompatActivity implements MedicineAdapter.SingleClickListener {

    private RecyclerView recyclerView;
    private List<CustomizedMedicine> currentMedicinesList = new ArrayList<>();
    private MedicineAdapter medicineAdapter;
    private DatabaseHelper healthCareDb;
    private Button editBtn, delBtn, detailsBtn;
    private int selectedPosition;

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
        medicineAdapter.setOnItemClickListener(this);
        healthCareDb = new DatabaseHelper(this);
        Cursor data = healthCareDb.getCustomMedicinesListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "Brak custom leków w bazie! Zdefiniuj lek!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                Medicine medicine = new Medicine(data.getString(1), data.getString(2));
                CustomizedMedicine customizedMedicine = new CustomizedMedicine(Integer.parseInt(data.getString(0)), medicine,
                        Integer.parseInt(data.getString(3)), Integer.parseInt(data.getString(4)), data.getString(5),Boolean.parseBoolean(data.getString(6)),
                        Integer.parseInt(data.getString(7)),Integer.parseInt(data.getString(8)));
                currentMedicinesList.add(customizedMedicine);
                Boolean czyTak = Boolean.valueOf(data.getString(6));
                medicineAdapter.notifyDataSetChanged();
            }
        }

        editBtn = (Button) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineIntent = new Intent(getApplicationContext(), EditCustomMedicineActivity.class);
                CustomizedMedicine customMedicine = currentMedicinesList.get(selectedPosition);
                medicineIntent.putExtra("medicineEditId",Integer.toString(customMedicine.getId()));
                medicineIntent.putExtra("medicineNameEdit",customMedicine.getMedicine().getName());
                medicineIntent.putExtra("medicineDescriptionEdit",customMedicine.getMedicine().getDescription());
                medicineIntent.putExtra("medicineFrequencyEdit",Integer.toString(customMedicine.getFrequency()));
                medicineIntent.putExtra("medicinePortionEdit",Integer.toString(customMedicine.getPortion()));
                medicineIntent.putExtra("medicineUnitEdit",customMedicine.getUnit());
                medicineIntent.putExtra("medicineHours",Integer.toString(customMedicine.getHours()));
                medicineIntent.putExtra("medicineMins",Integer.toString(customMedicine.getMins()));
                medicineIntent.putExtra("medicineRemindOn",Boolean.toString(customMedicine.isNotOn()));
                startActivity(medicineIntent);
            }
        });


        detailsBtn = (Button) findViewById(R.id.detailsBtn);
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineIntent = new Intent(getApplicationContext(), CustomMedicineDetailsActivity.class);
                CustomizedMedicine customMedicine = currentMedicinesList.get(selectedPosition);
                medicineIntent.putExtra("medicineName",customMedicine.getMedicine().getName());
                medicineIntent.putExtra("medicineDescription",customMedicine.getMedicine().getDescription());
                medicineIntent.putExtra("medicineFrequency", customMedicine.getFrequency());
                medicineIntent.putExtra("medicinePortion",customMedicine.getPortion());
                medicineIntent.putExtra("medicineUnit",customMedicine.getUnit());
                if(customMedicine.isNotOn()) medicineIntent.putExtra("medicineRemindOn","TAK");
                else medicineIntent.putExtra("medicineRemindOn","NIE");
                medicineIntent.putExtra("medicineHours",Integer.toString(customMedicine.getHours()));
                medicineIntent.putExtra("medicineMins",Integer.toString(customMedicine.getMins()));

                startActivity(medicineIntent);
            }
        });



        delBtn = (Button) findViewById(R.id.delBtn);

        delBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (healthCareDb.deleteCustomMedicine(currentMedicinesList.get(selectedPosition).getId())) {
                    Toast.makeText(MedicineActivity.this, "Usunięto lek o nazwie: " + currentMedicinesList.get(selectedPosition).getMedicine().getName(), Toast.LENGTH_LONG).show();
                    currentMedicinesList.remove(selectedPosition);
                    medicineAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MedicineActivity.this, "Blad usunięcia!", Toast.LENGTH_LONG).show();
                }
            }


        });


        Button addMedicineBtn = (Button) findViewById(R.id.addMedicineBtn);
        addMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineIntent = new Intent(getApplicationContext(), NewMedicineActivity.class);
                startActivity(medicineIntent);
            }
        });
    }

        @Override
        public void onItemClickListener(int position, View view) {
            medicineAdapter.selectedItem();
            this.selectedPosition = position;
            editBtn.setEnabled(true);
            detailsBtn.setEnabled(true);
            delBtn.setEnabled(true);
    }
}
