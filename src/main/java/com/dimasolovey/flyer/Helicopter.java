package com.dimasolovey.flyer;

/**
 * Created by Dmitry on 10/31/2015.
 */

// Класс Вертолет (потомок класса Летательный аппарат)
public class Helicopter extends Flyer{

    public Helicopter(String type, int number, double latitude, double longitude, int altitude, int course) {
        super(type, number, latitude, longitude, altitude, course);

    }
}
