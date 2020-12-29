package com.android.address_book;

public class People {
    String no;
    String name;
    String tel;
    String email;
    String relation;
    String memo;
    String image;
    String favorite;
    String emergency;

    public People(String no, String name, String tel, String email, String relation, String memo, String image, String favorite, String emergency) {
        this.no = no;
        this.name = name;
        this.tel = tel;
        this.email = email;
        this.relation = relation;
        this.memo = memo;
        this.image = image;
        this.favorite = favorite;
        this.emergency = emergency;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }
}
