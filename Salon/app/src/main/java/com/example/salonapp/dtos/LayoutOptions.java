package com.example.salonapp.dtos;

public class LayoutOptions {

    private  final int name;
    private final int resource;


    public LayoutOptions(int name,int resource) {
        this.name = name;
        this.resource = resource;
    }

    public int getName() {
        return name;
    }

    public int getResource() {
        return resource;
    }
}
