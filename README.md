# IKPMD - PokeDex

![](https://www.1337.games/app/uploads/2020/03/pokemon-940x529.jpg)

**Nehir en Yassir**

31-1-2021

IKPMD periode 2 - Eerste gelegenheid

#

# Inhoudsopgave

- [Inleiding](#inleiding)
- [Bronnen](#bronnen)
- [Projectstructuur & Extra functies](#projectstructuur--extra-functies)
  * [Fragments & Mainactivity](#fragments--mainactivity)
  * [Fragment navigatie](#fragment-navigatie)  
- [Schermen](#schermen)
  * [Loading DB](#loading-db)  
    + [XML](#xml)
    + [Java](#java)
  * [PokeDex](#pokedex)  
    + [XML](#xml-1)
    + [Java](#java-1)
  * [Favorites](#favorites)  
    + [XML](#xml-2)
    + [Java](#java-2) 
  * [Pokedetails](#pokedetails)  
    + [XML](#xml-3)
    + [Java](#java-3)    
  * [Graphs](#graphs)  
    + [XML](#xml-4)
    + [Java](#java-4)    
- [Databases](#databases)
  * [Lokale Database](#lokale-database)
  * [Firebase](#firebase)  
- [Assets](#assets)
  * [Muziek](#muziek)
  * [Afbeeldingen](#afbeeldingen)  
  * [Kleuren](#kleuren)
  * [Libraries](#libraries)  
  * [Maven](#maven)

----------

# Inleiding
De app die in dit verslag beschreven is is een pokedex van alle eerste generatie Pokemen. Deze app is gebouwd voor API 30 en is getest op een Google Pixel 3A. In GIT hebben wij alles in de master branche gedaan, dus het was een shitshow. Maar het heeft enigzins gewerkt. Als er vragen of opmerkingen zijn schiet een issue is of contact s1113405@student.hsleiden.nl.

Features:
- Het zien van alle first gen pokemen
- Stats, geluiden en afbeeldingen van alle first gen pokemon zijn inzichtelijk
- Pokemon kunnen worden toegevoegd en verwijdert van Favorites
- Lokale database wordt gevuld met API calls
- Firebase wordt gevuld met Favorites
- Er is een graph fragment met allerlei statestieken

Known bugd:
- Soms is de Loading DB fragment eerder klaar dan de setGridFillables functie. Dit heeft te maken met het feit dat thread states niet zo heel lekker gemonitoord kunnen worden. Met meer tijd is het waarschijnlijk te fixen, ik heb voor nu een timer gehardcode op 90 seconden. Als je na de loading DB screen een pokedex krijgt met witte achtergrond en alleen maar bulbasaurs. Start dan de app opnieuw op door naar de AVD home screen te gaan, en je app handmatig te openen. Als je dat doet dan laden de grid items in.
- In de graphs staan niet alle types verwerkt omdat er ongeveer 20 types zijn. Dat zou nooi passen. Dus zijn alleen de base types uitgekozen.

----------



# Bronnen
- Voor de opmaak van de MD: https://gist.githubusercontent.com/jonschlinkert/ac5d8122bfaaa394f896/raw/bd1106691cf344e972f575c49ba3cf281beeb9b3/markdown-toc_repeated-headings.md
- Voor de inspiratie: https://github.com/skydoves/Pokedex
- RestFULL-API: https://pokeapi.co/
- API Wrapper: https://github.com/PokeAPI/pokekotlin
- Favorite button: https://github.com/IvBaranov/MaterialFavoriteButton
- Grids: https://stackoverflow.com/questions/27908680/how-lists-specifically-recyclerview-with-cardviews-in-android-work#
- Fragment navigation issue: https://github.com/android/architecture-components-samples/issues/530
- Graphs: https://github.com/jjoe64/GraphView
- Pokemon sounds: https://veekun.com/dex/downloads#other-files
- Images: https://pokeres.bastionbot.org/images/pokemon/1.png

----------

# Projectstructuur & Extra functies
## Fragments & Mainactivity
Alle fragments communiceren via de mainactivity. Hieronder is een schematische weergave gegeven van de werking van de communicatie tussen de fragments en de mainactivity. De navigatie tussen de fragments is anders geregeld, dat is in het volgende subkopje gedefineerd.

![](Untitled%20Diagram.png)

## Fragment navigatie
De volgende statements worden gebruikt om van de een naar de andere fragment te navigeren, do note dat deze snippets in de mainactivity staan en ook vanuit daar aangeroepen worden.
- makes sure app starts with loading DB screen
```
  try {
       FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
       ft.replace(R.id.nav_host_fragment, d);
       ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
       ft.addToBackStack(null);
       ft.commit();
  } catch (Exception e) {
       e.printStackTrace();
  }
```  
De fragments zijn als variabelen gedefineerd bovenaan de mainactivity:
 ```
 Fragment a = new PokeDetails();
 Fragment b = new FavoritesFragment();
 Fragment d = new LoadingDBFragment();
 Fragment e = new NavHostFragment();
 Fragment f = new GraphFragment();
 ```
 
Door deze fragments bovenaan te defineren kan je gemakkelijk de try catch statement afstellen op de juiste fragment. Echter bleek gedurende het project dat de navigationbar onderaan de mainactivity problemen opleverde. De states van de fragments werden niet onthouden waardoor functies zoals SetGridFillables niet goed werkten. Google wilde de problemen niet fixen, omdat ze vonden dat het een feature was, en niet een bug. De originele github issue is te vinden in de bronnen https://github.com/android/architecture-components-samples/issues/530. Om dit probleem te fixen is het volgende stuk code geimplementeerd in de Onstart():

        BottomNavigationView botnav = findViewById(R.id.nav_view);
        GridLayout mainGrid = findViewById(R.id.mainGrid2);
        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        //println("Yes a mattie");
                        fm.beginTransaction().replace(R.id.nav_host_fragment, e, "1").show(e).commit();
                        mainGrid.setClickable(true);
                        mainGrid.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.navigation_dashboard:
                        //System.out.println("Yes a mattie");
                        fm.beginTransaction().replace(R.id.nav_host_fragment, b, "2").show(b).commit();
                        mainGrid.setClickable(false);
                        mainGrid.setVisibility(View.INVISIBLE);

                        return true;
                    case R.id.navigation_graph:
                        //System.out.println("Yes a mattie");
                        fm.beginTransaction().replace(R.id.nav_host_fragment, f, "3").show(f).commit();
                        mainGrid.setClickable(false);
                        mainGrid.setVisibility(View.INVISIBLE);


                        return true;
                }
                return false;
            }
        });



----------



# Schermen
## Loading DB
Deze fragment is een loading screen. Tijdens de zichtbaarheid van deze loading screen worden de API calls gemaakt. Klik op de GIF hieronder voor een demo:

[<img src="loadingdb.gif" width="225" height="475"/>](https://www.youtube.com/watch?v=9nxdhsyUO7g)

### XML

### JAVA

----------

## PokeDex
Deze fragment bevat een grid van alle first gen pokemon. Klik op de GIF hieronder voor een demo:

[<img src="pokedex.gif" width="225" height="475"/>](https://www.youtube.com/watch?v=XdETn0bsa0E)
### XML

### JAVA

----------

## Favorites
Deze fragment bevat een grid van alle favorite pokemon. Klik op de GIF hieronder voor een demo:

[<img src="favorites.gif" width="225" height="475"/>](https://www.youtube.com/watch?v=pn1e-YQCuhU)
### XML

### JAVA


----------

## Pokedetails
Deze fragment bevat een grid van alle favorite pokemon. Klik op de GIF hieronder voor een demo:

[<img src="pokedetails.gif" width="225" height="475"/>](https://www.youtube.com/watch?v=nC-LnVQLW-k)
### XML

### JAVA


----------

## Graphs
Deze fragment bevat een tweetal grafieken met daarin statestieken over je favorites. Klik op de GIF hieronder voor een demo:

[<img src="graphs.gif" width="225" height="475"/>](https://www.youtube.com/watch?v=q6owxm8XAwI)
### XML

### JAVA

----------


# Databases
## Lokale database

## Firebase


----------

# Assets
## Muziek

## Afbeeldingen

## Kleuren

## Libraries

## Maven

----------
####
