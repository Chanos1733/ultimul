package ro.simo.ChanosArtShop.Database;

public class Order {

    private int id;
    private int userIid;
    private String address;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUserIid() {
        return userIid;
    }

    public void setUserIid(int userIid) {
        this.userIid = userIid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
