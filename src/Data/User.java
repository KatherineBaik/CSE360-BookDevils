package Data;

public class User {
    /** we keep leading zeros, so store the ASU ID as a String */
    private final String asuId;
    private String password;
    private Role role;
    private boolean suspended;

    public User(String asuId, String password, Role role) {
        this.asuId = asuId;
        this.password = password;
        this.role = role;
        this.suspended = false;
    }

    // ——— getters & setters ———
    public String getAsuId()       { return asuId; }
    public String getPassword()    { return password; }
    public void   setPassword(String pw) { this.password = pw; }

    public Role   getRole()        { return role; }
    public void   setRole(Role r)  { this.role = r; }

    public boolean isSuspended()   { return suspended; }
    public void    setSuspended(boolean s) { this.suspended = s; }

    @Override public String toString() {
        return String.format("asuId=%s, role=%s", asuId, role);
    }

    // Enum of roles the UI combo-box shows
    public enum Role { BUYER, SELLER, ADMIN }
    
    public String toCsv() {
        return String.join(",",
                asuId,
                password,
                role.name(),
                Boolean.toString(suspended));
    }

    public static User fromCsv(String line) throws IllegalArgumentException {
        String[] parts = line.split(",", -1);
        if (parts.length < 4) throw new IllegalArgumentException("Bad line: " + line);
        User u = new User(parts[0], parts[1], Role.valueOf(parts[2]));
        u.suspended = Boolean.parseBoolean(parts[3]);
        return u;
    }

    //for testing
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof User other)) return false;
        return (asuId.equals(other.asuId) &&
                password.equals(other.password) &&
                role.equals(other.role) &&
                suspended == other.suspended);
    }
}
