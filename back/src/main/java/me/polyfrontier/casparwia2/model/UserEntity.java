package me.polyfrontier.casparwia2.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "account", schema = "public")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "first_name")
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Column(name = "token")
    private String token = generateToken();

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "role")
    private Role role = Role.GUEST;

    public UserEntity() {
    }

    public UserEntity(Role role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isActive() {
        return role != Role.DISABLED;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void setGuest() {
        role = Role.GUEST;
    }

    public boolean isGuest() {
        return role == Role.GUEST;
    }

    /**
     * Copy the UserEntity given or generate a new token
     * @param userEntity The userEntity to copy
     */
    public void populate(UserEntity userEntity) {
        if (userEntity.token == null) {
            this.token = generateToken();
        } else {
            this.id = userEntity.getId();
            this.token = userEntity.getToken();
            this.username = userEntity.getUsername();
            this.firstName = userEntity.getFirstName();
            this.lastName = userEntity.getLastName();
            this.role = userEntity.getRole();
            this.purpose = userEntity.getPurpose();
        }
    }

    /**
     * Defines the role of the user.
     * Note: Do not change the order of the roles.
     */
    public enum Role {
        ADMIN,
        USER,
        POLICE,
        CUSTOMS,
        DISABLED,
        GUEST
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", token='" + token + '\'' +
                ", purpose='" + purpose + '\'' +
                ", role=" + role +
                '}';
    }
}
