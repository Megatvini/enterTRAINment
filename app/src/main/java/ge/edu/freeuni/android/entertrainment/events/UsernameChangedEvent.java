package ge.edu.freeuni.android.entertrainment.events;

/**
 * Created by Nika Doghonadze.
 */
public class UsernameChangedEvent {
    private String newUsername;

    public UsernameChangedEvent(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
}
