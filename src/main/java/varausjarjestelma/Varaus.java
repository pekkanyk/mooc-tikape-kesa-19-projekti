/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varausjarjestelma;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author qru19
 */
public class Varaus {
    private Integer id;
    private LocalDateTime alku;
    private LocalDateTime loppu;
    private Integer yhteishinta;
    private Asiakas asiakas;
    private List<Lisavaruste> lisavarusteet;
    private List<HuoneVaraus> huonevaraukset;

    public Varaus(Integer id, Asiakas asiakas,LocalDateTime alku, LocalDateTime loppu, Integer yhteishinta) {
        this.id = id;
        this.asiakas = asiakas;
        this.alku = alku;
        this.loppu = loppu;
        this.yhteishinta = yhteishinta;
        this.lisavarusteet = new ArrayList<>();
        this.huonevaraukset = new ArrayList<>();
    }
    
    public Varaus(Asiakas asiakas, LocalDateTime alku, LocalDateTime loppu, Integer yhteishinta){
        this (null, asiakas, alku, loppu, yhteishinta);
    }
    
    public Varaus(){
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getAlku() {
        return alku;
    }

    public void setAlku(LocalDateTime alku) {
        this.alku = alku;
    }

    public LocalDateTime getLoppu() {
        return loppu;
    }

    public void setLoppu(LocalDateTime loppu) {
        this.loppu = loppu;
    }

    public Integer getYhteishinta() {
        return yhteishinta;
    }

    public void setYhteishinta(Integer yhteishinta) {
        this.yhteishinta = yhteishinta;
    }

    public List<Lisavaruste> getLisavarusteet() {
        return lisavarusteet;
    }

    public void setLisavarusteet(List<Lisavaruste> lisavarusteet) {
        this.lisavarusteet = lisavarusteet;
    }

    public Asiakas getAsiakas() {
        return asiakas;
    }

    public void setAsiakas(Asiakas asiakas) {
        this.asiakas = asiakas;
    }

    public List<HuoneVaraus> getHuonevaraukset() {
        return huonevaraukset;
    }

    public void setHuonevaraukset(List<HuoneVaraus> huonevaraukset) {
        this.huonevaraukset = huonevaraukset;
    }
    
    public void lisaaVaruste(Lisavaruste lisavaruste){
        this.lisavarusteet.add(lisavaruste);
    }
    
    public void lisaaHuoneVaraus(HuoneVaraus huonevaraus){
        this.huonevaraukset.add(huonevaraus);
    }
    
    @Override
    public String toString(){
        return this.asiakas.toString()+", "+this.alku+" - "+this.loppu;
    }
}
