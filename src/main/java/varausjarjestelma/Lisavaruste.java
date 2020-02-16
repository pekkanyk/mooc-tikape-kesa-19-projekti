/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varausjarjestelma;

/**
 *
 * @author qru19
 */
public class Lisavaruste {
    private Integer id;
    private Varaus varaus;
    private String varuste;

    public Lisavaruste(Integer id, Varaus varaus, String varuste) {
        this.id = id;
        this.varaus = varaus;
        this.varuste = varuste;
    }
    
    public Lisavaruste(Varaus varaus, String varuste){
        this(null,varaus,varuste);
    }
    public Lisavaruste (){
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVaruste() {
        return varuste;
    }

    public void setVaruste(String varuste) {
        this.varuste = varuste;
    }

    public Varaus getVaraus() {
        return varaus;
    }

    public void setVaraus(Varaus varaus) {
        this.varaus = varaus;
    }
    
}
