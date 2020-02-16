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
 * @author Pekka
 */
@Component
public class HuoneVarausDao{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    HuoneDao huoneDao;
    @Autowired
    VarausDao varausDao;

    
    public void create(Huone huone, Varaus varaus) throws SQLException {
        jdbcTemplate.update("INSERT INTO HuoneVaraus"
                + " (huone_id, varaus_id)"
                + " VALUES (?, ?)",
                huone.getId(),
                varaus.getId());
    }
    
    public HuoneVaraus read(Huone huone, Varaus varaus) throws SQLException {
        HuoneVaraus huonevaraus = jdbcTemplate.queryForObject(
                "SELECT * FROM HuoneVaraus WHERE huone_id = ? AND varaus_id = ?",
                new BeanPropertyRowMapper<>(HuoneVaraus.class),huone,varaus);
        return huonevaraus;
    }

    public List<HuoneVaraus> list() throws SQLException {
        String SQL = "SELECT * FROM HuoneVaraus";
        List <HuoneVaraus> huonevaraukset = jdbcTemplate.query(SQL, (ResultSet rs) -> {
            List<HuoneVaraus> list = new ArrayList<>();
            while(rs.next()){
                HuoneVaraus huonevaraus = new HuoneVaraus();
                huonevaraus.setHuone(huoneDao.read(rs.getInt("huone_id")));
                huonevaraus.setVaraus(varausDao.read(rs.getInt("varaus_id")));
                list.add(huonevaraus);
            }
            return list;
        });
    return huonevaraukset;
    }
    
    
}
