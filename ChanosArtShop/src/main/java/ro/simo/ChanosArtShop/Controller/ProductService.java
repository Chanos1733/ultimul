package ro.simo.ChanosArtShop.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simo.ChanosArtShop.Database.ProductDAO;

@Service
public class ProductService {

    @Autowired
    ProductDAO productDAO;

    public String saveProduct(String name, String description, double price) {
        //validam datele de intrare
        if (description.length() < 5) {
            return "Introduceti o lungime mai mare de 5 caractere";
        }

        productDAO.saveProduct(name, description, price);
        return "ok";
    }
}
