package ro.simo.ChanosArtShop.Controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.model.IModel;
import ro.simo.ChanosArtShop.Database.*;
import ro.simo.ChanosArtShop.Security.UserSession;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    UserSession userSession;

    @GetMapping("/admin/products")
    public ModelAndView adminProducts(@RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber) {
        ModelAndView modelAndView = new ModelAndView("/admin/products");
     if (userSession.getUserEmail().equals("chanos.art@gmail.com")) {
        List<Product> allProducts = productDAO.findAll();
        List<Product> products = productDAO.findByPage(pageNumber);
        modelAndView.addObject("products", products);
        int numberOfPages = 0;
        for (int i = 0; i < (allProducts.size() / 7) + 1; i++) {
            numberOfPages = numberOfPages + 1;
        }

        if (pageNumber == 1) {
            modelAndView.addObject("nextPage", "http://localhost:8080/admin/products?pageNumber=" + (pageNumber + 1));
            return modelAndView;
        } else if (pageNumber == numberOfPages) {
            // ar trebui sa scot si butonul de next la ultima pagina
            //Cum fac asta? :((
            modelAndView.addObject("prevPage", "http://localhost:8080/admin/products?pageNumber=" + (pageNumber - 1));
            return modelAndView;
        }
        modelAndView.addObject("nextPage", "http://localhost:8080/admin/products?pageNumber=" + (pageNumber + 1));
        modelAndView.addObject("prevPage", "http://localhost:8080/admin/products?pageNumber=" + (pageNumber - 1));
        return modelAndView;

        } else {
            return new ModelAndView("redirect:/index.html");
        }
    }


    @PostMapping("/admin/remove-product")
    @ResponseBody
    public String removeProduct(@RequestParam(value = "id") Integer productId) {
        productDAO.deleteProduct(productId);
        return "ok";
    }

    @PostMapping("/admin/save-product")
    @ResponseBody
    public String saveProduct(@RequestParam("name") String name,
                              @RequestParam("price") double price,
                              @RequestParam("materials") String materials,
                              @RequestParam("dimensions") String dimensions,
                              @RequestParam("color") String color,
                              @RequestParam("description") String description,
                              @RequestParam("photo1") String photo1,
                              @RequestParam("photo2") String photo2,
                              @RequestParam("photo3") String photo3) {
        return productService.saveProduct(name, price, materials, dimensions, color, description, photo1, photo2, photo3);
    }

    /*@GetMapping("admin/edit")
    public ModelAndView edit(@RequestParam ("id") Integer productId) {
        return new ModelAndView("edit-product?id=",productId);
    }

    @GetMapping("/admin/edit-product")
    public ModelAndView editProduct(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "price" , defaultValue = "0") double price,
                              @RequestParam(value = "materials", required = false) String materials,
                              @RequestParam(value = "dimensions", required = false) String dimensions,
                              @RequestParam(value = "color", required = false) String color,
                              @RequestParam(value = "description", required = false) String description,
                              @RequestParam(value = "photo1", required = false) String photo1,
                              @RequestParam(value = "photo2", required = false) String photo2,
                              @RequestParam(value = "photo3", required = false) String photo3,
                              @RequestParam(value = "id", defaultValue = "0") Integer productId) {
        ModelAndView modelAndView = new ModelAndView()
        Product product = productDAO.findById(productId);
        modelAndView.addObject("product", product);

        return productService.editProduct(productId, name, price, materials, dimensions, color, description, photo1, photo2, photo3);
    }*/


    @GetMapping("/admin/details")
    public ModelAndView details(@RequestParam(value = "id") Integer productId) {
        ModelAndView modelAndView = new ModelAndView("admin/details");
        Product product = productDAO.findById(productId);
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/search-admin")
    public ModelAndView search(@RequestParam("cautat") String cautat) {
        ModelAndView modelAndView = new ModelAndView("admin/products");
        List<Product> products = productDAO.searchProduct(cautat);
        if (products.size() == 0) {
            modelAndView.addObject("noResult", "Nu s-a gasit nici un rezultat!");
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }
}
