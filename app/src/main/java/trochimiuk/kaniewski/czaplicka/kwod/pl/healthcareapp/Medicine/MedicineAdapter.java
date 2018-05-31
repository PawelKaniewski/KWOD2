package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private List<CustomizedMedicine> customizedMedicines;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView medicineListName, dailyFrequencyList, portionList, unitList;

        public ViewHolder(View view) {
            super(view);
            medicineListName = (TextView) view.findViewById(R.id.medicineListName);
            dailyFrequencyList = (TextView) view.findViewById(R.id.dailyFrequencyList);
            portionList = (TextView) view.findViewById(R.id.portionList);
            unitList = (TextView) view.findViewById(R.id.unitList);
        }
    }

    public MedicineAdapter(List<CustomizedMedicine> customizedMedicines) {
        this.customizedMedicines = customizedMedicines;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.current_medicines_list_detail, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomizedMedicine customizedMedicine = customizedMedicines.get(position);
        holder.medicineListName.setText(customizedMedicine.getMedicine().getName());
        holder.dailyFrequencyList.setText(Integer.toString(customizedMedicine.getFrequency()));
        holder.portionList.setText(Integer.toString(customizedMedicine.getPortion()));
        holder.unitList.setText(customizedMedicine.getUnit());
    }

    @Override
    public int getItemCount() {
        return customizedMedicines.size();
    }





}
