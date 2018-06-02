package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Appointment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.R;

class AppointmentsList extends RecyclerView.Adapter<AppointmentsList.ViewHolderEvents> {

    private List<Event> events;

    public class ViewHolderEvents extends RecyclerView.ViewHolder {
        public TextView timeList, descriptionList;
        private SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());

        public ViewHolderEvents(View view) {
            super(view);
            timeList = (TextView) view.findViewById(R.id.appointmentTime);
            descriptionList = (TextView) view.findViewById(R.id.appointmentDescription);
        }
    }

    public AppointmentsList(List<Event> events) {
        this.events = events;
    }

    @Override
    public ViewHolderEvents onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointments_list_detail, parent, false);

        return new ViewHolderEvents(itemView);
    }

    @Override
    public void onBindViewHolder(AppointmentsList.ViewHolderEvents holder, int position) {
        Event event = events.get(position);
        holder.timeList.setText(holder.dateFormatTime.format(event.getTimeInMillis()));
        holder.descriptionList.setText(event.getData().toString());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

}
