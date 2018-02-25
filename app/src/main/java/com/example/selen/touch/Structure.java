package com.example.selen.touch;

/**
 * Created by Administrator on 16/12/2017.
 */

public class Structure {

    private String name;
    private String segmento;
    private String image;

    public Structure(String name, String segmento, String image) {
        this.name = name;
        this.segmento = segmento;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getSegmento() {
        return segmento;
    }

    public String getImage() {
        return image;
    }
}

