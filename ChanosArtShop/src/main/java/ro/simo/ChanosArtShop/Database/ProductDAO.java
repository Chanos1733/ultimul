package ro.simo.ChanosArtShop.Database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Product> findAll() {
        return jdbcTemplate.query("select * from product", new ProductRowMapper());
    }
    public List<Product> findByPage(int pageNumber) {
        return jdbcTemplate.query("select * from product limit " + (pageNumber - 1)*5+ ",5", new ProductRowMapper());
    }

    public Product findById(Integer id) {
        return jdbcTemplate.query("select * from product where id = " + id, new ProductRowMapper()).get(0);

    }

    public void deleteProduct(Integer id) {
        jdbcTemplate.update("delete from product where id = ?", id);
    }

    public void saveProduct(String name, double price, String materials, String dimensions, String color , String description, String photo1, String photo2,
                            String photo3) {
        jdbcTemplate.update("INSERT INTO product (id, name, price,  materials, dimensions, color, description, photo1, photo2, photo3) VALUES (null, ?, ?, ?, ?, ?,?,?,?,?)",
                name, price,  materials, dimensions, color, description, photo1, photo2, photo3
        );
    }
    public List<Product> searchProduct(String cautat) {
        return jdbcTemplate.query("SELECT * FROM product WHERE name LIKE '%" + cautat +"%';", new ProductRowMapper());
    }

}
