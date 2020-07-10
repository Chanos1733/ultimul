package ro.simo.ChanosArtShop.Database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Comment> findAllForProduct(int id_product) {
        return jdbcTemplate.query("select * from comment where id_product = " + id_product , new CommentRowMapper());
    }
}