package nightout.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.List;

import nightout.app.AboutFragment;
import nightout.app.AddNewFragment;
import nightout.app.AppDatabase;
import nightout.app.ListFragment;
import nightout.app.R;
import nightout.app.entity.Event;
import nightout.app.entity.Place;
import nightout.app.entity.Reservation;
import nightout.app.entity.User;

public class MainActivity extends AppCompatActivity  {

    private AboutFragment aboutFragment;
    private ListFragment listFragment;
    public static AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogged", false);
        editor.apply();

        database = AppDatabase.getInstance(this.getApplicationContext());
//
        User user = new User("Milan", "milan", "milan");
        database.userDao().insertUser(user);

        Place place = new Place("Caffe Zizi", "rock", "kafic", "Prijatno mesto za opustanje uz zvuke rock muzike", "zizi", "zizi");
        place.setAddress("Nemanjina 104 Uzice");
        database.placeDao().insertPlace(place);

        Reservation reservation = new Reservation();
        reservation.setNumOfGuests(10);
        reservation.setReservationEventId(1);
        reservation.setReservationUserId(1);
        database.reservationDao().insertReservation(reservation);

        Event event = new Event("1.1.2022.", "YU Grupa", "21:00");
        event.setEventPlaceId(1);
        database.eventDao().insertEvent(event);

        aboutFragment = new AboutFragment();
        listFragment = new ListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(isTablet()) {
            //tablet
            transaction.add(R.id.frameLeft, listFragment);
            transaction.add(R.id.frameRight, aboutFragment);

        } else {
            //phone
            transaction.add(R.id.frame, listFragment);
        }
        transaction.commit();
    }



    private boolean isTablet() {
        return findViewById(R.id.frameLeft) != null && findViewById(R.id.frameRight) != null;
    }

}