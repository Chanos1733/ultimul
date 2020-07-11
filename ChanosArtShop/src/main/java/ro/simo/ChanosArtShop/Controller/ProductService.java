package ro.simo.ChanosArtShop.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simo.ChanosArtShop.Database.ProductDAO;


@Service
public class ProductService {

    @Autowired
    ProductDAO productDAO;

    public String saveProduct(String name, double price, String materials, String dimensions, String color , String description, String photo1, String photo2,
                              String photo3) {
        //validam datele de intrare
        if (description.length() < 20) {
            return "Introduceti o lungime mai mare de 20 caractere";
        }

        productDAO.saveProduct(name, price,  materials, dimensions, color, description, photo1, photo2, photo3);
        return "ok";
    }
}
