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
        if (description.length() < 5) {
            return "Introduceti o lungime mai mare de 5 caractere in descriere!";
        }

        productDAO.saveProduct(name, price,  materials, dimensions, color, description, photo1, photo2, photo3);
        return "Produsul a fost adaugat in baza de date!";
    }

    public void editProduct( Integer id,String name, double price, String materials, String dimensions, String color , String description, String photo1, String photo2,
                              String photo3) {

        productDAO.editProduct(id,name, price,  materials, dimensions, color, description, photo1, photo2, photo3);
            }
}
