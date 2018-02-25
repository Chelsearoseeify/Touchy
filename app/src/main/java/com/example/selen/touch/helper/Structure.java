package com.example.selen.touch.helper;

/**
 * Created by Administrator on 16/12/2017.
 */

public class Structure {

    private Integer id;
    private String name;
    private String segmento;
    private String image;

    public Structure(Integer id, String name, String segmento, String image) {
        this.id = id;
        this.name = name;
        this.segmento = segmento;
        this.image = image;
    }

    public Integer getId(){
        return id;
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

