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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class HuoneDao implements Dao<Huone, Integer>{
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public Huone create(Huone huone) throws SQLException{
        /*
        jdbcTemplate.update("INSERT INTO Huone"
                + " (tyyppi, numero, hinta)"
                + " VALUES (?, ?, ?)",
                huone.getTyyppi(),
                huone.getNumero(),
                huone.getHinta());
        */
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Huone "
                + "(tyyppi, numero, hinta) "
                + "VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, huone.getTyyppi());
                stmt.setInt(2, huone.getNumero());
                stmt.setInt(3, huone.getHinta());
                return stmt;
        },keyHolder);
        huone.setId(keyHolder.getKey().intValue());
        return huone;
        
    }
    
    @Override
    public Huone read(Integer key) throws SQLException{
        Huone huone = jdbcTemplate.queryForObject(
                "SELECT * FROM Huone WHERE id = ?;",
                new BeanPropertyRowMapper<>(Huone.class),key);
        return huone;
    }
    
    @Override
    public List<Huone> list() throws SQLException{
        List<Huone> huoneet = new ArrayList<>();
        jdbcTemplate.query(
                "SELECT * FROM Huone ORDER BY hinta DESC;",
                (rs, rowNum)-> huoneet.add(new Huone(rs.getInt("id"), rs.getString("tyyppi"),
                rs.getInt("numero"), rs.getInt("hinta"))));
        if (huoneet.isEmpty()) return null;
        return huoneet;
    }
    
    public Map<String, Integer> lukumaarat() throws SQLException{
        Map<String, Integer> huoneet = new HashMap<>();
        jdbcTemplate.query(
                "SELECT tyyppi, COUNT(tyyppi) AS lkm FROM Huone "
                        + "GROUP BY tyyppi;",
                (rs, rowNum)-> huoneet.put(rs.getString("tyyppi"), rs.getInt("lkm"))  );
        if (huoneet.isEmpty()) return null;
        return huoneet;
    }
}
