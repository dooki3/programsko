# Utjecaj neotkrivenih neispravnosti na razvoj programskog proizvoda
##Svrha projekta
 Suština našeg projekta bila je razvoj aplikacije koja na osnovu danog dataseta - koji sadrži metrike programskog koda fileova - procijenjuje utjecaj neotkrivenih neispravnosti na sam razvoj programskog proizvoda.
 Cilj je bio istrenirati algoritam da pomoću metrike bugcnt - koja sadrži broj neispravnosti u kodu tog filea - u odnosu na druge metrike "nauči" procijeniti dali će se u sljedećoj verziji tog filea pojaviti greška. Zatim taj istrenirani algoritam primjeniti na originalne datasetove i pruned datasetove koje smo dobili čisteći već postojeće datasetove od fileova po sljedećim kriterijima:
  * ukoliko file ima grešku stavljamo ga u očišćeni dataset
  * ukoliko file nema grešku te se njegove metrike podudaraju sa metrikama iz idućeg dataseta (file nije mijenjan) stavljamo ga u očišćeni dataset
  * ukoliko file nema greške te se njegove metrike ne podudaraju sa metrikama iz idućeg dataseta (file je izmijenjen) ne stavljamo ga u očišćeni dataset

Nakon dobivenih očišćenih datasetova trebalo je izvršiti algoritam na originalnim (neočišćenim datasetovima) i očišćenim datasetovima te usporediti rezultate. Rezultati nam pokazuju točnost algoritma u procijeni pojave grešaka na očišćenom datasetu i na originalnom datasetu te iz njih možemo zaključiti jeli isplativo čistiti datasetove ili ne.

