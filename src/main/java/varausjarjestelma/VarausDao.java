/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varausjarjestelma;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
public class VarausDao implements Dao<Varaus, Integer>{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    AsiakasDao asiakasDao;
    
    @Override
    public Varaus create(Varaus varaus) throws SQLException{
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Varaus "
                + "(asiakas_id, alku, loppu, yhteishinta) "
                + "VALUES (?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, varaus.getAsiakas().getId());
                stmt.setTimestamp(2, Timestamp.valueOf(varaus.getAlku()));
                stmt.setTimestamp(3, Timestamp.valueOf(varaus.getLoppu()));
                stmt.setInt(4, varaus.getYhteishinta());
                return stmt;
        },keyHolder);
        varaus.setId(keyHolder.getKey().intValue());
        return varaus;
    }
    
    @Override
    public Varaus read(Integer key) throws SQLException{
        Varaus varaus = jdbcTemplate.queryForObject(
                "SELECT * FROM Varaus WHERE id = ?;",
                new BeanPropertyRowMapper<>(Varaus.class),key);
        return varaus;
    }
    
    @Override
    public List<Varaus> list() throws SQLException{
        
        String SQL = "SELECT * FROM Varaus ORDER BY alku";
        List <Varaus> varaukset = jdbcTemplate.query(SQL, (ResultSet rs) -> {
            List<Varaus> list = new ArrayList<>();
            while(rs.next()){
                Varaus varaus = new Varaus();
                varaus.setId(rs.getInt("id"));
                varaus.setAsiakas(asiakasDao.read(rs.getInt("asiakas_id")));
                varaus.setAlku(rs.getTimestamp("alku").toLocalDateTime() );
                varaus.setLoppu(rs.getTimestamp("loppu").toLocalDateTime());
                list.add(varaus);
            }
            return list;
        });
    return varaukset;
    }
    
}
