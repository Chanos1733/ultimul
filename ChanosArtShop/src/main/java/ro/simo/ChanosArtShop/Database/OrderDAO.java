package ro.simo.ChanosArtShop.Database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OrderDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void newOrder (int userId, Map<Integer, Integer> productsToQuantity) {
        jdbcTemplate.update("insert into orders values(null, ?,?,?)", userId, null, null);

        int orderId = jdbcTemplate.queryForObject("select max(id) from orders", Integer.class);

        for (Map.Entry<Integer, Integer> entry : productsToQuantity.entrySet()) {
            jdbcTemplate.update("insert into orders_product values(null, ?,?,?)", orderId, entry.getKey(), entry.getValue());
        }
    }

}
