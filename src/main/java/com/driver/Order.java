package com.driver;

public class Order {

    private String id;
    private int deliveryTime;


    public Order(String id, String deliveryTime) {

        if (!deliveryTime.matches("\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Invalid time format. Expected HH:MM");
        }
        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM

        int hh = Integer.parseInt(deliveryTime.substring(0,2));
        int mm = Integer.parseInt(deliveryTime.substring(3));

        // 24-hour format
        if (hh < 0 || hh > 23 || mm < 0 || mm > 59) {
            throw new IllegalArgumentException("Invalid time. Hours must be 00-23 and minutes 00-59.");
        }

        this.deliveryTime = hh*60 + mm;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
