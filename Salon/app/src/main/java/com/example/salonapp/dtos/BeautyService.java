package com.example.salonapp.dtos;

import com.google.firebase.database.GenericTypeIndicator;

import java.util.Map;

public class BeautyService {

    public Map<String, String> services;

    public Map<String, String> getServices() {
        return services;
    }

    public void setServices(Map<String, String> services) {
        this.services = services;
    }
}
