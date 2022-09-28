package nightout.app;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nightout.app.entity.Event;
import nightout.app.entity.Place;
import nightout.app.entity.Reservation;
import nightout.app.entity.User;
import nightout.app.entity.dao.EventDao;
import nightout.app.entity.dao.PlaceDao;
import nightout.app.entity.dao.ReservationDao;
import nightout.app.entity.dao.UserDao;

@Database(entities = {User.class, Event.class, Place.class, Reservation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract EventDao eventDao();
    public abstract ReservationDao reservationDao();
    public abstract PlaceDao placeDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {

        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Event_database")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }

}
