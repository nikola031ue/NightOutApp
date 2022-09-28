package nightout.app.entity.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import nightout.app.entity.Reservation;
import nightout.app.entity.User;

public class UserWithReservations {
    @Embedded
    public User user;

    @Relation(parentColumn = "idUser",
              entityColumn = "reservationUserId")
    public List<Reservation> reservations;
}
