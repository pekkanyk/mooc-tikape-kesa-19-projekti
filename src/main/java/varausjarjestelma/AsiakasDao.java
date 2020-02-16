/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varausjarjestelma;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author qru19
 */
@Component
public class AsiakasDao implements Dao<Asiakas, Integer>{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    
    public Asiakas create(Asiakas asiakas) throws SQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Asiakas "
                + "(nimi, phone, email) "
                + "VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, asiakas.getNimi());
                stmt.setString(2, asiakas.getPhone());
                stmt.setString(3, asiakas.getEmail());
                return stmt;
        },keyHolder);
        asiakas.setId(keyHolder.getKey().intValue());
        return asiakas;
        
    }




    @Override
    public Asiakas read(Integer key) throws SQLException {
        List<Asiakas> asiakkaat = jdbcTemplate.query(
            "SELECT * FROM Asiakas WHERE id = ?",
            (rs, rowNum) -> new Asiakas(rs.getInt("id"), rs.getString("nimi"),
            rs.getString("phone"), rs.getString("email")),
            key);

        if(asiakkaat.isEmpty()) {
            return null;
        }

        return asiakkaat.get(0);
    }
    public int asiakasId(String email){
        List<Integer> asiakasIdt = jdbcTemplate.query(
            "SELECT id FROM Asiakas WHERE email = ?",
            (rs, rowNum) -> rs.getInt("id"),email);
        if(asiakasIdt.isEmpty()) {
            return -1;
        }
        return asiakasIdt.get(0);
    }

    @Override
    public List<Asiakas> list() throws SQLException {
       List<Asiakas> asiakkaat = new ArrayList<>();
        jdbcTemplate.query(
                "SELECT * FROM Asiakas;",
                (rs, rowNum)-> asiakkaat.add(new Asiakas(rs.getInt("id"), rs.getString("nimi"),
                rs.getString("phone"), rs.getString("email"))));
        if (asiakkaat.isEmpty()) return null;
        return asiakkaat;
    }
    
    
    
}
