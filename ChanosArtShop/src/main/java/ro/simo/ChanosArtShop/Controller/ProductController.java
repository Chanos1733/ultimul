package ro.simo.ChanosArtShop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ro.simo.ChanosArtShop.Database.*;
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

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    UserDAO userDAO;

    @GetMapping("/product")
    public ModelAndView product(@RequestParam("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("product");

        //verific daca utilizatorul este logat sau nu
        boolean logged = false;
        if (userSession.getUserId() != 0) {
            logged = true;
        }
        Product product = productDAO.findById(id);
        List<Comment> comments = commentDAO.findCommentsForProduct(id);
        modelAndView.addObject("comments", comments);

//        List<String> userNames =  //numele din lista de comentarii.. nu din userSession
//
//        modelAndView.addObject("userNames",userName);

        int productCounter = 0;
        for (int quantityForProduct : userSession.getShoppingCart().values()) {
            productCounter = productCounter + quantityForProduct;
        }

        modelAndView.addObject("shoppingCartSize", productCounter);
        modelAndView.addObject("product", product);
        modelAndView.addObject("logged", logged);

        return modelAndView;
    }

    @PostMapping("/add-to-cart-product")
    public ModelAndView addToCartProd(@RequestParam("productId") Integer id) {
        //server-ul trebuie sa tina minte id-urile de produse pe care userul le are in comanda
        userSession.addNewProduct(id);

        return new ModelAndView("redirect:product?id=" + id);
    }

    @PostMapping("/add-to-cart-dash")
    public ModelAndView addToCartDash(@RequestParam("productId") Integer id) {
        //server-ul trebuie sa tina minte id-urile de produse pe care userul le are in comanda
        userSession.addNewProduct(id);

        return new ModelAndView("redirect:/dashboard");
    }

    @GetMapping("/cart")
    public ModelAndView shoppingCart() {
        ModelAndView modelAndView = new ModelAndView("cart");
        List<CartProduct> productsFromCart = new ArrayList<>();
        //verific daca utilizatorul este logat sau nu
        boolean logged = false;
        if (userSession.getUserId() != 0) {
            logged = true;
        }
        modelAndView.addObject("logged", logged);
        //cantitatea din cosul de cumparaturi
        int productCounter = 0;
        for (int quantityForProduct : userSession.getShoppingCart().values()) {
            productCounter = productCounter + quantityForProduct;
        }
        modelAndView.addObject("shoppingCartSize", productCounter);
        double totalPrice = 0;
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
            cartProduct.setPrice(productFromDatabase.getPrice() * quantity);
            productsFromCart.add(cartProduct);
            totalPrice = totalPrice + cartProduct.getPrice();
        }
        modelAndView.addObject("totalPrice", totalPrice);
        modelAndView.addObject("products", productsFromCart);
        return modelAndView;
    }

    @GetMapping("save-cart")
    public ModelAndView saveCart() {
        ModelAndView modelAndView = new ModelAndView("redirect:after.html");

        //salvam in baza de date cosul de cumparaturi
        orderDAO.newOrder(userSession.getUserId(), userSession.getShoppingCart());
        double totalPrice = 0;
        for (Map.Entry<Integer, Integer> entry : userSession.getShoppingCart().entrySet()) {
            Product product = productDAO.findById(entry.getKey());
            totalPrice = totalPrice + (product.getPrice() * entry.getValue());
        }
        modelAndView.addObject("totalPrice", totalPrice);
        boolean logged = false;

        if (userSession.getUserId() == 0) {
            return new ModelAndView("redirect:/index.html");
        } else {
            logged = true;
            modelAndView.addObject("logged", logged);
            //se curata cosul de cumparaturi
            userSession.getShoppingCart().clear();
            return modelAndView;
        }
    }

    @GetMapping("/add-comment")
    public ModelAndView addCommentOnProduct(@RequestParam(value = "comment") String comment,
                                            @RequestParam(value = "productId") Integer id) {
        ModelAndView modelAndView = new ModelAndView("redirect:product?id=" + id);

        boolean logged = false;
        if (userSession.getUserId() != 0) {
            logged = true;
        }
        modelAndView.addObject("logged", logged);

        String email = userSession.getUserEmail();
        String user_name = userSession.getName();
        commentDAO.addCommentOnProduct(email, comment, id, user_name);
        Product product = productDAO.findById(id);
        modelAndView.addObject("product", product);

        return modelAndView;
    }

    @GetMapping("/update-cart")
    public ModelAndView updateCart(@RequestParam(value = "quantity", required = false) Integer quantity,
                                   @RequestParam("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("cart");
        List<CartProduct> productsFromCart = new ArrayList<>();
        boolean logged = false;
        if (userSession.getUserId() != 0) {
            logged = true;
        }
        modelAndView.addObject("logged", logged);
        double totalPrice = 0;
        userSession.updateQuantity(id, quantity);
        for (Map.Entry<Integer, Integer> entry : userSession.getShoppingCart().entrySet()) {
            int productId = entry.getKey();
            Product productFromDatabase = productDAO.findById(productId);
            CartProduct cartProduct = new CartProduct();
            cartProduct.setQuantity(quantity);
            cartProduct.setId(productFromDatabase.getId());
            cartProduct.setName(productFromDatabase.getName());
            cartProduct.setPhoto1(productFromDatabase.getPhoto1());
            cartProduct.setPhoto2(productFromDatabase.getPhoto2());
            cartProduct.setPhoto3(productFromDatabase.getPhoto3());
            cartProduct.setPrice(productFromDatabase.getPrice() * quantity);
            productsFromCart.add(cartProduct);
            totalPrice = totalPrice + cartProduct.getPrice();

        }
        modelAndView.addObject("totalPrice", totalPrice);

        modelAndView.addObject("products", productsFromCart);
        return modelAndView;
    }
}
