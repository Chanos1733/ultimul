package ro.simo.ChanosArtShop.Database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CommentDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Comment> findCommentsForProduct(Integer id_product) {
        return jdbcTemplate.query("select * from comments where id_product =" + id_product + " order by id desc", new CommentRowMapper());
    }

    public void addCommentOnProduct(String email, String comment, Integer id_product, String user_name) {

        Integer id_user = jdbcTemplate.queryForObject("select id from user where email='" + email + "';", Integer.class);
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTime.getYear() + " " + dateTime.getMonth() + " " + dateTime.getDayOfMonth() + " ~ " + dateTime.getHour() + ":" + dateTime.getMinute();
        user_name = jdbcTemplate.queryForObject("select name from user where id = '" + id_user + "';", String.class);
        jdbcTemplate.update("INSERT INTO comments (id, id_product, id_user, date, comment,user_name) VALUES (null, ?,?,?,?,?)",
                id_product, id_user, date, comment,user_name
        );

    }
}
