# Oslo-T-Bane


# Overview
**English:** 
This Java program is designed to find the shortest path between two stations on the Oslo metro network using Dijkstra's algorithm. The data for the tunnels and stations have been created by me and are based on the Oslo metro timetable. The user first selects the line on which the departure station is located, and then the specific station. Next, the user chooses the line where the destination station is located, followed by the specific end station. The program then outputs the fastest route from the departure station to the destination station, based on the data in the files Tunnels.tsv and Stations.tsv.

**Norwegian:** 
Dette java programmet skal finne den korteste vei mellom to stasjoner på Oslo t-bane nettverk med hjelp av dijkstra algoritmen. Dataen for tunnelene og stasjoene er laget av meg og baserer seg på Oslo t-bane tabel. Brukeren velger først linjen avgangsstasjonen ligger på, deretter selve stasjonen. Så velger brukeren linjen destinasjonsstasjonen ligger på også selve slutt stasjonen. Programmet skriver ut den raskeste ruten fra avgangsstasjonen til destinasjonsstasjonen, basert på dataene i filene Tunnels.tsv og Stations.tsv


# Files :
**Tunnels.tsv:**
Tunnels.tsv består av alle tunnelene i Oslos t-banesystem. Hver tunnel har en unik id det første 2 sifrene sier hvilke line 
tunnelen tilhører. Hvis de to første sifrene er like vil det si at tunnelen går mellom to stasjoner på den samme linjen. Om
to do sifrene er anderledes betyr det at tunnelen er en overgangs tunnel. En overgangstunnel repesentere egentlig et linje bytte fra en linje til en annen. Etter de to sifrene står det TN som betyr at det er en tunnel. De siste fire sifrene 
sier hvilke to stasjoner som tunnelen med iden er imellom. Man ser på de 4 siste sifrene som par hvor hvert par er 
er stasjonene tunnelen er imellom. Det første av sifrene de to første sifrene tilhører det første paret, mens det andre sifret av de to første sidrene tilhører det andre paret i de 4 siste sifrene. Vet å se på denne kombinasjonen kan man se hvilke linje en stasjon tilhører. Dette er spesielt nyttig ved et linjebytte hvor man lett kan se hvilke stasjon man bytter til. Etter selve tunnel iden kommer det tileggsinfomasjon som hva stasjoene heter, hvor lang tid det tar å reise gjennom denne tunnelen og om det eventuelt er en overgangstunnel.

- Eksempel (12TN1811 Majorstuen-Majorstuen 5 TRANSITION "TUNNEL"): Ved å se på de første 2 sifrene kan vi se at dette er en tunnel som går mellom linje 1 og 2. Videre står det "TN" og vi kan være sikre på at dette er en tunnel id. Videre har vi 18 også 11. Dette betyr at vi har en tunnel som går fra linje 1 stasjon 18 til linje 2 stasjon 11. Videre ser vi at det står "Majorstuen-Majorstuen" så vi vet at begge stasjonen på linjen er  majorstuen stoppe på hver sin linje. Dette er tydlig et overgangstunnel som vi får bekreftet ved at det står "TRANSITION "TUNNEL"" på slutten 

**Stations.tsv:**
Stations.tsv består av alle Stasjonene i Oslos t-banesystem. Hver stasjon har en unik id hvor det første sifere sier hvilke line stoppe befinner seg på. En stasjon kan befinne seg på flere linjer samtidig som betyr at flere tbane linjer kjører på samme spor. Den samme stasjonen kan derfor ha ulikt stasjon id for hver linje. De siste to sifrene i iden sier hvor mange andre stasjoner det har vært før den på linjen. Videre så kommer navnet på stasjonen også en liste av tunnel idene til alle tunnelene den stasjonen er tilkoblet.

- Example (1STN18 Majorstuen 11TN1718 11TN1819 12TN1811 13TN1816 14TN1821 15TN1810): Dette er et eksemepl på en statsjon på line 1. Det første vi ser er 1STJ18 som indikerer at denne stasjon er på linje 1 og er på stopp 18. Videre ser vi navet på stasjonen som er Majorstuen. Etter Majorstuen kommer det mange tunnel ider og er de tunnelene man kan bruke fra den oppgitte stasjonen. Alle stasjonene skal skal minsta ha to tunner som brukes på linjen med untak av ende stasjoene som bare har en tunnel ut fra seg. Som nevt tidligere 
kan man se at mange av tunnel idene to første sifrere er ulike. I dette tilfellet her så kan man bytte fra Masjorstua på linje 1 stasjon 18 til linje 2 stasjon 11, 3 stasjon 16, 4 stasjon 21 og 5 stasjon 10. Disse er egentlig ikke tunneller, men signalisere at man bytter fra en linje til en annen. 


# Classes
Station, Tunnel og TBane klassen danner grunnmuren for hele programmet og lagrer informasjoner vi trenger.

- Stastion klassen holder på informasjon for den spesefikke klassen. En stasjon har en id, navn, dist og en liste av tunnel ider. Dist variabelen blir spesefikt brukt i dijkstra() funksjonen hvor det er den minste distansen mellom den spesfike noden til start noden. Dette er for å sammenligne forskjellige node for å finne den den korteste veien fra start. Listen av tunnel ider er idene til alle tunnelene som er koblet og kan brukes fra en stasjon. Metodene til stasjon klassen er hovedskakelig for å sette verdier eller hente verdier. 

- En tunnel klassen har en id, navn, reisetid, om det er en overgangstunnel og en liste av stasjoner som er tilkoblet til denne tunnellen. Metodene i tunnel klassen er hovedskakelig for å sette verdier eller hente verdier. 

- TBane klassen er hvor alt blir satt sammen og kjøres. Tbane klassen har 3 hashmaps hvor alt av informasjon blir sentalisert og kan hentes fra tbane klassen. Det er et hashmap for stasjoner hvor nøkkelen er stasjons iden i form av en string og verdien er den tilsvarende stasjon klassen. Det andre hasmapet er for tunneler hvor nøkkelen er tunnel iden i form av en string og verdien er den tilsvarende tunnel klassen. Det siste hashmapet danner en graf og er abstahert model av oslo tbane. Nøkklene i graf hashmapet er stasjon ider i from av stringer og verdien er en arraylist med tupler som er andre stasjoner og tunnelen de deler med stasjonen som er nøkkelen i hashmapet. Graf hashmapet er som sagt en representasjon av oslo tbane nettverk og stasjon og tunnel hashmapet hjelper å hente fram til enkelt tunnler og stasjoner. Tbane har metodene som bygger opp grafen, tar imot input, gjør et dijkstra søk og printer ut resultatet. 


# Flow :
1. Hele programmet starter og kjøres i TBane.java. Det første som skjer er at et tbane objekt blir laget i main. Deretter blir tsv filene Station.tsv og Tunnel.tsv lest i readTsvFiles() funksjonen. I readTsvFiles() blir alle stasjonene og tunnelen opprettet som klasser og alt av viktig informasjon blir lagre intert i klassene. I readTsvFiles() blir også de opprette stasjons klassene`s navn lagt til i graf hashmapet som nøkler med tomme arraylist foreløpig.   

2. Det neste som skjer at kantene må legges til i graf hashmapet. For å legge til kanter/tunneler må vi fylle de tomme arralistene i graf hashmapet. Hver tuppel i disse arraylistene er en kant/tunnel mellom stasjonen som er nøkelverdi og stasjonen som er inni tuppelen. Kantene blir lagt til i addEgdes() ved å gå over alle nøkle verdiene for å legge til tupler av <"StatsjonID", "TunnelID"> i deres tomme arraylist. Vi gjør dette ved å gå over informasjon vi allerede har fra klassene indre informasjon som har blir lagt til i readTsvFiles(). Tidskomkleksiteten til addEgdes() skulle man tro er O(n^2) fordi man skal legge til alle kantene i grafen. I realiteten er den ikke O(n^2) fordi grafen ikke er komplett som vil si at ikke alle stasjoner er tilkoblet sammen gjennom en tunnel.  

3. Etter vi har lagt til kanter i graf hashmapet er modellen for Oslo tbane laget og vi kan utføre et dijskra søk basert på en brukers input. Det er flere funksjoner som brukes for å håntere og forme brukerns input til noe vi kan sende til dijkstra søket vårt. Når vi gjør et dijkstra søk får vi en graf som angir korteste vei fra start noden til alle noder til hver annen node i grafen. Vi ønsker bare en vei fra en spesefikk node til start noden. I metoden printPath() henter vi den spesefikke veien vi ønsker ved å gå baklengs fra slutt til start fra grafen vi fikk fra diksjra søket. Vi legger dette på en stack og deretter printe ut stacken slik at vi får veien vår fra start til slutt igjen. 


# Time complexity in dikstra algorithm:
- Tidskomleksiteten til dijkstra er O((V + E) log V) fordi jeg har implementer dijsktraen med en prioriterings kø. Algoritmen starter fra en gitt startnode og besøker alle nodens kanter for å finne den raskeste veien til hver nabo. For hver node puttes naboen med den korteste funnede veien tilbake i prioritetskøen for å finne den raskeste veien videre fra den noden. Dijkstra's algoritme antar at den først korteste veien den finner til en node alltid er den korteste, noe som er riktig for denne grafen fordi den ikke har negative kantvekter. Den totale tidskompleksiteten er en kombinasjon av O(V log V) for å hente den neste noden fra køen og O(E log V) for å oppdatere avstandene til naboene, noe som gir O((V + E) log V)


# How to run the program :
- In terminalen 
```
javac Station.java Tunnel.java Tbane.java
```

```
java Tbane
```

- Departure station
```
======================================
              Oslo Metro              
======================================
Choose linje:
1. Frognerseteren 
2. Østerås 
3. Kolsås
4. Vestli / Bergkrystallen
5. Ringen / Sognsvann

Write line number for the departure station: (q to quit): 1 
```

```
--------------------------------------
          Selected Line: 1
--------------------------------------
01 Frognerseteren
02 Voksenkollen
03 Lillevann
04 Skogen
05 Voksenlia
06 Holmenkollen
07 Besserud
08 Midtstuen
09 Skådalen
10 Vettakollen
11 Gråkammen
12 Slemdal
13 Ris
14 Gaustad
15 Vinderen
16 Steinerud
17 Frøen
18 Majorstuen
19 Nationaltheatret
20 Stortinget
21 Jernbanetorget
22 Grønland
23 Tøyen
24 Ensjø
25 Helsfyr
26 Brynseng
27 Høyenhall
28 Manglerud
29 Ryen
30 Brattlikollen
31 Karlsrud
32 Lambertseter
33 Munkelia
34 Bergkrystallen

Write departure station number: (q to quit): 18
```

- Destination station
```
======================================
              Oslo Metro              
======================================
Choose linje:
1. Frognerseteren 
2. Østerås 
3. Kolsås
4. Vestli / Bergkrystallen
5. Ringen / Sognsvann

Write line number for the departure station: (q to quit): 3
```

```
--------------------------------------
          Selected Line: 3
--------------------------------------
01 Kolsås
02 Hauger
03 Gjettum
04 Avløs
05 Haslum
06 Gjønnes
07 Bekkestua
08 Ringstabekk
09 Jar
10 Bjørnsletta
11 Åsjordet
12 Ullernåsen
13 Montebello
14 Smestad
15 Borgen
16 Majorstuen
17 Nationaltheatret
18 Stortinget
19 Jernbanetorget
20 Grønland
21 Tøyen
22 Ensjø
23 Helsfyr
24 Brynseng
25 Hellerud
26 Godlia
27 Skøyenåsen
28 Oppsal
29 Ulsrud
30 Bøler
31 Bogerud
32 Skullerud
33 Mortensrud

Write departure station number: (q to quit): 28
```

- Output
```
======================================
            Optimal route:             
======================================
Start at Line:1
===[1STN18 Majorstuen]===>
===[1STN19 Nationaltheatret]===> 
===[1STN20 Stortinget]===> 
===[1STN21 Jernbanetorget]===> 
===[1STN22 Grønland]===> 
===[1STN23 Tøyen]===> 
===[1STN24 Ensjø]===> 
===[1STN25 Helsfyr]===> 
===[1STN26 Brynseng]===> 

Change to Line:3
===[3STN24 Brynseng]===> 
===[3STN25 Hellerud]===> 
===[3STN26 Godlia]===> 
===[3STN27 Skøyenåsen]===> 
===[3STN28 Oppsal]===> 

Total time: 26min
```