package nightout.app.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int idEvent;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "performer")
    public String performer;

    @ColumnInfo(name = "time")
    public String time;

    public int eventPlaceId;

    public Event(String date, String performer, String time) {
        this.date = date;
        this.performer = performer;
        this.time = time;
    }

    public Event() {
    }

    public int getIdEvent() {
        return idEvent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getEventPlaceId() {
        return eventPlaceId;
    }

    public void setEventPlaceId(int eventPlaceId) {
        this.eventPlaceId = eventPlaceId;
    }
}
