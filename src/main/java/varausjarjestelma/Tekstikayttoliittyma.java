package varausjarjestelma;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Tekstikayttoliittyma {
    @Autowired
    AsiakasDao asiakasDao;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    HuoneDao huoneDao;
    @Autowired
    LisavarusteDao lisavarusteDao;
    @Autowired
    HakumoottoriDao hakumoottoriDao;
    @Autowired
    HuoneVarausDao huoneVarausDao;
    @Autowired
    VarausDao varausDao;

    public void kaynnista(Scanner lukija) throws SQLException {
        while (true) {
            System.out.println("Komennot: ");
            System.out.println(" x - lopeta");
            System.out.println(" 1 - lisaa huone");
            System.out.println(" 2 - listaa huoneet");
            System.out.println(" 3 - hae huoneita");
            System.out.println(" 4 - lisaa varaus");
            System.out.println(" 5 - listaa varaukset");
            System.out.println(" 6 - tilastoja");
            System.out.println(" ALUSTA - Alusta tietokanta (poistaa kaiken)");
            System.out.println("");

            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                break;
            }

            if (komento.equals("1")) {
                lisaaHuone(lukija);
            } else if (komento.equals("2")) {
                listaaHuoneet();
            } else if (komento.equals("3")) {
                haeHuoneita(lukija);
            } else if (komento.equals("4")) {
                lisaaVaraus(lukija);
            } else if (komento.equals("5")) {
                listaaVaraukset();
            } else if (komento.equals("6")) {
                tilastoja(lukija);
            } else if (komento.equals("ALUSTA")) {
                alustaTietokanta();
            } else if (komento.equals("TEST")) {
                testikoodi(lukija);
            }
        }
    }

    private void lisaaHuone(Scanner s) throws SQLException {
        System.out.println("Lisätään huone");
        System.out.println("");

        System.out.println("Minkä tyyppinen huone on?");
        String tyyppi = s.nextLine();
        System.out.println("Mikä huoneen numeroksi asetetaan?");
        int numero = Integer.valueOf(s.nextLine());
        System.out.println("Kuinka monta euroa huone maksaa yöltä?");
        int hinta = Integer.valueOf(s.nextLine());
        huoneDao.create(new Huone(tyyppi,numero,hinta));
    }

    private void listaaHuoneet() throws SQLException {
        System.out.println("Listataan huoneet");
        System.out.println("");
        List<Huone> huoneet = huoneDao.list();
        if(huoneet== null) System.out.println("Tietokannassa ei ole huoneita.");
        else {
            huoneet.forEach((huone) -> {
                System.out.println(huone.toString());
            }); 
        }
        
    }

    private void haeHuoneita(Scanner s) throws SQLException {
        System.out.println("Haetaan huoneita");
        System.out.println("");

        System.out.println("Milloin varaus alkaisi (yyyy-MM-dd)?");
        LocalDateTime alku = LocalDateTime.parse(s.nextLine() + " " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Milloin varaus loppuisi (yyyy-MM-dd)?");
        LocalDateTime loppu = LocalDateTime.parse(s.nextLine() + " " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Minkä tyyppinen huone? (tyhjä = ei rajausta)");
        String tyyppi = s.nextLine();
        System.out.println("Minkä hintainen korkeintaan? (tyhjä = ei rajausta)");
        String maksimihinta = s.nextLine();
        
        List vapaatHuoneet = hakumoottoriDao.vapaatHuoneet(alku, loppu, tyyppi, maksimihinta);
        if (vapaatHuoneet.isEmpty()) System.out.println("Ei vapaita huoneita.");
        else {
            System.out.println("Vapaat huoneet: ");
            vapaatHuoneet.forEach((huone) -> {
                System.out.println(huone);
            });
        }
    }
    
    private void lisaaVaraus(Scanner s) throws SQLException {
        System.out.println("Haetaan huoneita");
        System.out.println("");

        System.out.println("Milloin varaus alkaisi (yyyy-MM-dd)?");;
        LocalDateTime alku = LocalDateTime.parse(s.nextLine() + " " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Milloin varaus loppuisi (yyyy-MM-dd)?");
        LocalDateTime loppu = LocalDateTime.parse(s.nextLine() + " " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Minkä tyyppinen huone? (tyhjä = ei rajausta)");
        String tyyppi = s.nextLine();
        System.out.println("Minkä hintainen korkeintaan? (tyhjä = ei rajausta)");
        String maksimihinta = s.nextLine();

        List huoneet = hakumoottoriDao.vapaatHuoneet(alku,loppu,tyyppi,maksimihinta);
        // mikäli huoneita ei ole vapaana, ohjelma tulostaa seuraavan viestin
        // ja varauksen lisääminen loppuu
        if (huoneet.isEmpty()) System.out.println("Ei vapaita huoneita.");
        // muulloin, ohjelma kertoo vapaiden huoneiden lukumäärän. Tässä 
        // oletetaan että vapaita huoneita on 2.
        else {
            System.out.println("Huoneita vapaana: "+huoneet.size());
            System.out.println("");
        // tämän jälkeen kysytään varattavien huoneiden lukumäärää
        // luvuksi tulee hyväksyä vain sopiva luku, esimerkissä 3 ei esim
        // kävisi, sillä vapaita huoneita vain 2
            int huoneita = 0;
            while (true) {
            System.out.println("Montako huonetta varataan?");
            huoneita = Integer.valueOf(s.nextLine());
            if (huoneita >= 1 && huoneita <= huoneet.size()) {
                break;
            }

            System.out.println("Epäkelpo huoneiden lukumäärä.");
            }
            
            // tämän jälkeen kysytään lisävarusteet
            List<String> lisavarusteet = new ArrayList<>();
             while (true) {
                System.out.println("Syötä lisävaruste, tyhjä lopettaa");
                String lisavaruste = s.nextLine();
                if (lisavaruste.isEmpty()) {
                    break;
                }
                lisavarusteet.add(lisavaruste);
            }
            
             // ja lopuksi varaajan tiedot
            System.out.println("Syötä varaajan nimi:");
            String nimi = s.nextLine();
            System.out.println("Syötä varaajan puhelinnumero:");
            String puhelinnumero = s.nextLine();
            System.out.println("Syötä varaajan sähköpostiosoite:");
            String sahkoposti = s.nextLine();
            
            //Tietokantaan lisäys
            int yhteishinta = 0;
            int days = 1+(int)ChronoUnit.DAYS.between(alku, loppu);
            for (int i=0;i<huoneita;i++){
                yhteishinta=yhteishinta + (((Huone)huoneet.get(i)).getHinta()*days);
            }
            Asiakas asiakas = new Asiakas();
            int asiakasId= asiakasDao.asiakasId(sahkoposti);
            if (asiakasId==-1){
                asiakas.setNimi(nimi);
                asiakas.setPhone(puhelinnumero);
                asiakas.setEmail(sahkoposti);
            } else {
                asiakas = asiakasDao.read(asiakasId);
            }
            Varaus varaus = new Varaus(asiakas, alku, loppu, yhteishinta);
            List <HuoneVaraus> huonevaraukset = new ArrayList<>();
            for (int i=0;i<huoneita;i++){
                huonevaraukset.add(new HuoneVaraus((Huone) huoneet.get(i),varaus));
            }
            List <Lisavaruste> varusteet = new ArrayList<>();
            lisavarusteet.forEach((lisavaruste) -> varusteet.add(new Lisavaruste(varaus,lisavaruste)));
            
            //varsinaiset lisäykset
            asiakasDao.create(asiakas);
            varausDao.create(varaus);
            for (HuoneVaraus huonevaraus: huonevaraukset){
                huoneVarausDao.create(huonevaraus.getHuone(), huonevaraus.getVaraus());
            }
            for (Lisavaruste varuste: varusteet){
                lisavarusteDao.create(varuste);
            }
            
        }
     
    }

    private void listaaVaraukset() throws SQLException {
        System.out.println("Listataan varaukset");
        System.out.println("");
        
        //List <Varaus> varaukset = varausDao.list();
        for (Varaus varaus:varausDao.list()){
            int days = 1+(int)ChronoUnit.DAYS.between(varaus.getAlku(), varaus.getLoppu());
            String paivia = "";
            if (days == 1) paivia = days+" paiva";
            else paivia= days+" paivaa";
            
            int lisavarusteita=0;
            for (Lisavaruste lisavaruste: lisavarusteDao.list()){
                if (Objects.equals(lisavaruste.getVaraus().getId(), varaus.getId())) lisavarusteita++;
            }
            String varusteita="";
            if (lisavarusteita == 1) varusteita="1 lisävaruste";
            else varusteita= lisavarusteita+" lisävarustetta";
            
            ArrayList<Huone> huoneet=new ArrayList<>();
            for(HuoneVaraus huonevaraus:huoneVarausDao.list()){
                if (Objects.equals(huonevaraus.getVaraus().getId(), varaus.getId())) huoneet.add(huonevaraus.getHuone());
            }
            String huonekpl="";
            if (huoneet.size()==1) huonekpl= "1 huone.";
            else huonekpl=huoneet.size()+" huonetta.";
            
            System.out.println(varaus.getAsiakas().getNimi()+", "
                    + varaus.getAsiakas().getEmail()+", "
                    + varaus.getAlku().toLocalDate()+", "
                    + varaus.getLoppu().toLocalDate()+", "
                    + paivia+", "
                    + varusteita+", "
                    + huonekpl
                    + " Huoneet:");
            
            int yht =0;
            for (Huone huone:huoneet){
                System.out.println(huone);
                yht = yht+huone.getHinta()* days;
            }
            System.out.println("Yhteensä: "+yht+" euroa");
            System.out.println("");
            
        }
        
    }

    private void tilastoja(Scanner lukija) throws SQLException {
        System.out.println("Mitä tilastoja tulostetaan?");
        System.out.println("");

        // tilastoja pyydettäessä käyttäjältä kysytään tilasto
        System.out.println(" 1 - Suosituimmat lisävarusteet");
        System.out.println(" 2 - Parhaat asiakkaat");
        System.out.println(" 3 - Varausprosentti huoneittain");
        System.out.println(" 4 - Varausprosentti huonetyypeittäin");

        System.out.println("Syötä komento: ");
        int komento = Integer.valueOf(lukija.nextLine());

        if (komento == 1) {
            suosituimmatLisavarusteet();
        } else if (komento == 2) {
            parhaatAsiakkaat();
        } else if (komento == 3) {
            varausprosenttiHuoneittain(lukija);
        } else if (komento == 4) {
            varausprosenttiHuonetyypeittain(lukija);
        }
    }

    private void suosituimmatLisavarusteet() {
        System.out.println("Tulostetaan suosituimmat lisävarusteet");
        System.out.println("");
        for (String rivi:hakumoottoriDao.tilastoiLisavarusteet()){
            System.out.println(rivi);
        }
    }

    private void parhaatAsiakkaat() {
        System.out.println("Tulostetaan parhaat asiakkaat");
        System.out.println("");
        for (String rivi:hakumoottoriDao.tilastoiAsiakkaat()){
            System.out.println(rivi);
        }
    }
 
    private void varausprosenttiHuoneittain(Scanner lukija) throws SQLException{
        System.out.println("Tulostetaan varausprosentti huoneittain");
        System.out.println("");

        System.out.println("Mistä lähtien tarkastellaan?");
        LocalDateTime alku = LocalDateTime.parse(lukija.nextLine() + "-01 " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Mihin asti tarkastellaan?");
        LocalDateTime loppu = LocalDateTime.parse(lukija.nextLine() + "-01 " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        
        int daysInMo = 1+(int)ChronoUnit.DAYS.between(alku, loppu);
        //int varatut = 0;
        
        for (Huone huone:huoneDao.list()){
            
            int varatut = hakumoottoriDao.paiviaVarattu(alku, loppu, huone);
            Double varausprosentti = 10000*(1.0*varatut/daysInMo);
            varausprosentti = (double)Math.round(varausprosentti);
            varausprosentti = varausprosentti /100;
            
            System.out.println(huone+", "+varausprosentti+"%");
            
            
        }
    }

    private void varausprosenttiHuonetyypeittain(Scanner lukija) throws SQLException {
        System.out.println("Tulostetaan varausprosentti huonetyypeittäin");
        System.out.println("");

        System.out.println("Mistä lähtien tarkastellaan?");
        LocalDateTime alku = LocalDateTime.parse(lukija.nextLine() + "-01 " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Mihin asti tarkastellaan?");
        LocalDateTime loppu = LocalDateTime.parse(lukija.nextLine() + "-01 " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        
        Map<String, Integer> varatutpaivat = hakumoottoriDao.paiviaVarattuHuoneTyypeittain(alku, loppu);
        Map<String, Integer> tyyppienLkm = huoneDao.lukumaarat();
        int daysInMo = 1+(int)ChronoUnit.DAYS.between(alku, loppu);
        
        System.out.println(varatutpaivat);
        System.out.println(tyyppienLkm);
        
        
        varatutpaivat.entrySet().forEach((entry) -> {
            String tyyppi = entry.getKey();
            Integer lkm = tyyppienLkm.get(tyyppi);
            int paivat = daysInMo*lkm;
            int varatut = entry.getValue();
            Double varausprosentti = 10000*(1.0*varatut/paivat);
            varausprosentti = (double)Math.round(varausprosentti);
            varausprosentti = varausprosentti /100;
            System.out.println(tyyppi+", "+varausprosentti+"%");
        });

    }
    
    
    private static void alustaTietokanta() {
        // mikäli poistat vahingossa tietokannan voit ajaa tämän metodin jolloin 
        // tietokantataulu luodaan uudestaan

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
            conn.prepareStatement("DROP TABLE Asiakas IF EXISTS;").executeUpdate();
            conn.prepareStatement("DROP TABLE Huone IF EXISTS;").executeUpdate();
            conn.prepareStatement("DROP TABLE Varaus IF EXISTS;").executeUpdate();
            conn.prepareStatement("DROP TABLE HuoneVaraus IF EXISTS;").executeUpdate();
            conn.prepareStatement("DROP TABLE Lisavaruste IF EXISTS;").executeUpdate();
            
            conn.prepareStatement("CREATE TABLE Asiakas "
                    + "(id SERIAL, nimi VARCHAR(128), phone VARCHAR(16), email VARCHAR(64), "
                    + "PRIMARY KEY (id)"
                    + ");").executeUpdate();
            
            conn.prepareStatement("CREATE TABLE Huone "
                    + "(id SERIAL, tyyppi VARCHAR(32), numero INTEGER, hinta INTEGER, "
                    + "PRIMARY KEY (id)"
                    + ");").executeUpdate();
            
            conn.prepareStatement("CREATE TABLE Varaus "
                    + "(id SERIAL, asiakas_id INTEGER, alku TIMESTAMP, loppu TIMESTAMP, yhteishinta INTEGER, "
                    + "PRIMARY KEY (id), "
                    + "FOREIGN KEY (asiakas_id) REFERENCES Asiakas(id)"
                    + ");").executeUpdate();
            
            conn.prepareStatement("CREATE TABLE HuoneVaraus "
                    + "(huone_id INTEGER, varaus_id INTEGER, "
                    + "FOREIGN KEY (huone_id) REFERENCES Huone(id), "
                    + "FOREIGN KEY (varaus_id) REFERENCES Varaus(id)"
                    + ");").executeUpdate();
            
            conn.prepareStatement("CREATE TABLE Lisavaruste "
                    + "(id SERIAL, varaus_id INTEGER, lisavaruste VARCHAR(64), "
                    + "PRIMARY KEY (id),"
                    + "FOREIGN KEY (varaus_id) REFERENCES Varaus(id)"
                    + ");").executeUpdate();
            
            
            
            System.out.println("Tietokanta alustettu.");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    public void testikoodi(Scanner s) throws SQLException{
        
    }
}
