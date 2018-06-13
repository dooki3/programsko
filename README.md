# Utjecaj neotkrivenih neispravnosti na razvoj programskog proizvoda
## Svrha projekta
 Suština našeg projekta bila je razvoj aplikacije koja na osnovu danog dataseta - koji sadrži metrike programskog koda fileova - procijenjuje utjecaj neotkrivenih neispravnosti na sam razvoj programskog proizvoda.
 Cilj je bio istrenirati algoritam da pomoću metrike "bug_cnt" - koja sadrži broj neispravnosti u kodu tog filea - u odnosu na druge metrike "nauči" procijeniti dali će se u sljedećoj verziji tog filea pojaviti greška. Zatim taj istrenirani algoritam primjeniti na originalne datasetove i pruned datasetove koje smo dobili čisteći već postojeće datasetove od fileova po sljedećim kriterijima:
  * ukoliko file ima grešku stavljamo ga u očišćeni dataset
  * ukoliko file nema grešku te se njegove metrike podudaraju sa metrikama iz idućeg dataseta (file nije mijenjan) stavljamo ga u očišćeni dataset
  * ukoliko file nema greške te se njegove metrike ne podudaraju sa metrikama iz idućeg dataseta (file je izmijenjen) ne stavljamo ga u očišćeni dataset

Nakon dobivenih očišćenih datasetova trebalo je izvršiti algoritam na originalnim (neočišćenim datasetovima) i očišćenim datasetovima te usporediti rezultate. Rezultati nam pokazuju točnost algoritma u procijeni pojave grešaka na očišćenom datasetu i na originalnom datasetu te iz njih možemo zaključiti jeli isplativo čistiti datasetove ili ne.



## Pokretanje  
Da bi se aplikacija pokrenula, potrebno je samo pokrenuti aplikaciju iz foldera pritiskom na "BugImpactAnalysis.exe"
koja će biti priložena u zip-u.

Alternativno, moguće je pokrenuti kod iz željenog IDE-a ukoliko prva opcija ne radi.  

#### Korištenje aplikacije:

* Prvo se učitaju datoteke pritiskom na ikonu u gornjem lijevom kutu.
* Odabrane datoteke pojavljuju se u prozoričiću sa checkboxovima
* Odabrati dvije datoteke koje želite provesti kroz algoritam. 
(Datoteke se biraju redoslijedom odozgo prema dolje, te program uzme samo prve dvije koje naiđe)
* Pokrenuti evaluaciju pritiskom na svijetloplavi gumb u obliku strelice
* Na ekranu će se prikazati rezultati, prvi se odnosi na rezultat dataseta koji smo očistili,  
a drugi na originalni dataset koji smo učitali.

#### Dodatne opcije:

* Brisanje datoteka radi se odabirom datoteke iz padajućeg izbornika i pritiskom na gumb "Remove File"
* Ukoliko želite mijenjati redoslijed datoteka u svrhu usporedbe starijih verzija, jednostavno ih pobrišite, i učitajte 
jednu po jednu, željenim redoslijedom.
* Za testiranje rada aplikacije, priložene su datoteke u .csv formatu na kojima je izrađena ova aplikacija