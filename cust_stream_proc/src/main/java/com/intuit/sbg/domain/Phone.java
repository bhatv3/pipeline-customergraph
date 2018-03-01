package com.intuit.sbg.domain;

import org.neo4j.ogm.annotation.Id;

/**
 * Created by vikasbhat on 2/27/18.
 */
public class Phone {

    @Id
    private String number;

    public Phone() {
    }

    public Phone(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
