package nightout.app.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Place {
    @PrimaryKey(autoGenerate = true)
    public int idPlace;

    @ColumnInfo(name="namePlcae")
    public String namePlace;

    @ColumnInfo(name="passwordPlace")
    public String passwordPlace;

    @ColumnInfo(name="usernamePlace")
    public String usernamePlace;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "music")
    public String music;

    @ColumnInfo(name = "typePlace")
    public String typePlace;

    @ColumnInfo(name = "picture")
    public String pictureName;

    @ColumnInfo(name = "address")
    public String address;


    public Place() {
    }

    public Place(String namePlace, String music, String typePlace, String description, String usernamePlace, String passwordPlace) {
        this.namePlace = namePlace;
        this.passwordPlace = passwordPlace;
        this.usernamePlace = usernamePlace;
        this.description = description;
        this.music = music;
        this.typePlace = typePlace;
    }

    public int getIdPlace() {
        return idPlace;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    public String getPasswordPlace() {
        return passwordPlace;
    }

    public void setPasswordPlace(String passwordPlace) {
        this.passwordPlace = passwordPlace;
    }

    public String getUsernamePlace() {
        return usernamePlace;
    }

    public void setUsernamePlace(String usernamePlace) {
        this.usernamePlace = usernamePlace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getTypePlace() {
        return typePlace;
    }

    public void setTypePlace(String typePlace) {
        this.typePlace = typePlace;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
