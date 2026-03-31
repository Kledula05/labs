package com.ustenko.model;

public class Character {
    private int id;
    private String name;
    private String status;
    private String species;
    private String gender;
    private String origin;

    public Character(int id, String name, String status, String species, String gender, String origin) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.species = species;
        this.gender = gender;
        this.origin = origin;
    }

    // Геттеры и Сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String toCsv() {
        return id + "," + name + "," + status + "," + species + "," + gender + "," + origin;
    }

    @Override
    public String toString() {
        return "Character{id=" + id + ", name='" + name + "', status='" + status +
                "', species='" + species + "', gender='" + gender + "', origin='" + origin + "'}";
    }
}