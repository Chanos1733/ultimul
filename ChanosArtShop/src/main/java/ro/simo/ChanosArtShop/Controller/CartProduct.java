package ro.simo.ChanosArtShop.Controller;

import ro.simo.ChanosArtShop.Database.Product;

public class CartProduct extends Product {

    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}