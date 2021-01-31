# IKPMD - PokeDex

<img src="https://www.1337.games/app/uploads/2020/03/pokemon-940x529.jpg" width="470" height="265"/>


**Nehir (S1121419 - INF2D - BDAM) en Yassir (S1113405 - INF3A - FICT)**

31-1-2021

IKPMD Periode 2 - Eerste gelegenheid

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
  * [Geluiden](#geluiden)
  * [Afbeeldingen](#afbeeldingen)  
  * [Kleuren](#kleuren)
  * [Libraries](#libraries)  
  * [Maven](#maven)

----------

# Inleiding
De app die in dit verslag beschreven is is een pokedex van alle eerste generatie Pokemen. Deze app is gebouwd voor API 30 en is getest op een Google Pixel 3A. In GIT hebben wij alles in de master branche gedaan, dus het was een shitshow. Maar het heeft enigzins gewerkt. Als er vragen of opmerkingen zijn, schiet dan een issue in.

Features:
- Het zien van alle first gen pokemen
- Stats, geluiden en afbeeldingen van alle first gen pokemon zijn inzichtelijk
- Pokemon kunnen worden toegevoegd en verwijdert van Favorites
- Lokale database wordt gevuld met API calls
- Firebase wordt gevuld met Favorites
- Er is een graph fragment met allerlei statestieken

Known bugs:
- Soms is de Loading DB fragment eerder klaar dan de setGridFillables functie. Dit heeft te maken met het feit dat thread states niet zo heel lekker gemonitoord kunnen worden. Met meer tijd is het waarschijnlijk te fixen, ik heb voor nu een timer gehardcode op 90 seconden. Als je na de loading DB screen een pokedex krijgt met witte achtergrond en alleen maar bulbasaurs. Start dan de app opnieuw op door naar de AVD home screen te gaan, en je app handmatig te openen. Als je dat doet dan laden de grid items in. 
  * Tl;dr: trigger onstart if grid only bulbasaur.
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

Daarnaast is er ook nog een stukje code ingebouwd voor het geval dat een user de "Go Back" knop gebruikt op zijn/haar telefoon. In essentie zorgt de code snippet hieronder ervoor dat er altijd terug gegaan wordt naar de main grid met alle first gen pokemen:
```
   // IF THE ANDROID BACK BUTTON IS CLICKED DO THIS PLEASE
    @Override
    public void onBackPressed() {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, e);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
            GridLayout mainGrid = findViewById(R.id.mainGrid);
            mainGrid.setClickable(true);
            mainGrid.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            setContentView(R.layout.fragment_loading_d_b);
            messWithFirebase();
        }


    }
```

----------



# Schermen
## Loading DB
Deze fragment is een loading screen. Tijdens de zichtbaarheid van deze loading screen worden de API calls gemaakt. Klik op de GIF hieronder voor een demo:

[<img src="loadingdb.gif" width="225" height="475"/>](https://www.youtube.com/watch?v=9nxdhsyUO7g)

### XML
De XML van het Loading DB scherm is extreem simpel. Het is namelijk niet de bedoeling dat de eindgebruiker hier vaak te maken mee gaat krijgen; enkel bij de eerste keer van het opstarten van de applicatie zal de eindgebruiker dit scherm zien. De XML bestaat uit een *FrameLayout* waaronder een simpele *TextView* en een *ProgressBar* staan.
```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.LoadingDBFragment">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#252426"
        android:gravity="center"
        android:text="Loading Database, go grab a snack ;)"
        android:textColor="#FFFFFF"
        android:textSize="15sp" />

    <ProgressBar
        android:id="@+id/progressBar"
        style="?android:attr/progressBarStyle"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:foregroundTint="#F44336"
        android:progressBackgroundTint="#F44336"
        android:progressTint="#F44336"
        android:secondaryProgressTint="#F44336"
        android:indeterminateTint="#F44336"/>

</FrameLayout>
```
### JAVA
Op de achtergrond wordt tijdens deze fragment de functie valuesToDB gerunned als thread. Deze functie zorgt ervoor dat er API calls worden gemaakt naar de PokeAPI, en de data in de lokale database wordt opgeslagen. Dit moet allemaal gedaan worden terwijl dat de Loading DB fragment draait. In de Oncreate wordt de loading DB fragment geinitialiseerd op de manier zoals bij [Fragment navigatie](#fragment-navigatie) te zien was. Verder wordt de functie valuesToDB in de onStart aangeroepen:
```
        //MainActivity dbhelper = this;
        DatabaseHelper dbHelper2 = DatabaseHelper.getHelper(this);

        if (dbHelper2.getProfilesCount(DatabaseInfo.PokemonTable.POKEMONTABLE) == 151) {

            try {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, e);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();


            } catch (Exception e) {
                e.printStackTrace();
            }
            setGridFillables();
        } else {
            if (d.isAdded()) {
                Runnable backGroundRunnable = new Runnable() {
                    public void run() {
                        try {
                            valuesToDB();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //Thread.sleep(30000);
                                        setGridFillables();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread sampleThread = new Thread(backGroundRunnable);
                sampleThread.start();
            }
        }

        if(e.isAdded()){
            setGridFillables();
        }
```        

Verder is de enige java die ertoe doet de parent klasse (de standaard voor fragments) en welke XML-layout is gekozen:
```
public class LoadingDBFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading_d_b, container, false);
    }



}
```
----------

## PokeDex
Deze fragment bevat een grid van alle first gen pokemon. Klik op de GIF hieronder voor een demo:

[<img src="pokedex.gif" width="225" height="475"/>](https://www.youtube.com/watch?v=XdETn0bsa0E)
### XML
De XML layout voor de pokedex is straight foward. Je hebt een scrollview om te scrollen, met daarin een lineair layout, waar dan weer een grid layout in zit.
In de grid layout zitten 151 cardviews, waarin dan weer de imageviews en textviews zitten. De ID's voor deze cardviews en de child nodes ervan zijn semi-hardcoded met Excel en android studioshortcuts om het proces wat sneller te maken.
Zo ziet een cardview + child nodes eruit:
```
<androidx.cardview.widget.CardView
                android:id="@+id/pokecard1"
                android:layout_width="30dp"
                android:layout_height="30dp"
                android:layout_rowWeight="1"
                android:layout_columnWeight="1"
                android:layout_marginLeft="16dp"
                android:layout_marginRight="16dp"
                android:layout_marginBottom="20dp"
                android:elevation="4dp"
                android:onClick="Switcher_main_to_poke"
                app:cardCornerRadius="10dp"
                tools:ignore="OnClick">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_margin="10dp"
                    android:orientation="vertical"
                    android:weightSum="3">

                    <ImageView
                        android:id="@+id/pokeimg1"
                        android:layout_width="50dp"
                        android:layout_height="50dp"
                        android:layout_gravity="center_horizontal"
                        android:src="@drawable/bulbasaur" />

                    <com.iambedant.text.OutlineTextView
                        android:id="@+id/pokename1"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_horizontal"
                        android:padding="5dp"
                        android:text="@string/pkmn_name1"
                        android:textAlignment="center"
                        android:textColor="@android:color/black"
                        android:textSize="20sp"
                        android:textStyle="bold"
                        app:outlineColor="@color/kloteding"
                        app:outlineWidth="0.1" />

                </LinearLayout>
            </androidx.cardview.widget.CardView>
```
### JAVA
De functie in java die ertoe doet bij de homefragment is setGridFillables. Zoals de naam suggereerd wordt in deze functie alle gegevens verwerkt in de maingrid. Deze functie wordt in de onstart van de mainactivity aangeroepen. In dit hoofdstuk worden snippets gegeven van stukjes code en die worden toegelicht. Alle snippets zitten in een for loop die door 151 indeces loopt, want er zijn 151 first gen pokemen.
- Set names
```
            //set name
            String name = getAllDBItems().get(i).get(0);
            int e = i + 1;
            String textviewID = "pokename" + e;
            int resID = this.getResources().getIdentifier(textviewID, "id", getPackageName());
            TextView pokenam = findViewById(resID);
            pokenam.setText(name);
            pokenam.getText();
```
            
- Set color 
```
           String type1 = getAllDBItems().get(i).get(1);
            int q = i + 1;
            String cardviewID = "pokecard" + q;
            int resID_card = this.getResources().getIdentifier(cardviewID, "id", getPackageName());
            CardView pokecard = findViewById(resID_card);
            if(type1.equals("rock")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.rock));
            }else if(type1.equals("ground")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.egg_yellow));
            }else if(type1.equals("normal")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.normal));
            }else if(type1.equals("fighting")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.fighting));
            }else if(type1.equals("flying")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.flying));
            }else if(type1.equals("poison")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.poison));
            }else if(type1.equals("bug")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.bug));
            }else if(type1.equals("ghost")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.ghost));
            }else if(type1.equals("steel")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.steel));
            }else if(type1.equals("???")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.special));
            }else if(type1.equals("fire")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.fire));
            }else if(type1.equals("water")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.water));
            }else if(type1.equals("grass")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.grass));
            }else if(type1.equals("electric")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.electric));
            }else if(type1.equals("psychic")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.psychic));
            }else if(type1.equals("ice")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.ice));
            }else if(type1.equals("dragon")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.dragon));
            }else if(type1.equals("dark")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.dark));
            }else if(type1.equals("fairy")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.fairy));
            }
```           
and so on, I think you get the idea....

----------

## Favorites
Deze fragment bevat een grid van alle favorite pokemon. Klik op de GIF hieronder voor een demo:

[<img src="favorites.gif" width="225" height="475"/>](https://www.youtube.com/watch?v=pn1e-YQCuhU)
### XML
Wat betreft de XMl layout van favorites is het vrijwel hetzelfde als de maingrid. Alleen zijn er:
1. Minder cardviews per row (namelijk maar 1 ipv 2)
2. Hebben de cardviews iets meer inhoud.
Voornamelijk dat laatste heeft invloed op de layout. Er zijn progress wheels toegevoegd om de base stats van een pokemon te kunnen weergeven. Zie code snippet van de cardview:
```
            <androidx.cardview.widget.CardView
                android:id="@+id/fav1"
                android:layout_width="0dp"
                android:layout_height="50dp"
                android:layout_rowWeight="1"
                android:layout_columnWeight="1"
                android:layout_marginLeft="16dp"
                android:layout_marginRight="16dp"
                android:layout_marginBottom="20dp"
                android:elevation="4dp"
                android:onClick="Switcher_main_to_poke_fav"
                android:visibility="invisible"
                app:cardCornerRadius="10dp"
                tools:ignore="OnClick">


                <ProgressBar
                    android:id="@+id/hp1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="40dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right"
                    android:background="@drawable/circle_shape"
                    android:indeterminate="false"
                    android:max="100"
                    android:progress="65"
                    android:progressDrawable="@drawable/circular_progress_bar"
                    android:translationX="-30dp"
                    android:translationY="10dp" />

                <ProgressBar
                    android:id="@+id/atk1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="40dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right"
                    android:background="@drawable/circle_shape"
                    android:indeterminate="false"
                    android:max="100"
                    android:progress="65"
                    android:progressDrawable="@drawable/circular_progress_bar"
                    android:translationX="-80dp"
                    android:translationY="10dp" />

                <ProgressBar
                    android:id="@+id/spatk1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="40dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right"
                    android:background="@drawable/circle_shape"
                    android:indeterminate="false"
                    android:max="100"
                    android:progress="65"
                    android:progressDrawable="@drawable/circular_progress_bar"
                    android:translationX="-130dp"
                    android:translationY="10dp" />

                <ProgressBar
                    android:id="@+id/def1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="40dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right"
                    android:background="@drawable/circle_shape"
                    android:indeterminate="false"
                    android:max="100"
                    android:progress="65"
                    android:progressDrawable="@drawable/circular_progress_bar"
                    android:translationX="-180dp"
                    android:translationY="10dp" />

                <ProgressBar
                    android:id="@+id/spdef1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="40dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right"
                    android:background="@drawable/circle_shape"
                    android:indeterminate="false"
                    android:max="100"
                    android:progress="65"
                    android:progressDrawable="@drawable/circular_progress_bar"
                    android:translationX="-230dp"
                    android:translationY="10dp" />

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_row="0"
                    android:layout_column="11"
                    android:layout_margin="16dp"
                    android:orientation="vertical"
                    android:weightSum="3">


                    <TextView
                        android:id="@+id/favname1"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:paddingTop="0dp"
                        android:text="@string/pkmn_name1"
                        android:textAlignment="center"
                        android:textColor="@android:color/black"
                        android:textSize="15sp"
                        android:textStyle="bold" />


                    <ImageView
                        android:id="@+id/favimg1"
                        android:layout_width="50dp"
                        android:layout_height="match_parent"
                        android:layout_alignParentLeft="true"
                        android:layout_gravity="center_vertical"
                        android:paddingTop="0dp"
                        android:src="@drawable/pkmn"
                        android:translationY="-10dp" />


                </LinearLayout>

            </androidx.cardview.widget.CardView>
```            

### JAVA
De java code van deze fragment was best geinig omdat ik hier voor het eerst heb gespeeld met dynamic visibility of items. De functie die er het meeste toe doet is setFavFillables(). Deze functie maakt gebruik van gegevens die zijn meegegeven uit de pokedetailsfragment, zodat het weet welke pokemon gerendered moeten worden in de favorites. Dit doet de functie door door alle entries van een meegegeven list te loopen en deze bij onStart visible te maken in de grid (ik heb wat setters weg gelaten want anders wordt het nogal een lange snippet):
```
    public void setFavFillables(){
        GridLayout layout = getView().findViewById(R.id.mainGrid);
        int count = layout.getChildCount();
        View v = null;
        for(Object entry : allFavs){
            for(int i=0; i<FavCounter; i++) {
                v = layout.getChildAt(i);
                int e = i+1;

                String newEntry = allFavs.get(i).toString().replace("[","").replace("]","");
                String[] SplittedString = newEntry.split(", ");

                //set name
                String textviewID = "favname" + e;
                int resID = getResources().getIdentifier(textviewID, "id", getActivity().getPackageName());
                TextView pokenam = v.findViewById(resID);
                String name= SplittedString[0];
                pokenam.setText(name);
                pokenam.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat("20"));

                //set visibility
                if(SplittedString[0] == (pokenam.getText())){
                    v.setVisibility(View.VISIBLE);
                }else{
                    v.setVisibility(View.INVISIBLE);
                }

                //set HP
                //set ATK
               
                //set spatk
                String SPATK = "spatk" + e;
                int spatkID = getResources().getIdentifier(SPATK, "id", getActivity().getPackageName());
                ProgressBar pokespatk = v.findViewById(spatkID);
                String spatk = SplittedString[3];
                //System.out.println("SPATK: " + name + spatk);
                pokespatk.setMax(194);
                pokespatk.setProgress(parseInt(spatk));
                pokespatk.setProgressTintList(ColorStateList.valueOf(parseColor("#2196F3")));


                //set def
                //set spdef
               
                //set image
                String IMG = "favimg" + e;
                int imgID = getResources().getIdentifier(IMG, "id", getActivity().getPackageName());
                ImageView pokeimg = v.findViewById(imgID);

                //System.out.println("Fatboy Slim " + );
                pokeimg.setImageResource(PokeDetails.pictureArray[PokeDetails.PokeIDs.get(pokenam.getText())-1]);

            }

        }
    }
```    

----------

## Pokedetails
Deze fragment bevat een grid van alle favorite pokemon. Klik op de GIF hieronder voor een demo:

[<img src="pokedetails.gif" width="225" height="475"/>](https://www.youtube.com/watch?v=nC-LnVQLW-k)
### XML
De XML van Pokedetails bestaat uit een hoop *TextViews* en een aantal *ProgressBars* die de statistieken van de Pokémon weergeven. 
Eigenlijk is alles aan de XML van Pokedetails erg simpel en spreekt het voor zich, op de *FavoritesButton* na. Deze is geimporteerd vanuit een GitHub repository; [IvBaranov / MaterialFavoriteButton](https://github.com/IvBaranov/MaterialFavoriteButton). Over het gebruik van deze library verwijzen we graag door naar de desbetreffende library. Wij hebben geen gebruik gemaakt van de animatie functie binnen deze library; onze *FavoriteButton* is static.
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#2B2828"
    android:elevation="1dp"
    tools:context=".PokeDetails">

    <com.github.ivbaranov.mfb.MaterialFavoriteButton
        android:id="@+id/Favorite_Button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_above="@+id/textName"
        android:layout_alignParentEnd="true"
        android:layout_marginEnd="10dp"
        android:layout_marginBottom="167dp"
        android:baselineAlignBottom="false"
        android:elevation="1dp"
        android:foregroundGravity="center"
        app:mfb_animate_favorite="true"
        app:mfb_bounce_duration="300"
        app:mfb_favorite_image="@drawable/ic_baseline_star_24"
        app:mfb_not_favorite_image="@drawable/ic_baseline_star_border_24"
        app:mfb_padding="12"
        app:mfb_rotation_angle="360"
        app:mfb_rotation_duration="400"
        app:mfb_size="64" />

    <TextView
        android:id="@+id/textName"
        android:layout_width="187dp"
        android:layout_height="82dp"
        android:layout_below="@+id/imageView"
        android:layout_alignParentStart="true"
        android:layout_alignParentEnd="true"
        android:layout_marginStart="107dp"
        android:layout_marginTop="-11dp"
        android:layout_marginEnd="118dp"
        android:gravity="center"
        android:text="Bulbasaur"
        android:textColor="#FFFFFF"
        android:textSize="30dp" />



    <androidx.gridlayout.widget.GridLayout
        android:layout_width="180dp"
        android:layout_height="180dp"
        android:layout_alignParentStart="true"
        android:layout_alignParentEnd="true"
        android:layout_alignParentBottom="true"
        android:layout_marginStart="34dp"
        android:layout_marginEnd="36dp"
        android:layout_marginBottom="17dp">

        <TextView
            android:id="@+id/textHP"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="HP"
            android:textColor="#A59B9B"
            app:layout_column="0"
            app:layout_row="0" />

        <ProgressBar
            android:id="@+id/progressHP"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="300dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:gravity="center"
            android:max="250"
            android:progressBackgroundTint="#FFFFFF"
            android:progressTint="#DF0C1E"
            android:scaleY="3"
            app:layout_column="1"
            app:layout_row="0" />

        <TextView
            android:id="@+id/textATK"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="ATK"
            android:textColor="#A59B9B"
            app:layout_column="0"
            app:layout_row="1" />

        <ProgressBar
            android:id="@+id/progressATK"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="300dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:gravity="center"
            android:max="134"
            android:progressBackgroundTint="#FFFFFF"
            android:progressTint="#FF9800"
            android:scaleY="3"
            app:layout_column="1"
            app:layout_row="1" />

        <TextView
            android:id="@+id/textSPATK"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="SP. ATK"
            android:textColor="#A59B9B"
            app:layout_column="0"
            app:layout_row="2" />

        <ProgressBar
            android:id="@+id/progressSPATK"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="300dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:gravity="center"
            android:max="194"
            android:progressBackgroundTint="#FFFFFF"
            android:progressTint="#2196F3"
            android:scaleY="3"
            app:layout_column="1"
            app:layout_row="2" />

        <TextView
            android:id="@+id/textDEF"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="DEF"
            android:textColor="#A59B9B"
            app:layout_column="0"
            app:layout_row="3" />

        <ProgressBar
            android:id="@+id/progressDEF"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="300dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:gravity="center"
            android:max="180"
            android:progressBackgroundTint="#FFFFFF"
            android:progressTint="#FFEB3B"
            android:scaleY="3"
            app:layout_column="1"
            app:layout_row="3" />

        <TextView
            android:id="@+id/textSPDEF"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="SP. DEF"
            android:textColor="#A59B9B"
            app:layout_column="0"
            app:layout_row="4" />

        <ProgressBar
            android:id="@+id/progressSPDEF"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="300dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:gravity="center"
            android:max="255"
            android:progressBackgroundTint="#FFFFFF"
            android:progressTint="#4CAF50"
            android:scaleY="3"
            app:layout_column="1"
            app:layout_row="4" />

        <TextView
            android:id="@+id/textSPD"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="SPD"
            android:textColor="#A59B9B"
            app:layout_column="0"
            app:layout_row="5" />

        <ProgressBar
            android:id="@+id/progressSPD"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="300dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:gravity="center"
            android:max="140"
            android:progressBackgroundTint="#FFFFFF"
            android:progressTint="#EA6FC7"
            android:scaleY="3"
            app:layout_column="1"
            app:layout_row="5" />
    </androidx.gridlayout.widget.GridLayout>

    <TextView
        android:id="@+id/textBaseStats"
        android:layout_width="130dp"
        android:layout_height="40dp"
        android:layout_alignParentStart="true"
        android:layout_alignParentEnd="true"
        android:layout_alignParentBottom="true"
        android:layout_marginStart="80dp"
        android:layout_marginEnd="80dp"
        android:layout_marginBottom="220dp"
        android:gravity="center"
        android:text="Base Stats"
        android:textColor="#FFFFFF"
        android:textSize="20sp"
        android:textStyle="bold"
        android:visibility="visible" />

    <TextView
        android:id="@+id/textHeight"
        android:layout_width="130dp"
        android:layout_height="wrap_content"
        android:layout_alignParentEnd="true"
        android:layout_alignParentBottom="true"
        android:layout_marginEnd="50dp"
        android:layout_marginBottom="270dp"
        android:gravity="center"
        android:text="Height"
        android:textColor="#A59B9B" />

    <TextView
        android:id="@+id/textHeightValue"
        android:layout_width="130dp"
        android:layout_height="40dp"
        android:layout_alignParentEnd="true"
        android:layout_alignParentBottom="true"
        android:layout_marginEnd="50dp"
        android:layout_marginBottom="290dp"
        android:gravity="center"
        android:text="0,69 M"
        android:textColor="#FFFFFF"
        android:textSize="20sp"
        android:textStyle="bold"
        android:visibility="visible" />

    <TextView
        android:id="@+id/textWeightValue"
        android:layout_width="130dp"
        android:layout_height="40dp"
        android:layout_alignParentStart="true"
        android:layout_alignParentBottom="true"
        android:layout_marginStart="50dp"
        android:layout_marginBottom="290dp"
        android:gravity="center"
        android:text="6,9 KG"
        android:textColor="#FFFFFF"
        android:textSize="20sp"
        android:textStyle="bold"
        android:visibility="visible" />

    <TextView
        android:id="@+id/textWeight"
        android:layout_width="130dp"
        android:layout_height="wrap_content"
        android:layout_alignParentStart="true"
        android:layout_alignParentBottom="true"
        android:layout_marginStart="50dp"
        android:layout_marginBottom="270dp"
        android:gravity="center"
        android:text="Weight"
        android:textColor="#A59B9B" />

    <TextView
        android:id="@+id/textType"
        android:layout_width="130dp"
        android:layout_height="30dp"
        android:layout_alignParentStart="true"
        android:layout_alignParentBottom="true"
        android:layout_marginStart="50dp"
        android:layout_marginBottom="345dp"
        android:background="@drawable/poketag"
        android:backgroundTint="#4CAF50"
        android:gravity="center"
        android:text="Grass"
        android:textColor="#FFFFFF"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/textType2"
        android:layout_width="130dp"
        android:layout_height="30dp"
        android:layout_alignParentEnd="true"
        android:layout_alignParentBottom="true"
        android:layout_marginEnd="50dp"
        android:layout_marginBottom="345dp"
        android:background="@drawable/poketag"
        android:backgroundTint="#9C27B0"
        android:gravity="center"
        android:text="Poison"
        android:textColor="#FFFFFF"
        android:textStyle="bold"
        android:visibility="visible" />

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="match_parent"
        android:layout_height="232dp"
        android:background="@drawable/bg"
        android:backgroundTint="#75AA4F"
        android:paddingTop="15dp"
        android:paddingBottom="10dp"
        android:scaleType="centerInside"
        android:src="@drawable/bulbasaur" />

</RelativeLayout>
```
### JAVA
In de JAVA van Pokedetails gebeuren een aantal dingen die beschreven moeten worden:
- Het invullen van Pokedetails
- Het afspelen van de geluiden
- Het afspelen van de imageshake
- De implementatie van de *FavoriteButton*

Om te beginnen met het invullen van Pokedetails. De invulling gaat volgens een redelijk simpel concept; het haalt de statistieken en variabelen van elke Pokémon op van de root view. Deze worden vervolgens binnen het *Pokedetails* fragment opgeslagen en ingevuld in het *Pokedetails* template..
```
 public void setPokeDetailsFillables(View root){
        //setname
        TextView pokedetails_name = root.findViewById(R.id.textName);
        String[] splittedString = PokeStats.split(", ");
        pokedetails_name.setText(splittedString[0]);

        //Set HP
        ProgressBar pokedetails_hp = root.findViewById(R.id.progressHP);
        pokedetails_hp.setProgress(Integer.parseInt(splittedString[3]),true);

        //Set ATK
        ProgressBar pokedetails_atk = root.findViewById(R.id.progressATK);
        pokedetails_atk.setProgress(Integer.parseInt(splittedString[4]),true);

        //Set SP_ATK
        ProgressBar pokedetails_sp_atk = root.findViewById(R.id.progressSPATK);
        pokedetails_sp_atk.setProgress(Integer.parseInt(splittedString[5]),true);

        //Set DEF
        ProgressBar pokedetails_def = root.findViewById(R.id.progressDEF);
        pokedetails_def.setProgress(Integer.parseInt(splittedString[6]),true);

        //Set SPDEF
        ProgressBar pokedetails_sp_def = root.findViewById(R.id.progressSPDEF);
        pokedetails_sp_def.setProgress(Integer.parseInt(splittedString[7]),true);

        //Set SPD
        ProgressBar pokedetails_spd = root.findViewById(R.id.progressSPD);
        pokedetails_spd.setProgress(Integer.parseInt(splittedString[8]),true);

        //Set weight
        TextView pokedetails_weight = root.findViewById(R.id.textWeightValue);
        Float realweight = Float.valueOf(splittedString[9])/10;
        pokedetails_weight.setText(realweight + " KG");

        //Set weight
        TextView pokedetails_height = root.findViewById(R.id.textHeightValue);
        Float realheight = Float.valueOf(splittedString[10])/10;
        pokedetails_height.setText(realheight + " M");
        
        //Set type
        TextView pokedetails_type1 = root.findViewById(R.id.textType);
        pokedetails_type1.setText(splittedString[1]);
        String type1 = (String) pokedetails_type1.getText();
```
De images van de Pokémon zijn hardcoded; het kijkt naar de Pokémon ID en op basis daarvan word de image opgehaald en ingevuld.
```
PokeIDs.put("bulbasaur", 1);
PokeIDs.put("ivysaur", 2);
PokeIDs.put("venusaur", 3);
...
PokeIDs.put("mewtwo", 150);
PokeIDs.put("mew", 151);
```
Elke type heeft zijn eigen signature color. Deze worden in dezelfde methode gekoppeld aan de desbetreffende types.
```
 if(type1.equals("rock")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.rock)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.rock)));
        }else if(type1.equals("ground")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.egg_yellow)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.egg_yellow)));
            pokedetails_type1.setTextColor(getResources().getColor(R.color.black));
        }else if(type1.equals("normal")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.normal)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.normal)));
...
        }else if(type1.equals("dark")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark)));
        }else if(type1.equals("fairy")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fairy)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fairy)));
            pokedetails_type1.setTextColor(getResources().getColor(R.color.black));
        }
```
Voordat de geluiden afgespeeld kunnen worden, zijn deze eerst gekoppeld aan de desbetreffende Pokémon. Dit is hetzelfde gegaan als de images.
```
public void playSound(){
        PokeIDs.put("bulbasaur", 1);
        PokeIDs.put("ivysaur", 2);
        PokeIDs.put("venusaur", 3);
...
        PokeIDs.put("mewtwo", 150);
        PokeIDs.put("mew", 151);
```
Om de geluiden vervolgens daadwerkelijk af te spelen, gebruiken we een simpele *MediaPlayer*.
```
TextView pokedetails_name = getView().findViewById(R.id.textName);
        Integer sound_filename = PokeIDs.get(pokedetails_name.getText());
        String textviewID = "s_" + sound_filename;
        int id = getContext().getResources().getIdentifier(textviewID, "raw", getContext().getPackageName());

        MediaPlayer mPlayer = MediaPlayer.create(getContext(), id);
        mPlayer.setVolume(0.1f, 0.1f);
        mPlayer.start();
```
Tijdens het afspelen van dit geluidseffect, is er ook een imageshake aan de gang. Ook dit is redelijk simpel, direct onder de *MediaPlayer* toepassing gecodeerd.
```
        Animation shake;
        shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        ImageView image;
        image = (ImageView) getView().findViewById(R.id.imageView);
        image.startAnimation(shake);
```
De implementatie van de *FavoriteButton* is wat ingewikkelder. Als eerst slaan we elke statistiek en variabele van een Pokémon op in de daarvoor aangewezen variabelen.
```
        MaterialFavoriteButton favorite = getView().findViewById(R.id.Favorite_Button);
        List<String> pokemon = new ArrayList<String>();
        String[] splittedString = PokeStats.split(", ");
        String name =  splittedString[0];
        String hp = splittedString[3];
        String atk = splittedString[4];
        String spatk = splittedString[5];
        String def = splittedString[6];
        String spdef = splittedString[7];
        String spd = splittedString[8];
        String weight = splittedString[9];
        String height = splittedString[10];
        String type1 = splittedString[1];
        String type2 = splittedString[2];
```
Wanneer er de *FavoriteButton* van state veranderd, kan het twee richtingen op zijn gegaan; een Pokémon is gefavorite of een Pokémon is geunfavorite. Om het grafiek en statistieken scherm in te kunnen vullen, zijn de totale waarde van de statistieken en de frequentie van de gefavoriten types nodig. Als een Pokémon wordt gefavorite, zullen bij dit totaal dus waarden moeten worden opgeteld.
```
        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
            if(favorite){
                    FavoritesFragment.FavCounter++;
                    AllFavs.add(pokemon);
                    System.out.println(FavoritesFragment.FavCounter);

                    totalHP = totalHP + parseInt(hp);
                    totalATK = totalATK + parseInt(atk);
                    totalSPATK = totalSPATK + parseInt(spatk);
                    totalDEF = totalDEF + parseInt(def);
                    totalSPDEF = totalSPDEF + parseInt(spdef);
                    totalSPD = totalSPD + parseInt(spd);

                    if (type1.equals("grass")) {
                        totalTypeGrass = totalTypeGrass +=1;
                    }
                    else if (type1.equals("fire")) {
                        totalTypeFire = totalTypeFire +=1;
                    }
                    else if (type1.equals("water")) {
                        totalTypeWater = totalTypeWater +=1;
                    }
                    else if (type1.equals("electric")) {
                        totalTypeElectric = totalTypeElectric +=1;
                    }
                    else if (type1.equals("poison")) {
                        totalTypePoison = totalTypePoison +=1;
                    }

                    if (type2.equals("grass")) {
                        totalTypeGrass = totalTypeGrass +=1;
                    }
                    else if (type2.equals("fire")) {
                        totalTypeFire = totalTypeFire +=1;
                    }
                    else if (type2.equals("water")) {
                        totalTypeWater = totalTypeWater +=1;
                    }
                    else if (type2.equals("electric")) {
                        totalTypeElectric = totalTypeElectric +=1;
                    }
                    else if (type2.equals("poison")) {
                        totalTypePoison = totalTypePoison +=1;
                    }
            }
```
Als een Pokémon geunfavorite wordt, zullen bij het totaal dus waarden moeten worden afgeteld.
```
            else{
                    AllFavs.remove(pokemon);
                    System.out.println("This is allfavs: " + AllFavs);
                    FavoritesFragment.FavCounter--;
                    System.out.println(FavoritesFragment.FavCounter);

                    totalHP = totalHP - parseInt(hp);
                    totalATK = totalATK - parseInt(atk);
                    totalSPATK = totalSPATK - parseInt(spatk);
                    totalDEF = totalDEF - parseInt(def);
                    totalSPDEF = totalSPDEF - parseInt(spdef);
                    totalSPD = totalSPD - parseInt(spd);

                    if (type1.equals("grass")) {
                        totalTypeGrass = totalTypeGrass -=1;
                    }
                    else if (type1.equals("fire")) {
                        totalTypeFire = totalTypeFire -=1;
                    }
                    else if (type1.equals("water")) {
                        totalTypeWater = totalTypeWater -=1;
                    }
                    else if (type1.equals("electric")) {
                        totalTypeElectric = totalTypeElectric -=1;
                    }
                    else if (type1.equals("poison")) {
                        totalTypePoison = totalTypePoison -=1;
                    }

                    if (type2.equals("grass")) {
                        totalTypeGrass = totalTypeGrass -=1;
                    }
                    else if (type2.equals("fire")) {
                        totalTypeFire = totalTypeFire -=1;
                    }
                    else if (type2.equals("water")) {
                        totalTypeWater = totalTypeWater -=1;
                    }
                    else if (type2.equals("electric")) {
                        totalTypeElectric = totalTypeElectric -=1;
                    }
                    else if (type2.equals("poison")) {
                        totalTypePoison = totalTypePoison -=1;
                    }
```

----------

## Graphs
Deze fragment bevat een tweetal grafieken met daarin statestieken over je favorites. Klik op de GIF hieronder voor een demo:

[<img src="graphs.gif" width="225" height="475"/>](https://www.youtube.com/watch?v=q6owxm8XAwI)
### XML
De XML in de graph fragment geeft weer waar de grafieken moeten staan. Ook de titels van deze grafieken worden hier geplaatst. Binnen de root *FrameLayout* staan twee child *FrameLayout*'s. In elke *FrameLayout* zit een *TextView* object en een *GraphView* object. Het *GraphView* object wordt verder hieronder beschreven in het JAVA gedeelte van de documentatie. De XML is dus redelijk simpel en spreekt goed voor zich.
```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:translationY="-50px"
    tools:context=".ui.graphs.GraphFragment">


    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginBottom="210dp">

        <TextView
            android:id="@+id/textView2"
            android:layout_width="400dp"
            android:layout_height="70dp"
            android:layout_marginLeft="80dp"
            android:layout_marginTop="300dp"
            android:text="Favorites; Frequent Types"
            android:textColor="@color/white"
            android:textSize="24dp"
            android:textStyle="bold"
            android:translationY="-600px" />

        <com.jjoe64.graphview.GraphView
            android:id="@+id/graph"
            android:layout_width="350dp"
            android:layout_height="200dp"
            android:layout_gravity="center"
            android:paddingBottom="90dp"
            android:translationY="-50px" />

    </FrameLayout>

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="330dp">

        <TextView
            android:id="@+id/textView"
            android:layout_width="400dp"
            android:layout_height="70dp"
            android:layout_marginLeft="80dp"
            android:layout_marginTop="20dp"
            android:text="Favorites; Average Stats"
            android:textColor="@color/white"
            android:textSize="24dp"
            android:textStyle="bold" />

        <com.jjoe64.graphview.GraphView
            android:id="@+id/graph2"
            android:layout_width="350dp"
            android:layout_height="200dp"
            android:layout_gravity="center"
            android:translationY="-50px" />

    </FrameLayout>

</FrameLayout>
```
### JAVA
De waarden die gebruikt worden voor de grafieken in dit scherm zijn afkomstig van *Pokedetails*. We beginnen dus eerst met het importeren van deze waarden naar dit fragment.
```
    public void initiateVars() {
        totalHP = PokeDetails.totalHP;
        totalATK = PokeDetails.totalATK;
        totalSPATK = PokeDetails.totalSPATK;
        totalDEF = PokeDetails.totalDEF;
        totalSPDEF = PokeDetails.totalSPDEF;
        totalSPD = PokeDetails.totalSPD;

        totalTypeGrass = PokeDetails.totalTypeGrass;
        totalTypeFire = PokeDetails.totalTypeFire;
        totalTypeWater = PokeDetails.totalTypeWater;
        totalTypeElectric = PokeDetails.totalTypeElectric;
        totalTypePoison = PokeDetails.totalTypePoison;
    }
```
De methoden om de grafieken te plotten hebben we geimporteerd uit een GitHub Repository; [jjoe64 / GraphView](https://github.com/jjoe64/GraphView). Onze implementatie van deze library begint bij de OnCreate van dit scherm. De datapunten per grafiek worden ingevuld middels de geimporteerde waarden die hierboven beschreven zijn. De labels van deze grafieken zijn verder hardcoded. De rest van de regels code bestaat vooral uit de opmaak van de grafieken. Voor meer informatie over de toepassing van deze library verwijzen we graag door naar de desbetreffende library.
```
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initiateVars();
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        GraphView graph2 = view.findViewById(R.id.graph2);
        GraphView graph = view.findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, totalHP),
                new DataPoint(1, totalATK),
                new DataPoint(2, totalSPATK),
                new DataPoint(3, totalDEF),
                new DataPoint(4, totalSPDEF),
                new DataPoint(5, totalSPD)
        });
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, totalTypeGrass),
                new DataPoint(1, totalTypeFire),
                new DataPoint(2, totalTypeWater),
                new DataPoint(3, totalTypeElectric),
                new DataPoint(4, totalTypePoison)
        });
        graph2.addSeries(series2);
        graph.addSeries(series);

        series2.setSpacing(40);
        series.setSpacing(40);
        graph2.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph2.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph2.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        series2.setColor(Color.rgb(149, 23, 34));
        series.setColor(Color.rgb(149, 23, 34));

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph2);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"HP", "ATK", "SP. ATK", "DEF", "SP. DEF", "SPD"});
        graph2.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        StaticLabelsFormatter staticLabelsFormatter2 = new StaticLabelsFormatter(graph);
        staticLabelsFormatter2.setHorizontalLabels(new String[] {"Grass", "Fire", "Water", "Electric", "Poison"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter2);

        return view;
    }
```
----------


# Databases
## Lokale database
De lokale database bestaat uit twee delen:
- Database info
Geeft de structuur weer van de database, welke tabellen, kolommmen en datatypes. Zoals in de code snippet hieronder te zien is:
```
public class DatabaseInfo {

    public class PokemonTable {
        public static final String POKEMONTABLE = "PokemonTable";   // NAAM VAN JE TABEL
    }

    public class PokemonTable_Columns {
        public static final String Name = "Name";
        public static final String type1 = "Type1";
        public static final String type2 = "Type2";
        public static final String HP = "HP";
        public static final String ATK = "ATK";
        public static final String SP_ATK = "SP_ATK";
        public static final String DEF = "DEF";
        public static final String SP_DEF = "SP_DEF";
        public static final String SPD = "SPD";
        public static final String Weight = "Weight";
        public static final String Height = "Height";}

}
```

- Database helper
Dit gedeelte geeft hulpfunties om uberhaupt met de database te kunnen interacteren. Een voorbeeldfunctie die hierin verwerkt zit is bijvoorbeeld de oncreate functie. Deze zorgt ervoor dat de database wordt aangemaakt:
 
``` 
    @Override										// Maak je tabel met deze kolommen
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseInfo.PokemonTable.POKEMONTABLE
                + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseInfo.PokemonTable_Columns.Name + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.type1 + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.type2 + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.HP + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.ATK + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.SP_ATK + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.DEF + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.SP_DEF + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.SPD + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.Weight + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.Height + " TEXT);"
        );
    }
```

Daarnaast zijn er nog een aantal functies uitgeschreven om bijvoorbeeld zelf samengestelde queries uit te kunnen voeren op de database, of te kijken hoeveel rijen er in een tabel zitten:
```
    public Cursor query(String table, String[] columns, String selection, String[] selectArgs, String groupBy, String having, String orderBy){
        return mSQLDB.query(table, columns, selection, selectArgs, groupBy, having, orderBy);
    }

    public long getProfilesCount(String TABLE_NAME) {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        //db.close();
        System.out.println(count);
        return count;
    }

```

## Firebase
In de Firebase Realtime Database slaan wij de gegevens op van Pokémon die gefavorite zijn. Wanneer deze geunfavorite worden, worden de desbetreffende records uiteraard uit de firebase verwijderd. Het inserten en verwijderen van deze records gebeurd in het scherm *Pokedetails*. Wanneer de *FavoriteButton* wordt ingedrukt waarna de state van de button veranderd naar True, wordt er een Firebase connectie geopend en wordt er naar de Realtime database geschreven.
```
FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference myRef = database.getReference(name);

myRef.setValue(parseInt(hp) + ", " + parseInt(atk) + ", " + parseInt(spatk) + ", " + parseInt(def) + ", " + parseInt(spdef) + ", " + type1 + ", " + type2);
```
Wanneer de *FavoriteButton* wordt ingedrukt waarna de state van de button veranderd naar False, wordt er een Firebase connectie geopend en wordt er van de Realtime database verwijderd.
```
FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference myRef = database.getReference(name);

myRef.removeValue();
```
De records in de Firebase Realtime database zien er als volgt uit:

<img src="https://i.imgur.com/35Gw1J6.png" width="945" height="485"/>

Deze database wordt realtime bijgehouden. Dit betekend dat als er naartoe wordt geschreven, dit direct te zien is in de console van de firebase. Hetzelfde geldt voor wanneer records verwijderd worden uit de firebase.
De database zal tijdens de test en beoordelingsfase staan op *public*. Dit betekend dat iedereen naar de database kan schrijven en van de database kan lezen. Dit wordt beschreven in de regels van de realtime database.

<img src="https://i.imgur.com/6XO7YxU.png" width="289" height="119"/>

Buiten de test en beoordelingsfase zal de realtime database alleen toegankelijk zijn tot authenticated users.

<img src="https://i.imgur.com/t5GudKN.png" width="350" height="118"/>

----------

# Assets
## Geluiden
Iedere pokemon ken een zogenaamde pokemon cry. Pikachu: "Pika Pika!!!". Al deze cries voor first gen pokemen zijn van een website gehaald die in de bronnen is terug te vinden. Die files kwamen in OGG formaat, dus ik heb ze met een online converter omgezet naar MP3 (I know, ik kon het scripten maar de converter liet mij per 25 files converten. Dus was sneller op deze manier). Vervolgens heb ik ervoor gezorgd dat de sounds in res.raw terecht zijn gekomen, in pokedetails onstart animatie+sound afspeelt:
```
        //// AYOOOOO DJ, SPIN THAT SHIT
        TextView pokedetails_name = getView().findViewById(R.id.textName);
        Integer sound_filename = PokeIDs.get(pokedetails_name.getText());
        String textviewID = "s_" + sound_filename;
        int id = getContext().getResources().getIdentifier(textviewID, "raw", getContext().getPackageName());

        MediaPlayer mPlayer = MediaPlayer.create(getContext(), id);
        mPlayer.setVolume(0.1f, 0.1f);
        mPlayer.start();


        //// AND SHAKE THAT BOOTYYYY
        Animation shake;
        shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        ImageView image;
        image = (ImageView) getView().findViewById(R.id.imageView);
        image.startAnimation(shake);
```

## Afbeeldingen
Gedownload vanaf een website die staat in de bronnen. Verwerkt in res.Drawables en op een naar mijn mening vreselijke manier verwerkt (maar goed, hoe meer tijd ik hiervoor krijg des te mooier het eruit gaat zien):
```
        //set images
        PokeIDs.put("bulbasaur", 1);
        PokeIDs.put("ivysaur", 2);
        PokeIDs.put("venusaur", 3);
        PokeIDs.put("charmander", 4);
        PokeIDs.put("charmeleon", 5);
        PokeIDs.put("charizard", 6);
        PokeIDs.put("squirtle", 7);
        PokeIDs.put("wartortle", 8);
        PokeIDs.put("blastoise", 9);
        PokeIDs.put("caterpie", 10);
        PokeIDs.put("metapod", 11);
        PokeIDs.put("butterfree", 12);
        PokeIDs.put("weedle", 13);
        PokeIDs.put("kakuna", 14);
        PokeIDs.put("beedrill", 15);
        PokeIDs.put("pidgey", 16);
        PokeIDs.put("pidgeotto", 17);
        PokeIDs.put("pidgeot", 18);
        PokeIDs.put("rattata", 19);
        PokeIDs.put("raticate", 20);
        PokeIDs.put("spearow", 21);
        PokeIDs.put("fearow", 22);
        PokeIDs.put("ekans", 23);
        PokeIDs.put("arbok", 24);
        PokeIDs.put("pikachu", 25);
        PokeIDs.put("raichu", 26);
        PokeIDs.put("sandshrew", 27);
        PokeIDs.put("sandslash", 28);
        PokeIDs.put("nidoran-f", 29);
        PokeIDs.put("nidorina", 30);
        PokeIDs.put("nidoqueen", 31);
        PokeIDs.put("nidoran-m", 32);
        PokeIDs.put("nidorino", 33);
        PokeIDs.put("nidoking", 34);
        PokeIDs.put("clefairy", 35);
        PokeIDs.put("clefable", 36);
        PokeIDs.put("vulpix", 37);
        PokeIDs.put("ninetales", 38);
        PokeIDs.put("jigglypuff", 39);
        PokeIDs.put("wigglytuff", 40);
        PokeIDs.put("zubat", 41);
        PokeIDs.put("golbat", 42);
        PokeIDs.put("oddish", 43);
        PokeIDs.put("gloom", 44);
        PokeIDs.put("vileplume", 45);
        PokeIDs.put("paras", 46);
        PokeIDs.put("parasect", 47);
        PokeIDs.put("venonat", 48);
        PokeIDs.put("venomoth", 49);
        PokeIDs.put("diglett", 50);
        PokeIDs.put("dugtrio", 51);
        PokeIDs.put("meowth", 52);
        PokeIDs.put("persian", 53);
        PokeIDs.put("psyduck", 54);
        PokeIDs.put("golduck", 55);
        PokeIDs.put("mankey", 56);
        PokeIDs.put("primeape", 57);
        PokeIDs.put("growlithe", 58);
        PokeIDs.put("arcanine", 59);
        PokeIDs.put("poliwag", 60);
        PokeIDs.put("poliwhirl", 61);
        PokeIDs.put("poliwrath", 62);
        PokeIDs.put("abra", 63);
        PokeIDs.put("kadabra", 64);
        PokeIDs.put("alakazam", 65);
        PokeIDs.put("machop", 66);
        PokeIDs.put("machoke", 67);
        PokeIDs.put("machamp", 68);
        PokeIDs.put("bellsprout", 69);
        PokeIDs.put("weepinbell", 70);
        PokeIDs.put("victreebel", 71);
        PokeIDs.put("tentacool", 72);
        PokeIDs.put("tentacruel", 73);
        PokeIDs.put("geodude", 74);
        PokeIDs.put("graveler", 75);
        PokeIDs.put("golem", 76);
        PokeIDs.put("ponyta", 77);
        PokeIDs.put("rapidash", 78);
        PokeIDs.put("slowpoke", 79);
        PokeIDs.put("slowbro", 80);
        PokeIDs.put("magnemite", 81);
        PokeIDs.put("magneton", 82);
        PokeIDs.put("farfetchd", 83);
        PokeIDs.put("doduo", 84);
        PokeIDs.put("dodrio", 85);
        PokeIDs.put("seel", 86);
        PokeIDs.put("dewgong", 87);
        PokeIDs.put("grimer", 88);
        PokeIDs.put("muk", 89);
        PokeIDs.put("shellder", 90);
        PokeIDs.put("cloyster", 91);
        PokeIDs.put("gastly", 92);
        PokeIDs.put("haunter", 93);
        PokeIDs.put("gengar", 94);
        PokeIDs.put("onix", 95);
        PokeIDs.put("drowzee", 96);
        PokeIDs.put("hypno", 97);
        PokeIDs.put("krabby", 98);
        PokeIDs.put("kingler", 99);
        PokeIDs.put("voltorb", 100);
        PokeIDs.put("electrode", 101);
        PokeIDs.put("exeggcute", 102);
        PokeIDs.put("exeggutor", 103);
        PokeIDs.put("cubone", 104);
        PokeIDs.put("marowak", 105);
        PokeIDs.put("hitmonlee", 106);
        PokeIDs.put("hitmonchan", 107);
        PokeIDs.put("lickitung", 108);
        PokeIDs.put("koffing", 109);
        PokeIDs.put("weezing", 110);
        PokeIDs.put("rhyhorn", 111);
        PokeIDs.put("rhydon", 112);
        PokeIDs.put("chansey", 113);
        PokeIDs.put("tangela", 114);
        PokeIDs.put("kangaskhan", 115);
        PokeIDs.put("horsea", 116);
        PokeIDs.put("seadra", 117);
        PokeIDs.put("goldeen", 118);
        PokeIDs.put("seaking", 119);
        PokeIDs.put("staryu", 120);
        PokeIDs.put("starmie", 121);
        PokeIDs.put("mr-mime", 122);
        PokeIDs.put("scyther", 123);
        PokeIDs.put("jynx", 124);
        PokeIDs.put("electabuzz", 125);
        PokeIDs.put("magmar", 126);
        PokeIDs.put("pinsir", 127);
        PokeIDs.put("tauros", 128);
        PokeIDs.put("magikarp", 129);
        PokeIDs.put("gyarados", 130);
        PokeIDs.put("lapras", 131);
        PokeIDs.put("ditto", 132);
        PokeIDs.put("eevee", 133);
        PokeIDs.put("vaporeon", 134);
        PokeIDs.put("jolteon", 135);
        PokeIDs.put("flareon", 136);
        PokeIDs.put("porygon", 137);
        PokeIDs.put("omanyte", 138);
        PokeIDs.put("omastar", 139);
        PokeIDs.put("kabuto", 140);
        PokeIDs.put("kabutops", 141);
        PokeIDs.put("aerodactyl", 142);
        PokeIDs.put("snorlax", 143);
        PokeIDs.put("articuno", 144);
        PokeIDs.put("zapdos", 145);
        PokeIDs.put("moltres", 146);
        PokeIDs.put("dratini", 147);
        PokeIDs.put("dragonair", 148);
        PokeIDs.put("dragonite", 149);
        PokeIDs.put("mewtwo", 150);
        PokeIDs.put("mew", 151);

        ImageView imageView = root.findViewById(R.id.imageView);
        imageView.setImageResource(pictureArray[PokeIDs.get(pokedetails_name.getText())-1]);
 ```       

## Kleuren
De meeste kleuren die met de app te maken hebben staan in colors.xml binnen res.values. En anders worden ze op een manier gebruikt zoals in de snippet hieronder te zien is: 
```
                String SPATK = "spatk" + e;
                int spatkID = getResources().getIdentifier(SPATK, "id", getActivity().getPackageName());
                ProgressBar pokespatk = v.findViewById(spatkID);
                String spatk = SplittedString[3];
                //System.out.println("SPATK: " + name + spatk);
                pokespatk.setMax(194);
                pokespatk.setProgress(parseInt(spatk));
                pokespatk.setProgressTintList(ColorStateList.valueOf(parseColor("#2196F3")));
```

## Libraries

Database:
```
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
```

GraphFragment:
```
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.ikpmd_periode2.PokeDetails;
import com.example.ikpmd_periode2.R;
import com.example.ikpmd_periode2.ui.favorites.FavoritesFragment;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
```

Mainactivity:
```
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.ikpmd_periode2.database.DatabaseHelper;
import com.example.ikpmd_periode2.database.DatabaseInfo;
import com.example.ikpmd_periode2.ui.LoadingDBFragment;
import com.example.ikpmd_periode2.ui.favorites.FavoritesFragment;
import com.example.ikpmd_periode2.ui.graphs.GraphFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import me.sargunvohra.lib.pokekotlin.model.PokemonStat;
import me.sargunvohra.lib.pokekotlin.model.PokemonType;
```

PokedetailsFragment:
```
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.example.ikpmd_periode2.ui.favorites.FavoritesFragment;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static java.lang.Integer.parseInt;
```

FavoritesFragment:
```
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.ikpmd_periode2.PokeDetails;
import com.example.ikpmd_periode2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import static android.graphics.Color.parseColor;
import static java.lang.Integer.parseInt;
```

## Maven
Er is een repo toegevoegd en een aantal dependencies aan het build script vanwege de favoritebutton en firebase:
```
repositories {
    mavenCentral()
    maven { url 'https://dl.bintray.com/sargunv/maven' }
    maven { url 'https://jitpack.io' }
    google()
    jcenter()
}

dependencies {
    implementation platform('com.google.firebase:firebase-bom:26.1.1')
    implementation 'com.google.firebase:firebase-database:19.5.1'
    implementation 'com.google.firebase:firebase-analytics'
    implementation 'me.sargunvohra.lib:pokekotlin:2.3.1'
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'com.google.android.material:material:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'androidx.navigation:navigation-fragment:2.3.1'
    implementation 'androidx.navigation:navigation-ui:2.3.1'
    implementation 'com.github.ivbaranov:materialfavoritebutton:0.1.5'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.2.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0'
    implementation 'androidx.gridlayout:gridlayout:1.0.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'com.google.firebase:firebase-database:19.3.0'
    implementation 'com.github.PhilJay:MPAndroidChart:v3.0.3'
    implementation 'com.jjoe64:graphview:4.2.2'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
    implementation 'com.github.iamBedant:OutlineTextView:1.0.5'
    implementation 'com.google.android.material:material:1.1.0'
}
```

----------
####
