/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varausjarjestelma;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author qru19
 */
public class Asiakas {
    private Integer id;
    private String nimi;
    private String phone;
    private String email;
    private List<Varaus> varaukset;

    public Asiakas(Integer id, String nimi, String phone, String email) {
        this.id = id;
        this.nimi = nimi;
        this.phone = phone;
        this.email = email;
        this.varaukset = new ArrayList<>();
    }
    
    public Asiakas (String nimi, String phone, String email){
        this(null, nimi,phone,email);
    }
    
    public Asiakas(){  
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Varaus> getVaraukset() {
        return varaukset;
    }

    public void setVaraukset(List<Varaus> varaukset) {
        this.varaukset = varaukset;
    }
    
    @Override
    public String toString (){
        return this.nimi+", "+this.phone+", "+this.email;
    }
    
    
    
}
