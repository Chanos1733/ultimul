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

    public void saveProduct(String name, String description, double price) {
        jdbcTemplate.update("INSERT INTO product (id, name, description, category_id, price, quantity, photo_file) VALUES (null, ?, ?, ?, ?, ?,?)",
                name, description, 1, price, null, null
        );
    }

}
