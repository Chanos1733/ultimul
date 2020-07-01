package ro.simo.ChanosArtShop.Database;


import org.springframework.jdbc.core.RowMapper;
import ro.simo.ChanosArtShop.Database.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setCountry(resultSet.getString("country"));
        user.setCity(resultSet.getString("city"));
        user.setAdress(resultSet.getString("adress"));
        user.setPhone(resultSet.getString("phone"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));

        return user;
    }
}
