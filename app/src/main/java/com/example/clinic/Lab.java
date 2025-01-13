package com.example.clinic;

import java.io.Serializable;

public class Lab implements Serializable {
    private String name;
    private String description;
    private String date;

    // Constructor
    public Lab(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}



