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

//    public List<Integer> getUserIdForComment(Integer id_product) {
//        List<Integer> userId = jdbcTemplate.queryForObject("select id_user from comments where id_product = " + id_product,List.class);
//        return userId;
//    }
//
//    public List<String> getUserNameForComment(List userId) {
//        List<String> userNames = null;
//       for ( int i=0; i>userId.size() ; i++) {
//           String userNameCForCommentFromDatabase = jdbcTemplate.queryForObject("select name from user where id = " + i, String.class);
//           userNames.add(userNameCForCommentFromDatabase);
//       }
//       return userNames;
//    }

    public void addCommentOnProduct(String email, String comment, int id_product) {


        Integer id_user = jdbcTemplate.queryForObject("select id from user where email = " + email,Integer.class);
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTime.getYear()+" "+dateTime.getMonth()+" "+dateTime.getDayOfMonth()+" ~ "+dateTime.getHour()+":"+ dateTime.getMinute();
        jdbcTemplate.update("INSERT INTO comments (id, id_product, id_user, date, comment) VALUES (null, ?,?,?,?)",
                id_product, id_user, date, comment
        );
    }
}
