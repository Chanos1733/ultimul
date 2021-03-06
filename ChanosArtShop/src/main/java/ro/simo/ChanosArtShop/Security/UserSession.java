package ro.simo.ChanosArtShop.Security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;

@Component
@SessionScope
public class UserSession {
    private int userId;
    private String userEmail;
    private String name;


    HashMap<Integer, Integer> shoppingCart = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public HashMap<Integer, Integer> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(HashMap<Integer, Integer> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void addNewProduct(Integer id) {
        if(shoppingCart.get(id) != null) {
            int currentQuantity = shoppingCart.get(id);
            int newQuantity = currentQuantity + 1;
            shoppingCart.put(id, newQuantity);
        } else {
            shoppingCart.put(id, 1);
        }
    }

    public void updateQuantity(Integer id,Integer quantity) {
            shoppingCart.put(id, quantity);

    }

}
