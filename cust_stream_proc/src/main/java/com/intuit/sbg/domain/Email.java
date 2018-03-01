package com.intuit.sbg.domain;

import org.neo4j.ogm.annotation.Id;

/**
 * Created by vikasbhat on 2/27/18.
 */
public class Email {

    @Id
    private String id;

    public Email(String id) {
        this.id = id;
    }

    public Email() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
