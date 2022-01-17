package me.polyfrontier.casparwia2.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "FBCA")
public class FBCAEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "passport_number")
    private String passportNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "expiration_date")
    private Date expirationDate;
            
    @Column(name = "reason")
    private FBCAReason crossingReason;

    @Column(name = "transport")
    private FBCATransport transportType;

    @Column(name = "valid")
    private boolean valid;

    @Column(name = "state")
    private FBCAState state;

    @Column(name = "declined_reason")
    private String declinedReason;

    @ElementCollection
    @CollectionTable(name = "fbca_freights", joinColumns = @JoinColumn(name = "fbca_id"))
    private Set<Freight> freights = new HashSet<>();

    public FBCAEntity() {
    }

    public FBCAEntity(long id, String firstName, String lastName, String email,
                      String phone, String passportNumber, Date expirationDate, FBCAReason crossingReason,
                      FBCATransport transportType, boolean valid, FBCAState state, String declinedReason) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.passportNumber = passportNumber;
        this.expirationDate = expirationDate;
        this.crossingReason = crossingReason;
        this.transportType = transportType;
        this.valid = valid;
        this.state = state;
        this.declinedReason = declinedReason;
    }

    public FBCAEntity(String firstName, String lastName, String email,
                      String phone, String passportNumber, Date expirationDate, FBCAReason crossingReason,
                      FBCATransport transportType, boolean valid, FBCAState state, Set<Freight> freights) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.passportNumber = passportNumber;
        this.expirationDate = expirationDate;
        this.crossingReason = crossingReason;
        this.transportType = transportType;
        this.valid = valid;
        this.state = state;
        this.freights = freights;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassportNumber() { return passportNumber; }

    public Date getExpirationDate() { return expirationDate; }

    public FBCAReason getCrossingReason() {
        return crossingReason;
    }

    public FBCATransport getTransportType() { return transportType; }

    public boolean isValid() {
        return valid;
    }
    public FBCAState getState() { return state; }

    public String getDeclinedReason() {
        return declinedReason;
    }

    public Set<Freight> getFreights() {
        return freights;
    }

    /**
     * !!! This function is also a setter
     * If the FBCA is expired, this function sets it to Invalid
     * @return Whether the FBCA is invalid or not
     */
    public boolean canBeUsed() {
        if(expirationDate.before(Date.from(Instant.now()))) {
            this.valid = false;
        }
        return valid;
    }
}
