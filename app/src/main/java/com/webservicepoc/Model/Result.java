package com.webservicepoc.Model;

/**
 * Created by Bryant on 11/4/2014.
 */
public class Result {
    private String name;
    private String address;

    public Result(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
