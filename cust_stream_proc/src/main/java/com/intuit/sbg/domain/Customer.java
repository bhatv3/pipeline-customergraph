package com.intuit.sbg.domain;

import org.neo4j.ogm.annotation.*;

/**
 * Created by vikasbhat on 2/27/18.
 */
@NodeEntity
public class Customer {

    @Id  @GeneratedValue
    private Long id;

    private String fullName;

    @Index(unique = true)
    @Relationship(type="HAS_ADDRESS", direction = Relationship.OUTGOING)
    private Address address;

    @Index(unique = true)
    @Relationship(type="HAS_PHONE", direction = Relationship.OUTGOING)
    private Phone phone;

    @Index(unique = true)
    @Relationship(type="HAS_EMAIL", direction = Relationship.OUTGOING)
    private Email email;

    public Customer() {
    }

    public Customer(String fullName, Address address, Phone phone, Email email) {
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }


}
