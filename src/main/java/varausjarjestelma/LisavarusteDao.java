/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varausjarjestelma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author qru19
 */
@Component
public class LisavarusteDao implements Dao<Lisavaruste, Integer>{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    VarausDao varausDao;
    
    @Override
    public Lisavaruste create(Lisavaruste lisavaruste) throws SQLException {
        jdbcTemplate.update("INSERT INTO Lisavaruste"
                + " (varaus_id, lisavaruste)"
                + " VALUES (?, ?)",
                lisavaruste.getVaraus().getId(),
                lisavaruste.getVaruste());
          return null;      
    }

    @Override
    public Lisavaruste read(Integer key) throws SQLException {
        Lisavaruste lisavaruste = jdbcTemplate.queryForObject(
                "SELECT * FROM Lisavaruste WHERE id = ?;",
                new BeanPropertyRowMapper<>(Lisavaruste.class),key);
        return lisavaruste;
    }

    @Override
    public List<Lisavaruste> list() throws SQLException {
        String SQL = "SELECT * FROM Lisavaruste";
        List <Lisavaruste> lisavarusteet = jdbcTemplate.query(SQL, (ResultSet rs) -> {
            List<Lisavaruste> list = new ArrayList<>();
            while(rs.next()){
                Lisavaruste lisavaruste = new Lisavaruste();
                lisavaruste.setId(rs.getInt("id"));
                lisavaruste.setVaraus(varausDao.read(rs.getInt("varaus_id")));
                lisavaruste.setVaruste(rs.getString("lisavaruste"));
                list.add(lisavaruste);
            }
            return list;
        });
    return lisavarusteet;
    }
    
}
