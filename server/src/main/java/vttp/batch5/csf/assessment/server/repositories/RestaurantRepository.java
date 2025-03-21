package vttp.batch5.csf.assessment.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    private final static String SQL_VALID = """
            SELECT * FROM customers WHERE username = ? AND password = sha2(?,224)
            """;

    private final static String SQL_INSERT_ORDER = """
            INSERT INTO place_orders(order_id,payment_id,order_date,total,username) VALUES (?,?,?,?,?)
            """;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean validUser(String username, String password) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_VALID, username,password);
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public void insertData(String order_id,String payment_id,String order_date,float total, String username){
        jdbcTemplate.update(SQL_INSERT_ORDER, order_id,payment_id,order_date,total,username);
    }
}
