package ro.simo.ChanosArtShop.Database;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    public void save(String name, String country, String city, String adress, String phone, String email, String password) throws  InvalidPassword{
        if (password.length() < 6) {
            throw new InvalidPassword("Parola trebuie sa aiba mai mult de 6 caractere!");
        }
        if (userDAO.findByEmail(email).size() > 0) {
            throw new InvalidPassword("Exista deja un cont cu acest Email!");
        }
        //folosim functia md5 pentru a cripta parola
        String passwordMD5 = DigestUtils.md5Hex(password);
        userDAO.create(name, country, city, adress, phone, email, passwordMD5);
    }

    public List<User> findByEmail(String email) {
        return userDAO.findByEmail(email);
    }
    

}
