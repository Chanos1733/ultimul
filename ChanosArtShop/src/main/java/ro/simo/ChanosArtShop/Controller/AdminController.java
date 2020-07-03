package ro.simo.ChanosArtShop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ro.simo.ChanosArtShop.Database.Product;
import ro.simo.ChanosArtShop.Database.ProductDAO;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    ProductService productService;

    @GetMapping("/admin/products")
    public ModelAndView viewProducts() {
        ModelAndView modelAndView = new ModelAndView("admin/products");
        List<Product> products = productDAO.findByPage(1);
        modelAndView.addObject("products", products);

        return modelAndView;
    }

    @GetMapping("/admin/products-paginated")
    @ResponseBody
    public List<Product> viewProducts(@RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber) {
        List<Product> products = productDAO.findByPage(pageNumber);
        return products;
    }

    @PostMapping("/admin/remove-product")
    @ResponseBody
    public String removeProduct(@RequestParam(value = "id") Integer productId) {
        //stergem produsul din baza de date
        productDAO.deleteProduct(productId);
        return "ok";
    }

    @PostMapping("/admin/save-product")
    @ResponseBody
    public String saveProduct(@RequestParam("name") String name,
                              @RequestParam("description") String description,
                              @RequestParam("price") Double price) {
        return productService.saveProduct(name, description, price);
    }
}
