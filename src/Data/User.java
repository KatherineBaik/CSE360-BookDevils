package Data;

public class User {
    private final int userID;
    private String name;
    private String password;
    private Role role;
    private boolean isSuspended;

    //Constructor
    public User(int ID, String username, String password, Role role){
        userID = ID;
        this.username = username;
        this.password = password;
        this.role = role;
        isSuspended = false;
    }

    //Getters and Setters
    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    //Other Functions

    @Override
    public String toString(){
        return String.format("id=%d, username=%s, role=%s", userID, username, role.toString());
    }

    //Enums
    public enum Role{
        BUYER, SELLER, ADMIN
    }
}
