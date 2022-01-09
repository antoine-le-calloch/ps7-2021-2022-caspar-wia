package me.polyfrontier.casparwia2.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

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
    @Column(name = "start_date")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "reason")
    private FBCAReason crossingReason;

    @Column(name = "transport")
    private FBCATransport transportType;

    @Column(name = "valid")
    private boolean valid;

    public FBCAEntity() {
    }

    public FBCAEntity(long id, String firstName, String lastName, String email, String phone, String passportNumber, Date startDate, Date endDate, FBCAReason crossingReason, FBCATransport transportType, boolean valid) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.passportNumber = passportNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.crossingReason = crossingReason;
        this.transportType = transportType;
        this.valid = valid;
    }

    public FBCAEntity(String firstName, String lastName, String email, String phone, String passportNumber, Date startDate, Date endDate, FBCAReason crossingReason, FBCATransport transportType, boolean valid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.passportNumber = passportNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.crossingReason = crossingReason;
        this.transportType = transportType;
        this.valid = valid;
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

    public Date getStartDate() { return startDate; }

    public Date getEndDate() { return endDate; }

    public FBCAReason getCrossingReason() {
        return crossingReason;
    }

    public FBCATransport getTransportType() { return transportType; }

    public boolean isValid() {
        return valid;
    }
}
