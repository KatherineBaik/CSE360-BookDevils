public class User {
    private int userID;
    private String username;
    private String password;
    private Role role;

    //Constructors
    public User(){
        //TODO
    }

    //Getters and Setters

    //Other Functions

    //Enums
    public enum Role{
        BUYER, SELLER, ADMIN
    }
}
