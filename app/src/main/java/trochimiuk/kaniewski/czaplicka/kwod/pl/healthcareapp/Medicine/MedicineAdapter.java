package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private List<CustomizedMedicine> customizedMedicines;
    private static SingleClickListener sClickListener;

    private int lastSelectedPosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView medicineListName, dailyFrequencyList, portionList, unitList;

        public RadioButton selectionState;

        public ViewHolder(View view) {
            super(view);
            medicineListName = (TextView) view.findViewById(R.id.medicineListName);
            dailyFrequencyList = (TextView) view.findViewById(R.id.dailyFrequencyList);
            portionList = (TextView) view.findViewById(R.id.portionList);
            unitList = (TextView) view.findViewById(R.id.unitList);

            selectionState = (RadioButton) view.findViewById(R.id.medicineRbtn);

            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    sClickListener.onItemClickListener(getAdapterPosition(), v);
                }
            });

        }
    }

    void setOnItemClickListener(SingleClickListener clickListener) {
        sClickListener = clickListener;
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

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomizedMedicine customizedMedicine = customizedMedicines.get(position);
        holder.medicineListName.setText(customizedMedicine.getMedicine().getName());
        holder.dailyFrequencyList.setText(Integer.toString(customizedMedicine.getFrequency()));
        holder.portionList.setText(Integer.toString(customizedMedicine.getPortion()));
        holder.unitList.setText(customizedMedicine.getUnit());
        holder.selectionState.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return customizedMedicines.size();
    }

    public int getLastSelectedPosition(){
        return lastSelectedPosition;
    }

    interface SingleClickListener {
        void onItemClickListener(int position, View view);
    }


}
