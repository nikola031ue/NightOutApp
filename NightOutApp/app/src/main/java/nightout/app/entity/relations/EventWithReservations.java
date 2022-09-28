package nightout.app.entity.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import nightout.app.entity.Event;
import nightout.app.entity.Reservation;

public class EventWithReservations {
    @Embedded
    public Event event;

    @Relation(parentColumn = "idEvent",
              entityColumn = "reservationEventId")
    public List<Reservation> reservations;
}
