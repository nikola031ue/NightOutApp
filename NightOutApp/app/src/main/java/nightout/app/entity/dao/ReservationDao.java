package nightout.app.entity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import nightout.app.entity.Reservation;

@Dao
public interface ReservationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReservation(Reservation reservation);

}
