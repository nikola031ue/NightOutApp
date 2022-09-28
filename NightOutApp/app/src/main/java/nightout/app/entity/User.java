package nightout.app.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public int idUser;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="password")
    public String password;

    @ColumnInfo(name="username")
    public String username;

    public User() {
    }

    public User(String name, String username, String password) {
        this.name = name;
        this.password = password;
        this.username = username;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
