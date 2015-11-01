package com.dimasolovey.main;

import com.dimasolovey.flyer.Aircraft;
import com.dimasolovey.dispatchcenter.DispatchCenter;
import com.dimasolovey.flyer.Flyer;
import com.dimasolovey.flyer.Helicopter;
import org.jgroups.Address;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dmitry on 10/31/2015.
 */

// Основной класс Симулятора с main-методом
public class Simulator extends  Thread {

    static  DispatchCenter dispatchCenter = new DispatchCenter();

    public static void main(String[] args) throws Exception {
        // Устанавливаем свойство preferIPv4Stack, чтобы использовать стек IPv4
        System.setProperty("java.net.preferIPv4Stack" , "true");
        new Simulator().start();
        // Определяем адресс диспетчерского центра
        final Address dispatchCenterAddress = dispatchCenter.getChannel().getAddress();
        // Коннектим диспетчерский центр к кластеру
        dispatchCenter.connectCluster();
        // Создаем вертолеты и самолеты и добавляем в список
        final Flyer mi17 = new Helicopter("MI-17", 12234, 50.2701, 30.3185, 1000, 13);
        dispatchCenter.addListOfFlyers(mi17);
        final Flyer mi171 = new Helicopter("MI-171", 156785, 50.0855, 28.4626, 700, 76);
        dispatchCenter.addListOfFlyers(mi171);
        final Flyer mi2 = new Helicopter("MI-2", 178934, 53.3187, 22.4998, 1300, 53);
        dispatchCenter.addListOfFlyers(mi2);
        final Flyer A300 = new Aircraft("A300", 456734, 61.2589, 8.0404, 5300, 101);
        dispatchCenter.addListOfFlyers(A300);
        final Flyer L39 = new Aircraft("L39", 675321, 37.5855, 4.0716, 2300, 120);
        dispatchCenter.addListOfFlyers(L39);
        final Flyer B777 = new Aircraft("B777", 980456, 7.4077, 84.1654, 6500, 63);
        dispatchCenter.addListOfFlyers(B777);
        Timer updateDataTimer = new Timer();
// Раз в минуту изменяем параметры самолетов и вертолетов и коннектимся к кластеру, чтобы передать сообщение с
// измененными параметрами
        updateDataTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mi17.changeParameters(0.05, 0.04, 10, 1);
                mi17.connectCluster(dispatchCenterAddress, mi17);
                mi171.changeParameters(0.03, 0.04, 10, 2);
                mi171.connectCluster(dispatchCenterAddress, mi171);
                mi2.changeParameters(0.06, 0.03, 10, 1);
                mi2.connectCluster(dispatchCenterAddress, mi2);
                A300.changeParameters(0.1, 0.12, 10, 3);
                A300.connectCluster(dispatchCenterAddress, A300);
                L39.changeParameters(0.2, 0.35, 15, 4);
                L39.connectCluster(dispatchCenterAddress, L39);
                B777.changeParameters(0.15, 0.11, 12, 2);
                B777.connectCluster(dispatchCenterAddress, B777);
            }
        }, 1000, 1000);
        Timer logDataTimer = new Timer();
// Раз в 10 секунд логируем в консоль сообщения с измененными параметрами (широта, долгота, высота, курс)
        logDataTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                dispatchCenter.log();
            }
        },10000, 10000);
    }
// Запускаем в отдельном потоке, чтобы пользователь мог выйти из программы, вводом слов quit или stop или exit
    @Override
    public void run() {
        while(true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = null;
            try {
                input = reader.readLine();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
            if (input != null) {
                if (input.equals("quit") || input.equals("stop") || input.equals("exit")) {
                    dispatchCenter.getChannel().close();
                    System.exit(0);
                }
            }
        }
    }
}


