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

//        List<String> userNames = commentDAO.getUserNameForComment(commentDAO.getUserIdForComment(id)); //numele din listade comentarii.. nu din userSession
//
//        modelAndView.addObject("userNames",userNames);

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
        ModelAndView modelAndView = new ModelAndView("/cart");
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
    public ModelAndView addCommentOnProduct(@RequestParam(value = "email", required = false) String email,
                                            @RequestParam(value = "comment", required = false) String comment,
                                            @RequestParam(value = "productId", required = false) Integer id) {
        ModelAndView modelAndView = new ModelAndView("redirect:product?id=" + id);
//        email = "gfrt@gmail.com";
//        comment = "asd";
//        id_product = 1;
        boolean logged = false;
        if (userSession.getUserId() != 0) {
            logged = true;
        }
        modelAndView.addObject("logged", logged);

        if (email.equals(userSession.getUserEmail())) {
            commentDAO.addCommentOnProduct(email, comment, id);
        } else {
            modelAndView.addObject("incorrectEmail", "Acest email nu este corect!");
        }
        Product product = productDAO.findById(id);
        modelAndView.addObject("product", product);

        return modelAndView;
    }

    @GetMapping("/update")
    public ModelAndView updateCart(@RequestParam(value = "quantity", required = false) Integer quantity) {
        ModelAndView modelAndView = new ModelAndView("/cart");
        List<CartProduct> productsFromCart = new ArrayList<>();
        boolean logged = false;
        if (userSession.getUserId() != 0) {
            logged = true;
        }
        modelAndView.addObject("logged", logged);

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
        }

        modelAndView.addObject("products", productsFromCart);
        return modelAndView;
    }
}
