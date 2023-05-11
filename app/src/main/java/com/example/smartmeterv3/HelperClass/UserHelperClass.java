package com.example.smartmeterv3.HelperClass;

public class UserHelperClass {

    String phone_number,name,pin,electricity_reading,limit,price,url_state;
    public UserHelperClass() { }

    public UserHelperClass(String phone_number, String name, String pin, String electricity_reading, String limit, String price, String url_state) {
        this.phone_number = phone_number;
        this.name = name;
        this.pin = pin;
        this.electricity_reading = electricity_reading;
        this.limit = limit;
        this.price = price;
        this.url_state = url_state;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElectricity_reading() {
        return electricity_reading;
    }

    public void setElectricity_reading(String electricity_reading) {
        this.electricity_reading = electricity_reading;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl_state() {
        return url_state;
    }

    public void setUrl_state(String url_state) {
        this.url_state = url_state;
    }
}
