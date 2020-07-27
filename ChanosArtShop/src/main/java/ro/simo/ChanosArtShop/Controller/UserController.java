package ro.simo.ChanosArtShop.Controller;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ro.simo.ChanosArtShop.Database.*;
import ro.simo.ChanosArtShop.Security.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserSession userSession;

    @Autowired
    ProductDAO productDAO;

    @Autowired
    OrderDAO orderDAO;


    //parolele sunt identice?
    //emailul este deja in baza de date?
    //stochez in baza de date un nou utilizator
    @GetMapping("/register-form")
    public ModelAndView registerAction(@RequestParam("name") String name,
                                       @RequestParam("country") String country,
                                       @RequestParam("city") String city,
                                       @RequestParam("adress") String adress,
                                       @RequestParam("phone") String phone,
                                       @RequestParam("email") String email,
                                       @RequestParam("password") String password,
                                       @RequestParam("password-again") String password2) {
        ModelAndView modelAndView = new ModelAndView("register");

        List<User> userList = userService.findByEmail(email);
        if (!password.equals(password2)) {
            modelAndView.addObject("message", "Parolele nu sunt identice!");
            return modelAndView;
        } else {
            //salvare in baza de date
            try {
                userService.save(name, country, city, adress, phone, email, password);
            } catch (InvalidPassword invalidPassword) {
                String messageException = invalidPassword.getMessage();
                modelAndView.addObject("message", messageException);
                return modelAndView;
            }
        }
        //redirect user catre login
        return new ModelAndView("redirect:/index.html");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register");
    }

    @PostMapping("/login")
    public ModelAndView index(@RequestParam("email") String email,
                              @RequestParam("password") String password) {
        ModelAndView modelAndView = new ModelAndView("index");
        List<User> userList = userService.findByEmail(email);

        if (email.equals("chanos.art@gmail.com")) {
            User userFromDatabase = userList.get(0);
            if (userFromDatabase.getPassword().equals(DigestUtils.md5Hex(password))) {
                return new ModelAndView("redirect:/admin/products");
            }
        }
        if (userList.size() == 0) {
            modelAndView.addObject("message", "Credentialele nu sunt corecte!");
        }
        if (userList.size() > 1) {
            modelAndView.addObject("message", "Credentialele nu sunt corecte!");
        }
        if (userList.size() == 1) {
            User userFromDatabase = userList.get(0);
            if (!userFromDatabase.getPassword().equals(DigestUtils.md5Hex(password))) {
                modelAndView.addObject("message", "Credentialele nu sunt corecte!");
            } else { //a introdus credentialele cu succes
                userSession.setUserEmail(userFromDatabase.getEmail());
                userSession.setUserId(userFromDatabase.getId());
                modelAndView = new ModelAndView("redirect:/dashboard");
            }
        }
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout() {
        userSession.setUserId(0);
        return new ModelAndView("redirect:/index.html");
    }

    @GetMapping("/dashboard") // la fiecare pagina trebuie facuta verificarea, pengtru a afisa istoric sau login :D
    public ModelAndView home() {

        List<Product> products = productDAO.findAll();
        for (Product p : products) {
            p.setUrl("product?id=" + p.getId());
        }
        ModelAndView modelAndView = new ModelAndView("dashboard");

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
        modelAndView.addObject("product", products);

        return modelAndView;
    }

    @GetMapping("/back-to-login")
    public ModelAndView back() {
        return new ModelAndView("/index");
    }

    @GetMapping("/About")
    public ModelAndView about() {
        ModelAndView modelAndView = new ModelAndView("About");

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
        return modelAndView;
    }

    @GetMapping("/Contact")
    public ModelAndView contact() {
        ModelAndView modelAndView = new ModelAndView("Contact");
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
        return modelAndView;
    }

    @GetMapping("/ordersHistory")
    public ModelAndView history() {
        List<Order> orders = orderDAO.findOrderForUser(userSession.getUserId());
        ModelAndView modelAndView = new ModelAndView("orders");
        modelAndView.addObject("orders", orders);

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
        return modelAndView;
    }

    @PostMapping("/search")
    public ModelAndView search(@RequestParam("cautat") String cautat) {
        ModelAndView modelAndView = new ModelAndView("dashboard");
        List<Product> products = productDAO.searchProduct(cautat);
        for (Product p : products) {
            p.setUrl("product?id=" + p.getId());
        }
        if (products.size() == 0) {
            modelAndView.addObject("noResult", "Nu s-a gasit nici un rezultat!");
        }
        boolean logged = false;
        if (userSession.getUserId() != 0) {
            logged = true;
        }
        modelAndView.addObject("logged", logged);

        modelAndView.addObject("product", products);
        return modelAndView;
    }


}
