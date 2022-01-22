package com.example.yarbi;

public class User
{
    public String email,name,phone,password;

    public User(){};
    public User(String name,String phone, String email, String password)
    {
        this.email=email;
        this.name = name;
        this.phone=phone;
        this.password=password;
    };
}
