package ro.simo.ChanosArtShop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ro.simo.ChanosArtShop.Database.OrderDAO;
import ro.simo.ChanosArtShop.Database.Product;
import ro.simo.ChanosArtShop.Database.ProductDAO;
import ro.simo.ChanosArtShop.Security.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    UserSession userSession;

    @Autowired
    OrderDAO orderDAO;

    @GetMapping("/product")
    public ModelAndView product(@RequestParam("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("product");
        //verific daca utilizatorul este logat sau nu
        if (userSession.getUserId() == 0) {
            modelAndView.addObject("history", "Login");
        } else {
            modelAndView.addObject("history", "Istoric comenzi");
        }
        Product product = productDAO.findById(id);
        modelAndView.addObject("product", product);
        int productCounter = 0;
        for (int quantityForProduct : userSession.getShoppingCart().values()) {
            productCounter = productCounter + quantityForProduct;
        }
        modelAndView.addObject("shoppingCartSize", productCounter );

        return modelAndView;
    }

    @PostMapping("/add-to-cart")
    public ModelAndView addToCart(@RequestParam("productId") Integer id) {
        //server-ul trebuie sa tina minte id-urile de produse pe care userul le are in comanda
        userSession.addNewProduct(id);

        return new ModelAndView("redirect:product?id=" + id);
    }

    @GetMapping("/cart")
    public ModelAndView shoppingCart() {
        ModelAndView modelAndView = new ModelAndView("/cart");
        List<CartProduct> productsFromCart = new ArrayList<>();
        //verific daca utilizatorul este logat sau nu
        if (userSession.getUserId() == 0) {
            modelAndView.addObject("login", "Login");
        } else {
            modelAndView.addObject("history", "Istoric comenzi");
        }
        //cantitatea din cosul de cumparaturi
        int productCounter = 0;
        for (int quantityForProduct : userSession.getShoppingCart().values()) {
            productCounter = productCounter + quantityForProduct;
        }
        modelAndView.addObject("shoppingCartSize", productCounter );

        for (Map.Entry<Integer, Integer> entry : userSession.getShoppingCart().entrySet()) {
            int quantity = entry.getValue();
            int productId = entry.getKey();
            Product productFromDatabase = productDAO.findById(productId);
            CartProduct cartProduct = new CartProduct();
            cartProduct.setQuantity(quantity);
            cartProduct.setId(productFromDatabase.getId());
            cartProduct.setName(productFromDatabase.getName());
            cartProduct.setPhoto1(productFromDatabase.getPhoto1());
            cartProduct.setPhoto2(productFromDatabase.getPhoto2());
            cartProduct.setPhoto3(productFromDatabase.getPhoto3());
            cartProduct.setPrice(productFromDatabase.getPrice()*quantity);
            productsFromCart.add(cartProduct);
        }

        modelAndView.addObject("products", productsFromCart);
        return modelAndView;
    }

    @GetMapping("save-cart")
    public ModelAndView saveCart() {
        ModelAndView modelAndView = new ModelAndView("redirect:after.html");

        //salvam in baza de date cosul de cumparaturi
        orderDAO.newOrder(userSession.getUserId(), userSession.getShoppingCart());

        //verific daca utilizatorul este logat sau nu
        if (userSession.getUserId() == 0) {
            modelAndView.addObject("login", "Login");
            return new ModelAndView("redirect:/index.html");
        } else {
            modelAndView.addObject("history", "Istoric comenzi");
            //se curata cosul de cumparaturi
            userSession.getShoppingCart().clear();
            return modelAndView;
        }
    }

}
