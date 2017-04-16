package ba.steleks.model;/**
 * Created by ensar on 22/03/17.
 */

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

@Entity
public class User {
    private static final Logger logger =
            Logger.getLogger(User.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private int cardNumber;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Timestamp registrationDate;
    @NotNull
    private String email;
    @NotNull
    private String contactNumber;
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;
    @NotNull
    private String username;

    private String profilePictureUrl;

    @ManyToMany
    @JoinColumn
    private Set<Course> courses;

    @ManyToMany
    @JoinColumn
    private Set<MembershipType> membershipTypes;

    @ManyToMany
    @JoinColumn
    private Set<UserRole> userRoles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
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

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<MembershipType> getMembershipTypes() {
        return membershipTypes;
    }

    public void setMembershipTypes(Set<MembershipType> membershipTypes) {
        this.membershipTypes = membershipTypes;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @PrePersist
    protected void onCreate() {
        this.registrationDate = new Timestamp(new Date().getTime());
    }

}
