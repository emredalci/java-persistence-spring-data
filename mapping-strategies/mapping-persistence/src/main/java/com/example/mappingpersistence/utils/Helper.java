package com.example.mappingpersistence.utils;

import java.util.Date;

public class Helper {

    private Helper(){
        throw new RuntimeException();
    }

    public static Date tomorrow(){
        return new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
    }
}
