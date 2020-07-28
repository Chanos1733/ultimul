package ro.simo.ChanosArtShop.Database;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet resultSet, int i) throws SQLException {

        Comment comment = new Comment();
        comment.setId(resultSet.getInt("id"));
        comment.setId_product(resultSet.getInt("id_product"));
        comment.setId_user(resultSet.getInt("id_user"));
        comment.setDate(resultSet.getString("date"));
        comment.setComment(resultSet.getString("comment"));
        comment.setUser_name(resultSet.getString("user_name"));

        return comment;
    }



}
