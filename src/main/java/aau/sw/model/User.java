package aau.sw.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    public enum UserStatus {
        AVAILABLE,
        ON_LEAVE,
        BUSY,
    }

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String encryptedPassword;
    private String name;
    private String role;
    private String title;
    private String phoneNumber;

    private UserStatus userStatus;

    public User() {}

    public User(String id, String email, String encryptedPassword, String name, String role, String title, String phoneNumber, UserStatus userStatus) {
        this.id = id;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.name = name;
        this.role = role;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.userStatus = userStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
