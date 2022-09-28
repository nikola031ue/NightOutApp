package nightout.app.entity.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import nightout.app.entity.Event;
import nightout.app.entity.Place;

public class PlaceWithEvents {
    @Embedded
    public Place place;

    @Relation(parentColumn = "idPlace",
              entityColumn = "eventPlaceId")
    public List<Event> events;
}
