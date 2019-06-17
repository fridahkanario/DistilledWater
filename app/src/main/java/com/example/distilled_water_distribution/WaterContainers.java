package com.example.distilled_water_distribution;


// model class denoting the water container properties
public class WaterContainers {
    private String name;
    private int capacity;
    private int thumbnail;

    public WaterContainers() {
    }

    public WaterContainers(String name, int capacity, int thumbnail) {
        this.name = name;
        this.capacity = capacity;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int numOfSongs) {
        this.capacity = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
