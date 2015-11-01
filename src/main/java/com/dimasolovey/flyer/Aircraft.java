package com.dimasolovey.flyer;

/**
 * Created by Dmitry on 10/31/2015.
 */

// Класс Самолет (потомок класса Летательный аппарат)
public class Aircraft extends Flyer{

    public Aircraft(String type, int number, double latitude, double longitude, int altitude, int course) {
        super(type, number, latitude, longitude, altitude, course);
    }
}
