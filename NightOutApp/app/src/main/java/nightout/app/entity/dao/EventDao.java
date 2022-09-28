package nightout.app.entity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;


import java.util.List;

import nightout.app.entity.Event;
import nightout.app.entity.relations.EventWithReservations;

@Dao
public interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvent(Event event);

    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

//    @Transaction
//    @Query(value="SELECT * FROM event WHERE idEvent = :id")
//    Event loadEventById(int id);

    @Transaction
    @Query(value = "SELECT * FROM event WHERE idEvent = :eventId")
    List<EventWithReservations> getEventWithReservations(int eventId);

}
