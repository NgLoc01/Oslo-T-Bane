# Oslo-T-Bane
**Norwegian:**
Dette java programmet skal finne den korteste vei mellom to stasjoner på Oslo t-bane med hjelp av dijkstra algoritmen.
Dataen for tunnelene og stasjoene er laget av meg og baserer seg på Oslo t-bane tabel. Brukeren velger først linjen 
avgangsstasjonen ligger på, deretter selve stasjonen. Så velger brukeren linjen destinasjonsstasjonen ligger på og til
slutt stasjonen. Programmet skriver ut den raskeste ruten fra avgangsstasjonen til destinasjonsstasjonen, basert på 
dataene i filene Tunnels.tsv og Stations.tsv

**Filer :**
# Tunnels.tsv:
Tunnels.tsv består av alle tunnelene i Oslos t-banesystem. Hver tunnel har en unik id det første 2 sifrene sier hvilke line 
tunnelen tilhører. Hvis de to første sifrene er like vil det si at tunnelen går mellom to stasjoner på den samme linjen. Om
to do sifrene er anderledes betyr det at tunnelen er en overgangs tunnel. En overgangstunnel repesentere egentlig et linje
bytte fra en linje til en annen. Videre i til en tunnel id står det TN som betyr at det er en tunnel. De siste fire sifrene 
sier hvilke to stasjoner som tunnelen med iden er imellom. Man ser på de 4 siste sifrene som par hvor hvert par som sagt er 
er stasjonene tunnelen er imellom. Det første av sifrene de to første sifrene tilhører det første paret i de 4 siste sifrene,
mens det andre sifret av de to første sidrene tilhører det andre paret i de 4 siste sifrene. Vet å se på denne kombinasjonen 
kan man se hvilke linje en stasjon tilhører. Dette er spesielt nyttig ved et linjebytte hvor man lett kan se hvilke stasjon
man bytter til. Etter selve tunnel iden kommer det tileggsinfomasjon som hva stasjoene heter, hvor lang tid det tar å reise 
gjennom denne tunnelen og om det eventuelt er en overgangstunnel.

Eksempel:
12TN1811 Majorstuen-Majorstuen 5 TRANSITION "TUNNEL"

Ved å se på de første 2 sifrene kan vi se atdDette er en tunnel som går mellom linje 1 og 2. Videre står det "TN" og vi kan
være sikre på at dette er en tunnel id. Videre har vi 18 også 11. Dette betyr at vi har en tunnel som går fra linje 1 stasjon
18 til linje 2 stasjon 11. Videre ser vi at det står "Majorstuen-Majorstuen" så vi vet at begge stasjonen på linjen er 
majorstuen stoppe på hver sin linje. Dette er tydlig et overgangstunnel som vi får bekreftet ved at det står "TRANSITION "TUNNEL""
på slutten 



Stations.tsv:
Stations.tsv består av alle Stasjonene i Oslos t-banesystem. Hver stasjon har en unik id hvor det første sifere sier hvilke line stoppe befinner 
seg på. En stasjon kan befinne seg på flere linjer som tilsier at flere tbane linjer kjører på samme spor.  

Example:
1STJ18 Majorstuen 11TN1718 11TN1819 12TN1811 13TN1816 14TN1821 15TN1810
Dette er et eksemepl på en statsjon på line 1. Det første vi ser er 1STJ18 som indikerer at denne stasjon er på linje 1 og er på stopp 18.
Videre ser vi navet på stasjonen. De neste to ordene er altså 11TN1718 11TN1819 er tunnel IDer og er de tunnelene man bruker når man kjører på 
den linjen stasjonen oppgir. Alle stasjonene skal skal minsta ha to tunner som brukes på linjen med untak av ende stasjoene som bare har en tunnel ut 
fra seg. Videre kan en Tunnel ha flere "tunnler" den kan ta i bruk. Disse er egentlig ikke tunneller, men signalisere at man bytter fra en linje til en  annen. I eksempelent har vi 12TN1811, her ser vi at vi bytter fra linje 1 til linje 2 indikert på starten av IDen. Her bytter man fra majorstuen på stopp 18 på linje 1 til majorstuen på stopp 11 på linje 2.


**Helhet :**
readTsvFiles

**Tidskompleksistet :**
addEgdes()
/* EXPLANATION: adding egdes 
        First, the graph hashmap is retrieved to iteration through. We iterater over all station ids and its list with tuples[another station, connecting tunnel]
            One station we get the station's tunnel list (all tunnel this station uses) 
                iterate over all tunnles for spesifc station 
                    get spesific tunnels own station list
                        iterate over the spesific tunnels own station list
                            now have all the information we need to add a egde
                                Each station in a tunnels internal station list uses the same tunnel as the station we curently iteration on
                                it therfore an egde and can add the egde to the grpah hashmap
                                    ["Station", {["diffrent station", "shared tunnel"], ["diffrent station", "shared tunnel"], ...}]
                
        Summed up, for every station in graph hashmap, get their tunnel list, then go trought each of those tunnels to their own station list,
        every station that is on a list shares a tunnel with the first station and have an egde                  
        
        TIME COMPLEXITY:
        You should think that the time complexecy would me O(n^2) but in reallity it not beacuse the graph is not complete
            Every station is not connected to every other station via tunnels  
        */

**English:** 
This java program will find the shortest possible route between two stations in the Oslo t-bane network with the help of dijkstra algorithm. 
Data for both the tunnel and the stations was created by me, based on the Oslo metro schedule.
