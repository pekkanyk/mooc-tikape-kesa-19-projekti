/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varausjarjestelma;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author Pekka
 */
@Component
public class HakumoottoriDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public List<String> tilastoiLisavarusteet(){
        String SQL = "SELECT lisavaruste, count(lisavaruste) AS lukumaara FROM Lisavaruste "
                      + "GROUP BY lisavaruste "
                      + "ORDER BY lukumaara DESC "
                      + "LIMIT 10;";
                                List <String> rivit = jdbcTemplate.query(SQL,(ResultSet rs) -> {
                                    List<String> list = new ArrayList<>();
                                            while(rs.next()){
                                                String rivi = rs.getString("lisavaruste")+", "+rs.getInt("lukumaara");
                                                if (rs.getInt("lukumaara") == 1) rivi=rivi+" varaus";
                                                else rivi=rivi+" varausta";
                                                list.add(rivi);
                                            } return list; }                                           
                                    );
        return rivit;
    }
    
    public List<String> tilastoiAsiakkaat(){
        String SQL = "SELECT nimi, email, phone, SUM(yhteishinta) AS summa FROM Asiakas "
                      + "JOIN Varaus ON Varaus.asiakas_id = Asiakas.id "
                      + "GROUP BY email "
                      + "ORDER BY summa DESC "
                      + "LIMIT 10;";
                                List <String> rivit = jdbcTemplate.query(SQL,(ResultSet rs) -> {
                                    List<String> list = new ArrayList<>();
                                            while(rs.next()){
                                                String rivi = rs.getString("nimi")+", "+rs.getString("email")+", "+rs.getString("phone")+", "+rs.getInt("summa")+" euroa";
                                                list.add(rivi);
                                            } return list; }                                           
                                    );
        return rivit;
    }
     
    public List<Huone> vapaatHuoneet(LocalDateTime alku, LocalDateTime loppu, String tyyppi, String maksimihinta){
        if (tyyppi.equals("")) return vapaatHuoneet(alku,loppu,maksimihinta);
        else {
        
        int hinta = Integer.MAX_VALUE;
        if (!maksimihinta.equals("")) hinta= Integer.valueOf(maksimihinta);
        String innerSQL = "SELECT huone_id FROM HuoneVaraus "
                + "JOIN Varaus ON HuoneVaraus.varaus_id= Varaus.id "
                + "JOIN Huone ON HuoneVaraus.huone_id = Huone.id "
                + "WHERE (alku BETWEEN ? AND ? OR loppu BETWEEN ? AND ?)";
        String SQL = "SELECT * FROM Huone "
                + "WHERE id NOT IN ("+innerSQL+") AND hinta <= ? AND tyyppi = ? "
                + "ORDER BY hinta DESC;";
        List <Huone> rivit = jdbcTemplate.query(SQL,(ResultSet rs) -> {
                                    List<Huone> list = new ArrayList<>();
                                            while(rs.next()){
                                                Huone huone = new Huone();
                                                huone.setId(rs.getInt("id"));
                                                huone.setHinta(rs.getInt("hinta"));
                                                huone.setNumero(rs.getInt("numero"));
                                                huone.setTyyppi(rs.getString("tyyppi"));
                                                list.add(huone);
                                            } return list; },
                                            alku,loppu,alku,loppu, hinta, tyyppi
                                    );
        return rivit;
        }
    }
    public List<Huone> vapaatHuoneet(LocalDateTime alku, LocalDateTime loppu, String maksimihinta){
        int hinta = Integer.MAX_VALUE;
        if (!maksimihinta.equals("")) hinta= Integer.valueOf(maksimihinta);
        
        String innerSQL = "SELECT huone_id FROM HuoneVaraus "
                + "JOIN Varaus ON HuoneVaraus.varaus_id= Varaus.id "
                + "JOIN Huone ON HuoneVaraus.huone_id = Huone.id "
                + "WHERE (alku BETWEEN ? AND ? OR loppu BETWEEN ? AND ?)";
        String SQL = "SELECT * FROM Huone "
                + "WHERE id NOT IN ("+innerSQL+") AND hinta <= ? "
                + "ORDER BY hinta DESC;";
        List <Huone> rivit = jdbcTemplate.query(SQL,(ResultSet rs) -> {
                                    List<Huone> list = new ArrayList<>();
                                            while(rs.next()){
                                                Huone huone = new Huone();
                                                huone.setId(rs.getInt("id"));
                                                huone.setHinta(rs.getInt("hinta"));
                                                huone.setNumero(rs.getInt("numero"));
                                                huone.setTyyppi(rs.getString("tyyppi"));
                                                list.add(huone);
                                            } return list; },
                                            alku,loppu,alku,loppu,hinta
                                    );
        return rivit;
    }
    
    public Integer paiviaVarattu(LocalDateTime alku, LocalDateTime loppu, Huone huone){
        String SQL = "SELECT DATEDIFF(day, alku, loppu) AS paivia FROM HuoneVaraus "
                + "JOIN Varaus ON HuoneVaraus.varaus_id= Varaus.id "
                + "WHERE huone_id = ? AND (alku BETWEEN ? AND ? OR loppu BETWEEN ? AND ?);";
        
        List <Integer> rivit = jdbcTemplate.query(SQL,(ResultSet rs) -> {
                                    List<Integer> list = new ArrayList<>();
                                            while(rs.next()){
                                                Integer rivi = rs.getInt("paivia");
                                                list.add(rivi);
                                            } return list; },
                                            huone.getId(), alku, loppu, alku, loppu
                                    );
      
        if (!rivit.isEmpty()) return rivit.get(0);
        else return 0;
    }
    
    public Map<String,Integer> paiviaVarattuHuoneTyypeittain(LocalDateTime alku, LocalDateTime loppu){
        String SQL = "SELECT Huone.tyyppi, SUM (DATEDIFF(day, alku, loppu)) AS paivia FROM HuoneVaraus "
                + "JOIN Varaus ON HuoneVaraus.varaus_id= Varaus.id "
                + "JOIN Huone ON HuoneVaraus.huone_id = Huone.id "
                + "WHERE alku BETWEEN ? AND ? OR loppu BETWEEN ? AND ? "
                + "GROUP BY Huone.tyyppi;";
        
        Map <String,Integer> rivit = jdbcTemplate.query(SQL,(ResultSet rs) -> {
                                    Map<String,Integer> list = new HashMap<>();
                                            while(rs.next()){
                                                list.put(rs.getString("tyyppi"), rs.getInt("paivia"));
                                            } return list; },
                                            alku, loppu, alku, loppu
                                    );
        return rivit;
    }

        
}
