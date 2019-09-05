package com.example.findmyfriend;
public class Profile_info {
    String name,email,phone,sex,birthday,circle,friends,profile_pic,image;
    Profile_info(){

    }
    Profile_info(String name, String email, String phone, String sex,String birthday,String circle,String friends,String profile_pic, String image ){
        this.birthday = birthday;
        this.circle = circle;
        this.email = email;
        this.friends = friends;
        this.name = name;
        this.phone = phone;
        this.sex = sex;
        this.profile_pic = profile_pic;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCircle() {
        return circle;
    }

    public String getFriends() {
        return friends;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSex() {
        return sex;
    }
}
