package com.dimasolovey.dispatchcenter;

import com.dimasolovey.flyer.Flyer;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Dmitry on 10/31/2015.
 */

// Класс диспетчерского центра
public class DispatchCenter extends ReceiverAdapter {

    /*
Объявляем экземпляры класса: канал, аддрес дисп. центра, список летаьельных аппаратов, список полученных сообщений,
логгер
    */
    private JChannel channel;
    private static Address address;
    private List<Flyer> listOfFlyers = new ArrayList<Flyer>();
    private List<Message> listOfMessages = new ArrayList<Message>();
    private static Logger log = Logger.getLogger(DispatchCenter.class.getName());

// В конструкторе создаем канал и определяем адресс диспетчерского центра
    public DispatchCenter() {
        try {
            channel = new JChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        address = channel.getAddress();
    }

// Метод логирования сообщений, приходящих  в диспетчерский центр
    public void log() {
        for (int i = 0; i < listOfMessages.size() ; i++) {
            log.info(String.valueOf(listOfMessages.get(i).getObject()));
        }
        listOfMessages.clear();
    }

// Метод добавления летательных аппаратов в список
    public void addListOfFlyers(Flyer flyer) {
        listOfFlyers.add(flyer);
    }

// Метод соединения с класером "AviaSimulatorCluster"
    public void connectCluster() {
        try {
            channel.setReceiver(this);
            channel.connect("AviaSimulatorCluster");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// Метод добавления сообщений в список, когда сообщения приходят в дисп. центр
    public void receive(Message msg) {
        listOfMessages.add(msg);
    }

    public JChannel getChannel() {
        return channel;
    }
}
