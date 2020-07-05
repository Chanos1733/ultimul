package ro.simo.ChanosArtShop.Database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<User> findByEmail(String email) {
        return jdbcTemplate.query("select * from user where email='" + email + "';", new UserRowMapper());
    }

    public void create( String name,String country, String city, String adress,String phone, String email,String password) {
       jdbcTemplate.update("insert into user values (null,?,?,?,?,?,?,?)", name, country, city, adress, phone, email, password);
    }
}
