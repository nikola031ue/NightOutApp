package nightout.app.entity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import nightout.app.entity.Place;
import nightout.app.entity.relations.PlaceWithEvents;

@Dao
public interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlace(Place place);

    @Transaction
    @Query("SELECT * FROM place WHERE usernamePlace = :username AND passwordPlace = :password")
    Place loadPlace(String username, String password);

    @Transaction
    @Query("SELECT * FROM place WHERE idPlace = :id")
    Place loadPlaceById(int id);

    @Transaction
    @Query("SELECT * FROM place WHERE namePlcae = :name")
    Place loadPlaceByName(String name);

    @Transaction
    @Query("SELECT * FROM place WHERE idplace = :placeId")
    List<PlaceWithEvents> getPlaceWithEvents(int placeId);
}
