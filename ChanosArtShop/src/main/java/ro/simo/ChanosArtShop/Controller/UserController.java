package ro.simo.ChanosArtShop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ro.simo.ChanosArtShop.Database.*;
import ro.simo.ChanosArtShop.Security.UserSession;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserSession userSession;

    @Autowired
    ProductDAO productDAO;

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
        return new ModelAndView("index");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        String message;
        return new ModelAndView("register");
    }

    @PostMapping("/login")
    public ModelAndView index(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        List<User> userList = userService.findByEmail(email);
        if (userList.size() == 0) {
            modelAndView.addObject("message", "Credentialele nu sunt corecte!");
        }
        if (userList.size() > 1) {
            modelAndView.addObject("message", "Credentialele nu sunt corecte!");
        }
        if (userList.size() == 1) {
            User userFromDatabase = userList.get(0);
            if (!userFromDatabase.getPassword().equals(password)) {
                modelAndView.addObject("message", "Credentialele nu sunt corecte!");
            } else { //a introdus credentialele cu succes
                userSession.setUserId(userFromDatabase.getId());
                modelAndView = new ModelAndView("redirect:/dashboard");
            }
        }
        return modelAndView;
    }

    @GetMapping("/dashboard") // la fiecare pagina trebuie facuta verificarea, pengtru a afisa istoric sau login :D
    public ModelAndView home() {

        List<Product> products = productDAO.findAll();


        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("product", products);


        //verific daca utilizatorul este logat sau nu
        if (userSession.getUserId() == 0) {
            modelAndView.addObject("cart", "Login");
        } else {
            modelAndView.addObject("cart", "Istoric comenzi");
        }

        modelAndView.addObject("product", products);

        return modelAndView;
    }

    @GetMapping("/back-to-login")
    public ModelAndView back() {
        return new ModelAndView("/index");
    }
}
