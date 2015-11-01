package com.dimasolovey.flyer;

import com.google.gson.Gson;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Dmitry on 10/31/2015.
 */

// Класс инкапсулирующий Летательный аппарат (родительский класс для вертолета и самолета)
public class Flyer {

// Поля: тип, бортовой номер, широта, долгота, высота полета, курс
    private String type;
    private int number;
    private double latitude;
    private double longitude;
    private int altitude;
    private int course;

    public Flyer(String type, int number, double latitude, double longitude, int altitude, int course) {
        this.type = type;
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.course = course;
    }

    // Метод изменения параметров летательного аппарата
    public void changeParameters(double latitude, double longitude, int altitude, int course) {
        // Широта и долгота - 4 знака после запятой
        this.latitude = new BigDecimal(this.latitude+latitude).setScale(4, RoundingMode.UP).doubleValue();
        this.longitude = new BigDecimal(this.longitude + longitude).setScale(4, RoundingMode.UP).doubleValue();
        this.altitude = this.altitude + altitude;
        this.course = this.course + course;
    }

// Метод соединения с кластером "AviaSimulatorCluster" и отправки сообщений с измененными параметрами
    public void connectCluster(Address address, Flyer flyer) {
        try {
           JChannel channel = new JChannel();
            channel.connect("AviaSimulatorCluster");
            Message message = new Message(address, null, getGsonMessage(flyer));
            channel.send(message);
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// Метод получения Gson-сообщения
    private String getGsonMessage(Flyer flyer) {
       Gson gson = new Gson();
        return gson.toJson(flyer);
    }
}
