package nightout.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import nightout.app.entity.Event;
import nightout.app.entity.Place;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    List<Event> eventList;
    AppDatabase database;

    public EventAdapter(List<Event> eventList, AppDatabase database) {
        this.eventList = eventList;
        this.database = database;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        EventViewHolder evh = new EventViewHolder(v, eventList);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Place p = database.placeDao().loadPlaceById(eventList.get(position).getIdEvent());
        if(p != null) {
            holder.tvPlaceItem.setText("");
            holder.tvPerformerItem.setText(eventList.get(position).getPerformer());
            holder.tvDateItem.setText(eventList.get(position).getDate());
            holder.tvTimeItem.setText(eventList.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        public TextView tvPlaceItem, tvPerformerItem, tvDateItem,tvTimeItem;
        public ImageButton btnLocation;
        public Button btnReservation;

        public EventViewHolder(View v, List<Event> eventList) {
            super(v);
            tvPlaceItem = v.findViewById(R.id.tvPlaceItem);
            tvPerformerItem = v.findViewById(R.id.tvPerformerItem);
            tvDateItem = v.findViewById(R.id.tvDateItem);
            tvTimeItem = v.findViewById(R.id.tvTimeItem);
            btnLocation = v.findViewById(R.id.btnLocation);
            btnReservation = v.findViewById(R.id.btnReservation);

            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

            btnReservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int idevent = eventList.get(getBindingAdapterPosition()).getIdEvent();
                    Place p = database.placeDao().loadPlaceById(idevent);
                    Log.d("****button test****", "Id event" + idevent + " binding position " + getBindingAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putInt("idEventReservation", idevent);

                    ReservationFragment reservationFragment = new ReservationFragment();
                    reservationFragment.setArguments(bundle);

                    if(isTablet(v)) {
                        //tablet
                        transaction.replace(R.id.frameRight, reservationFragment);
                        transaction.addToBackStack(null);
                    } else {
                        transaction.replace(R.id.frame, reservationFragment);
                        transaction.addToBackStack(null);
                    }
                    transaction.commit();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PlaceViewFragment placeFragment = new PlaceViewFragment();


                    int idevent = eventList.get(getBindingAdapterPosition()).getIdEvent();
                    Place p = database.placeDao().loadPlaceById(idevent);

                    Bundle bundle = new Bundle();
                    bundle.putInt("itemPlace", p.idPlace);
                    placeFragment.setArguments(bundle);
                    if(isTablet(v)) {
                        //tablet
                        transaction.replace(R.id.frameRight, placeFragment);
                        transaction.addToBackStack(null);
                    } else {
                        transaction.replace(R.id.frame, placeFragment);
                        transaction.addToBackStack(null);
                    }
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            btnLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int idevent = eventList.get(getBindingAdapterPosition()).getIdEvent();
                    Place p = database.placeDao().loadPlaceById(idevent);
                    Bundle bundle = new Bundle();
                    bundle.putString("address", p.getAddress());
                    bundle.putString("placeNameMap", p.getNamePlace());
                    MapsFragment map = new MapsFragment();
                    map.setArguments(bundle);

                    if(isTablet(v)) {
                        //tablet
                        transaction.replace(R.id.frameRight, map);
                        transaction.addToBackStack(null);
                    } else {
                        transaction.replace(R.id.frame, map);
                        transaction.addToBackStack(null);
                    }
                    transaction.commit();
                }
            });
        }

        private boolean isTablet(View view) {
            return view.findViewById(R.id.frameLeft) != null && view.findViewById(R.id.frameRight) != null;
        }
    }

    //If we need to clear all events.
    public void clear() {
        eventList.clear();
        notifyDataSetChanged();
    }
}
