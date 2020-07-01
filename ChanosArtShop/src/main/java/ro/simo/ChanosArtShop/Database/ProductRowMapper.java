package ro.simo.ChanosArtShop.Database;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getDouble("price"));
        product.setMaterials(resultSet.getString("materials"));
        product.setDimensions(resultSet.getString("dimensions"));
        product.setPhoto1(resultSet.getString("photo1"));
        product.setPhoto2(resultSet.getString("photo2"));
        product.setPhoto3(resultSet.getString("photo3"));

        return product;
    }
}
