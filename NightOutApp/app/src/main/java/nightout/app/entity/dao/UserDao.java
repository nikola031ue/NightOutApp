package nightout.app.entity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import nightout.app.entity.User;
import nightout.app.entity.relations.UserWithReservations;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Transaction
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    User loadUser(String username, String password);

    @Transaction
    @Query(value = "SELECT * FROM user WHERE idUser = :userId")
    List<UserWithReservations> getUserWithReservations(int userId);
}
