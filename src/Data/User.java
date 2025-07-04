package Data;

import javafx.beans.property.*;

public class User {
    /** we keep leading zeros, so store the ASU ID as a String */
    private final StringProperty asuId;
    private final StringProperty password;
    private final ObjectProperty<Role> role;
    private final BooleanProperty suspended;

    public User(String asuId, String password, Role role) {
        this.asuId = new SimpleStringProperty(asuId);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleObjectProperty<>(role);
        this.suspended = new SimpleBooleanProperty(false);
    }

    // ——— getters & setters ———
    public String getAsuId()       { return asuId.get(); }
    public String getPassword()    { return password.get(); }
    public void   setPassword(String pw) { this.password.set(pw); }

    public Role   getRole()        { return role.get(); }
    public void   setRole(Role r)  { this.role.set(r); }

    public boolean isSuspended()   { return suspended.get(); }
    public void    setSuspended(boolean s) { this.suspended.set(s); }

    @Override public String toString() {
        return String.format("asuId=%s, role=%s", asuId, role);
    }

    // Enum of roles the UI combo-box shows
    public enum Role { BUYER, SELLER, ADMIN }
    
    public String toCsv() {
        return String.join(",",
                asuId.get(),
                password.get(),
                role.get().name(),
                Boolean.toString(suspended.get()));
    }

    public static User fromCsv(String line) throws IllegalArgumentException {
        String[] parts = line.split(",", -1);
        if (parts.length < 4) throw new IllegalArgumentException("Bad line: " + line);
        User u = new User(parts[0], parts[1], Role.valueOf(parts[2]));
        u.suspended.set(Boolean.parseBoolean(parts[3]));
        return u;
    }

    //for testing
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof User other)) return false;
        return (asuId.equals(other.asuId) &&
                password.get().equals(other.getPassword()) &&
                role.equals(other.role) &&
                suspended == other.suspended);
    }
}
