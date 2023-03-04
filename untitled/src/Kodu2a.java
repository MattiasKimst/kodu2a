import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Kodu2a {

	// Kolm massiivi, mis täidetakse failist loetud andmetega
	static String[] kuupäev;
	static String[] kellaaeg;
	static double[] temperatuur;


	 public static void loeAndmed(String failitee) {
	 	//loeb andmed failist nimega "failitee"
		try {
			// Loeme failist kõik read, eeldame et faili kodeering on UTF-8
			List<String> read = Files.readAllLines(Path.of(failitee), StandardCharsets.UTF_8);

			// Määrame massiivide pikkuse
			kuupäev = new String[read.size()];
			kellaaeg = new String[read.size()];
			temperatuur = new double[read.size()];
			for (int i = 0; i < read.size(); i++) {
				// Hakime read tühikute põhjal
				String[] jupid = read.get(i).split(" ");
				// Määrame hakitud jupid vastavatesse massiividesse
				kuupäev[i] = jupid[0];
				kellaaeg[i] = jupid[1];
				temperatuur[i] = Double.parseDouble(jupid[2]);
			}
		} catch (IOException e) {
			// Faili ei leitud või lugemisel esines viga - viskame erindi ja lõpetame töö
			throw new RuntimeException("Faili " + failitee + " lugemisel tekkis viga", e);
		}
	}//loeAndmed

	/**
	 * Näidismeetod (mitte kasutamiseks): leiab antud ajal mõõdetud temperatuuri.
	 *
	 * @param päev Kuupäev kujul aaaa-kk-pp.
	 * @param kell Kellaaeg kujul tt:mm:ss.
	 * @return Temperatuur kuupäeval <b>päev</b> ajal <b>kell</b>, NaN kui andmetest puudu.
	 */
	static double temperatuurValitudPäevalJaKellal(String päev, String kell) {
		for (int i = 0; i < temperatuur.length; i++) {
			if (kuupäev[i].equals(päev) && kellaaeg[i].equals(kell))
				return temperatuur[i];
		}
		return Double.NaN;
	}

	public static void main(String[] args) {
		// Näidis
		// Failitee jooksvas kaustas. IntelliJ puhul otsitakse faili eelkõige PROJEKTI kaustast, mitte SRC kaustast
	    // Kui fail asetsetb src kaustas siis peaks failitee olema "src/ilmAegTemp_2022.txt"
		loeAndmed("ilmAegTemp_2022.txt");//loeb andmed etteantud failist
		System.out.println("Kodutöö nr 2a");
		System.out.println();

		System.out.println("Kontrolliks, massiivide algused:");
		System.out.printf("%s\t%s\t %s%n", "kuupäev[]", "kellaaeg[]", "temperatuur[]");
		for (int i = 0; i < 10; i++)
			System.out.printf("%s\t%s\t%.11f%n", kuupäev[i], kellaaeg[i], temperatuur[i]);

		System.out.println("   ...   \t".repeat(3) + "\n");

		String sünnipäev = "2022-08-29";
		String kell = "12:00:00";
		double temp = temperatuurValitudPäevalJaKellal(sünnipäev, kell);

		System.out.printf("Minu sünnipäeval aastal 2022 (%s) oli keskpäevane temperatuur: ", sünnipäev);
		if (Double.isNaN(temp))
			System.out.printf("- %nAntud aega: %s %s ei ole andmestikus!%n%n", sünnipäev, kell);
		else
			System.out.printf("%.1f kraadi.%n%n", temp);

		System.out.println(aastaKesk());
		System.out.println(Arrays.toString(aastaMinMax()));
		System.out.println(Arrays.toString(pikimKasvavKahanev()));
		System.out.println(Arrays.toString(kuudeKeskmised()));

	}

	// Õpilase poolt teostatavad meetodid:

	/**
	 * Meetod, mis for tsükliga käib temperatuuride massiivi läbi, liidab iga temperatuuri muutujale summa ja keskmise
	 * leidmiseks hiljem jagab summa temperatuuride arvuga
	 * @return aasta keskmine temperatuur
	 */
	public static double aastaKesk() {

		double summa=0; //muutuja kõigi temperatuuride summa jaoks
		int temperatuuride_arv=temperatuur.length;
		for (int i = 0; i < temperatuuride_arv; i++) {
			summa+=temperatuur[i];
		}
		return summa/temperatuuride_arv;


	}

	/**
	 * Meetod, mis käib for tsükliga kogu temperatuuride massiivi läbi, iga massiivi elementi võrdleb muutuja
	 * vähim või muutuja suurim väärtusega, kui vastavalt element on väiksem muutuja vähim väärtusest, siis saab muutuja
	 * vähim uueks väärtuseks see element. Suurim element leitakse sama tsükli sees analoogselt.
	 * @return kahekohaline järjend, esimene element aasta vähim ja teine aasta suurim temperatuur
	 */

	public static double[] aastaMinMax() {
		double minMaxJärjend[];
		minMaxJärjend=new double[2];
		double vähim = 50; // vaikimisi on muutujate suurim ja vähim väärtuseks ebareaalsed arvud, millega igal sammul
		double suurim = -50;
		for (int i = 0; i < temperatuur.length; i++) {
			if (temperatuur[i] > suurim) {
				suurim = temperatuur[i];
			}
			if (temperatuur[i] < vähim) {
				vähim = temperatuur[i];
			}
		}
		minMaxJärjend[0]=vähim;
		minMaxJärjend[1]=suurim;
		return minMaxJärjend;
		}
		// Tagastada kaheelemendiline järjend kus
		// 0. kohal on aasta miinimumtemperatuur
		// 1. kohal on aasta maksimumtemperatuur
		// Tagastuse näide: [-26.34938475, 32.564546]


	/**
	 * Meetod, mis for tsükliga käib kõik temperatuurid läbi, võrdleb igat temperatuuri talle eelnevaga, kasvava jada
	 * leidmiseks, kui temperatuur on eelmisest madalam, siis suurendab muutuja kasvavArv väärtust ühe võrra, vastasel
	 * juhul lõpetab jada ära, kui jada on senisest kõige pikemast (kahanevArv on suurem suurimKahanev väärtusest) jadast
	 * pikem, saab selle jada algus ja lõppajahetk muutujate kasvasvAlgus ja kasvavLõpp väärtusteks, neist moodustatakse
	 * kaheelemendiline massiiv. Kahaneva perioodi leidmine on analoogne.
	 * Lõpus võrreldakse suurima kasvava jada (muutuja suurimKasvav) ja suurima kahaneva jada
	 * (muutuja suurimKahanev) väärtusi, olenevalt sellest, meetod tagastab neist suurema jada ehk ajaperioodi
	 * algus- ja lõpphetke
	 * @return tagastab kaheelemendilise massiivi, mille esimene element on pikima temp kasvamise/kahenemis alguse kuupäev
	 * ja kellaaeg, teine element perioodi lõpu kuupäev ja kellaaeg
	 */
	public static String[] pikimKasvavKahanev() {
		double eelmine=temperatuur[0];
		int suurimKasvav=0;
		int kasvavArv=0;
		String kasvavVahemik[];
		kasvavVahemik = new String[2];
		String kasvavAlgus = (kuupäev[0] + " " + kellaaeg[0]);
		String kasvavLõpp =(kuupäev[0] + " " + kellaaeg[0]);
		for (int i = 1; i < temperatuur.length; i++) {
			if (temperatuur[i] >= eelmine) {
				kasvavArv++;
				kasvavLõpp = (kuupäev[i] + " " + kellaaeg[i]);
				eelmine=temperatuur[i];
				if (suurimKasvav < kasvavArv) {
					suurimKasvav = kasvavArv;
					kasvavVahemik[0] = kasvavAlgus;
					kasvavVahemik[1] = kasvavLõpp;
				}
			} else {
				kasvavAlgus = (kuupäev[i] + " " + kellaaeg[i]);
				kasvavArv=0;
				eelmine=temperatuur[i];
			}
		}

		int kahanevArv=0;
		double suurimKahanev=0;
		String kahanevVahemik[];
		kahanevVahemik = new String[2];
		eelmine=temperatuur[0];
		String kahanevAlgus = (kuupäev[0] + " " + kellaaeg[0]);
		String kahanevLõpp = (kuupäev[0] + " " + kellaaeg[0]);
		for (int i = 1; i < temperatuur.length; i++) {
			if (temperatuur[i] < eelmine) {
				kahanevArv++;
				kahanevLõpp = (kuupäev[i] + " " + kellaaeg[i]);
				eelmine=temperatuur[i];
				if (suurimKahanev < kahanevArv) {
					suurimKahanev = kahanevArv;
					kahanevVahemik[0] = kahanevAlgus;
					kahanevVahemik[1] = kahanevLõpp;
				}
			} else {
				kahanevAlgus = (kuupäev[i] + " " + kellaaeg[i]);
				kahanevArv=0;
				eelmine=temperatuur[i];
			}
		}
		if (suurimKasvav>suurimKahanev){
			return kasvavVahemik;
		}
		else {
			return kahanevVahemik;
		}
		// Tagastada pikima sellise perioodi algus ja lõpp (kaasa arvatud)
		// kus temperatuur ainult kasvab või ainult kahaneb kaheelemendilise massiivi kujul kus
		// 0. kohal on perioodi alguse kuupäev ja aeg, eraldatud tühikuga
		// 1. kohal on perioodi lõpu kuupäev ja aeg, eraldatud tühikuga
		// Tagastuse näide: ["2022-08-02 00:15:00", "2022-08-02 10:35:00"]
	}

	/**
	 * Meetod leiab iga kuu keskmise temperatuuri, kasutades while tsüklit, mille jätkamistingimus on, et kuu number
	 * massiivi kuupäev vastava j-indas elemendis vastab kuu numbrile, mille annab for tsükkel (vahemikus 1-12)
	 *
	 * @return tagastab massiivi, milles iga kuu jrk numbrile vastaval kohal on vastava kuu keskmine temp.
	 */
	public static double[] kuudeKeskmised() {
		double kuudeKeskmisteJärjend[];
		kuudeKeskmisteJärjend = new double[12];
		int j=0;
		for (int i = 1; i <=12; i++) { //annab ette kuunumbrid
			double kuuSumma=0;
			int kuuPikkus=0;

			while (Integer.parseInt(kuupäev[j].substring(5,7))==i){
				kuuSumma+=temperatuur[j];
				kuuPikkus++;
				if (j==temperatuur.length-1) { // kui failiread on otsas, siis lõpetab töö
					break;
				}
				j++;
			}
			kuudeKeskmisteJärjend[i-1]=kuuSumma/kuuPikkus; 

		}
		return kuudeKeskmisteJärjend;
		// Tagastada 12-elemendiline järjend kus
		// 0. kohal on jaanuari keskmine temperatuur
		// 1. kohal on veebruari keskmine temperatuur
		// ...
		// Tagastuse näide: [-3.23534509, ..., 1.4567456]

	}
}


