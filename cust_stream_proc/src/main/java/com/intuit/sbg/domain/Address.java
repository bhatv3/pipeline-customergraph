package com.intuit.sbg.domain;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.neo4j.ogm.annotation.Id;

/**
 * Created by vikasbhat on 2/27/18.
 */
public class Address {

    @Id
    private String id;
    private String street;
    private String zipcode;

    public Address() {
    }

    public Address(String street, String zipcode) {
        this.street = street;
        this.zipcode = zipcode;
        this.id = Hashing.murmur3_128().hashString(street + zipcode, Charsets.UTF_8).toString();
    }

    public static long longHash(String string) {
        long h = 98764321261L;
        int l = string.length();
        char[] chars = string.toCharArray();

        for (int i = 0; i < l; i++) {
            h = 31 * h + chars[i];
        }
        if (h < 0) return h * -1;
        return h;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
