package com.kuycoding.favoritemoviecatalogue.model;

import org.json.JSONObject;

public class GenreModel {
    private int id;
    private String name;

    public GenreModel(JSONObject object) {

        try {
            int id = object.getInt("id");
            String name = object.getString("name");

            this.id = id;
            this.name = name;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
