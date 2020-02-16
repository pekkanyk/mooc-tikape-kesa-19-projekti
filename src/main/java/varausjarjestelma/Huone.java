/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varausjarjestelma;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author qru19
 */
public class Huone {
    private Integer id;
    private String tyyppi;
    private Integer numero;
    private Integer hinta;
    private List<HuoneVaraus> huonevaraukset;
    
    public Huone(Integer id, String tyyppi, Integer numero, Integer hinta){
        this.id = id;
        this.tyyppi = tyyppi;
        this.numero = numero;
        this.hinta = hinta;
        this.huonevaraukset = new ArrayList<>();
    }
    
    public Huone(String tyyppi, Integer numero, Integer hinta){
        this (null,tyyppi, numero, hinta);
    }
    public Huone(){
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTyyppi() {
        return tyyppi;
    }

    public void setTyyppi(String tyyppi) {
        this.tyyppi = tyyppi;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getHinta() {
        return hinta;
    }

    public void setHinta(Integer hinta) {
        this.hinta = hinta;
    }

    public List<HuoneVaraus> getHuonevaraukset() {
        return huonevaraukset;
    }

    public void setHuonevaraukset(List<HuoneVaraus> huonevaraukset) {
        this.huonevaraukset = huonevaraukset;
    }
    
    @Override
    public String toString(){
        return this.tyyppi+", "+this.numero+", "+this.hinta+" euroa";
    }
    @Override
    public boolean equals(Object verrattava){
        if (this==verrattava) return true;
        if (!(verrattava instanceof Huone)) return false;
        Huone verrattavaHuone = (Huone) verrattava;
        return Objects.equals(this.id, verrattavaHuone.id);
    }
}
