package com.app.nEdge.models;

public class users {

    public String Name;
    public String email;
    public String phoneNumber;
    public String register;

    public users(String name, String email, String phoneNumber, String register) {
        Name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.register = register;


    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }


}





