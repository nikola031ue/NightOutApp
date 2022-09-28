package nightout.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import nightout.app.AddNewFragment;
import nightout.app.R;
import nightout.app.entity.Event;
import nightout.app.entity.Place;
import nightout.app.entity.Reservation;
import nightout.app.entity.User;

public class ReservationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public ReservationFragment() { }
    TextView tvPlaceRes, tvDTResVal;
    Button btnAccept, btnCancelRes;
    AppDatabase database;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean showButtons;
    int idUserLogged, idPlaceLogged;
    String userDataForLog;
    int[] numSpinner = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    Spinner numGuestsSpinner;
    int numSelectedGuests;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        showButtons = sharedPreferences.getBoolean("isLogged", false);
        idUserLogged = sharedPreferences.getInt("idUserLogged", 0);
        idPlaceLogged = sharedPreferences.getInt("idPlaceLogged", 0);
        userDataForLog = sharedPreferences.getString("userDataForLog", null);

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_reservation, container, false);
        tvPlaceRes = v.findViewById(R.id.tvPlaceRes);
        tvDTResVal = v.findViewById(R.id.tvDTResVal);
        btnAccept = v.findViewById(R.id.btnAccept);
        btnCancelRes = v.findViewById(R.id.btnCancelRes);

        numGuestsSpinner = v.findViewById(R.id.numGuestsSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.spinner_array, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        numGuestsSpinner.setAdapter(adapter);
        numGuestsSpinner.setOnItemSelectedListener(this);

        String[] arrOfStr;
        String username = null, password = null;
        if(userDataForLog != null) {
            arrOfStr = userDataForLog.split(" ");
            username = arrOfStr[0].trim();
            password = arrOfStr[1].trim();
        } else {
            // ako su prazni username i password niko nije prijavljen pa salji da se prijave
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            ListFragment listFragment = new ListFragment();
            Toast.makeText(getActivity(), "Niste prijavljeni",
                    Toast.LENGTH_LONG).show();
            transaction.replace(R.id.frame, listFragment);
            transaction.commit();
        }
        //Kreiran kanal za notifikacije
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyChannel";
            String description = "Description of my channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Reservation_Notification", name, importance);
            channel.setDescription(description);
            NotificationManager notifManager = getActivity().getSystemService(NotificationManager.class);
            notifManager.createNotificationChannel(channel);
        }


        database = AppDatabase.getInstance(getContext());
        int idEventReservation = getArguments() != null ? getArguments().getInt("idEventReservation") : 0;
        Place p = database.placeDao().loadPlaceById(idEventReservation);
        User u = database.userDao().loadUser(username, password);
        List<Event> events = database.eventDao().getAllEvents();
        Event eventReservation = null;
        // TODO moram ovako jer nemam funkciju u eventDao
        for (Event e: events) {
            if(e.getIdEvent() == idEventReservation) {
                eventReservation = e;
            }
        }

        String dateTimeEvent = "" + (eventReservation != null ? eventReservation.getDate() : "Nepoznat datum")
                + " " + (eventReservation != null ? eventReservation.getTime() : "Nepoznato vreme");
        tvDTResVal.setText(dateTimeEvent);
        tvPlaceRes.setText(p.namePlace);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reservation reservation = new Reservation();
                reservation.setReservationEventId(idEventReservation);
                if(u != null) {
                    reservation.setReservationUserId(u.getIdUser());
                    reservation.setNumOfGuests(numSelectedGuests);

                    database.reservationDao().insertReservation(reservation);

                    Log.d("Success reservation", "-----Uspela rezervacija------" + numSelectedGuests);

                    //Notifikacija kreiranje i prikaz
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "Reservation_Notification");
                    builder.setSmallIcon(R.drawable.ic_launcher_background);
                    builder.setContentTitle("Rezervacija");
                    builder.setContentText("Uspesno ste rezervisali mesto u " + p.namePlace + " za " +
                            numSelectedGuests + notificationText(numSelectedGuests));
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                    notificationManager.notify(1, builder.build());

                    getParentFragmentManager().popBackStack();
                } else {
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    ListFragment listFragment = new ListFragment();
                    Toast.makeText(getActivity(), "Niste prijavljeni",
                            Toast.LENGTH_LONG).show();
                    transaction.replace(R.id.frame, listFragment);
                    transaction.commit();
                }

            }
        });

        btnCancelRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        return v;
    }

    private String notificationText(int num) {
        if(num <= 4 && num != 0) {
            return " gosta";
        } else {
            return " gostiju";
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        AddNewFragment addNewFragment = new AddNewFragment();
        AboutFragment aboutFragment = new AboutFragment();

        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(getActivity(), "Podesavanja", Toast.LENGTH_LONG).show();
                return true;
            case R.id.about:
                transaction.replace(R.id.frame, aboutFragment);
                transaction.addToBackStack("about");
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        numSelectedGuests = Integer.parseInt(adapterView.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        numSelectedGuests = 0;
    }
}
