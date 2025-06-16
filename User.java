public class User {
    private int userID;
    private String username;
    private String password;
    private Role role;


    enum Role{
        BUYER, SELLER, ADMIN
    }
}
