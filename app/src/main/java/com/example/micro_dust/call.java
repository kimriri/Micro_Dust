package com.example.micro_dust;

public class call {

    private String Authorization;
    private String lat;
    private String lon;
    private String in;
    private String out;


    @Override
    public String toString() {
        return Authorization+lat + lon + in + out;
    }



}
