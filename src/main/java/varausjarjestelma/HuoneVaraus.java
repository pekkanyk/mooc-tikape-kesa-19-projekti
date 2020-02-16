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
public class HuoneVaraus {
    private Huone huone;
    private Varaus varaus;

    public HuoneVaraus(Huone huone, Varaus varaus) {
        this.huone = huone;
        this.varaus = varaus;
    }
    public HuoneVaraus(){
        
    }

    public Huone getHuone() {
        return huone;
    }

    public void setHuone(Huone huone) {
        this.huone = huone;
    }

    public Varaus getVaraus() {
        return varaus;
    }

    public void setVaraus(Varaus varaus) {
        this.varaus = varaus;
    }
    
    
    
}
