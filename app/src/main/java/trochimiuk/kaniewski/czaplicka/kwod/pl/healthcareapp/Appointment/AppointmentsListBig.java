package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Appointment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.DatabaseHelper;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine.MedicineAdapter;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

class AppointmentsListBig extends RecyclerView.Adapter<AppointmentsListBig.ViewHolderEvents> {

    private List<Event> events;
    private int[] remind;
    private int lastSelectedPosition = -1;
    private static SingleClickListener aClickListener;

    public class ViewHolderEvents extends RecyclerView.ViewHolder {
        public TextView timeList, descriptionList;
        private SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        public RadioButton selectionState;

        public ViewHolderEvents(View view) {
            super(view);
            timeList = (TextView) view.findViewById(R.id.appointmentTime);
            descriptionList = (TextView) view.findViewById(R.id.appointmentDescription);

            selectionState = (RadioButton) view.findViewById(R.id.appRadioBtn);

            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    aClickListener.onItemClickListener(getAdapterPosition(), v);
                }
            });
        }
    }

    void setOnItemClickListener(SingleClickListener clickListener) {
        aClickListener = clickListener;
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }


    public AppointmentsListBig(List<Event> events, int[] remindTime) {
        this.events = events;
        this.remind = remindTime;
    }

    @Override
    public ViewHolderEvents onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointments_list_big_detail, parent, false);

        return new ViewHolderEvents(itemView);
    }

    @Override
    public void onBindViewHolder(AppointmentsListBig.ViewHolderEvents holder, int position) {
        Event event = events.get(position);
        holder.timeList.setText(holder.dateFormatTime.format(event.getTimeInMillis()));
        String info = event.getData().toString()+"\nPowiadomienie: "+showRemindTime(remind[position]);
        holder.descriptionList.setText(info);
        holder.selectionState.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public String showRemindTime(int index){
        String timeBefore="brak";
        switch(index) {
            case 0:
                timeBefore="2 tygodnie przed";
                break;
            case 1:
                timeBefore="tydzień przed";
                break;
            case 2:
                timeBefore="5 dni przed";
                break;
            case 3:
                timeBefore="3 dni przed";
                break;
            case 4:
                timeBefore="2 dni przed";
                break;
            case 5:
                timeBefore="dzień przed";
                break;
            case 6:
                timeBefore="5 godzin przed";
                break;
            case 7:
                timeBefore="2 godziny przed";
                break;
            case 8:
                timeBefore="godzinę przed";
                break;

        }
        return timeBefore;

    }


    interface SingleClickListener {
        void onItemClickListener(int position, View view);
    }


}
