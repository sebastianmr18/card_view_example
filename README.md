# 📱 card_view_app

> Esta es una aplicación sencilla donde se prueba funcionalidades de Kotlin, en este caso el funcionamiento de RecyclerView con Card View.

EL primer paso es agregar las siguientes dependencias en el build.gradel.kts de la aplicación.
```bash
dependencies {
    val navVersion = "2.7.3"
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation ("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-common:$navVersion")

    //cardView
    implementation("androidx.cardview:cardview:1.0.0")
    //RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.1")

}
```


---

## Instrucciones

A continuación, se explica como implementar las distintas funcionalidades.

### Recycler View:
<b>RecyclerView</b> es un componente de Android que se utiliza para mostrar listas de datos de manera eficiente en las aplicaciones. 
Es una versión mejorada del <b>ListView</b> y se usa comúnmente cuando tienes una gran cantidad de datos que necesitan ser mostrados 
de manera eficiente y fluida, permitiendo el reciclaje de vistas, de ahí su nombre. 

Para este ejemplo se crea una clase <b>SuperHero</b>
```bash
<!--SuperHero.kt-->
package com.example.class_7

data class SuperHero(val name: String, val power: String, val imageId: Int)
```
Necesitamos crear las Card View que contienen la informaciòn, creamos el siguiente xml:
```bash
<!--item_super_hero.xml-->
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android">
    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp">

        <androidx.cardview.widget.CardView
            xmlns:card_view="http://schemas.android.com/apk/res-auto"
            android:id="@+id/cardViewSuperHeroes"
            android:layout_gravity="center"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:foreground="?selectableItemBackground"
            card_view:cardCornerRadius="8dp"
            android:elevation="8dp"
            card_view:layout_constraintStart_toStartOf="parent"
            card_view:layout_constraintTop_toTopOf="parent"
            card_view:layout_constraintEnd_toEndOf="parent"
            >

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:padding="16dp">

                <ImageView
                    android:id="@+id/imageViewSuperHero"
                    android:layout_width="80dp"
                    android:layout_height="120dp"
                    android:scaleType="centerCrop"
                    android:src="@drawable/superman"/>

                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="match_parent"
                    android:layout_weight="1"
                    android:orientation="vertical"
                    android:paddingStart="16dp">

                    <TextView
                        android:id="@+id/textViewSuperName"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Super Man"
                        android:textSize="18sp"
                        android:textStyle="bold"
                        android:layout_marginBottom="16dp"/>

                    <TextView
                        android:id="@+id/textViewSuperPower"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Fuerza sobrehumana, lo que le permite levantar objetos extremadamente pesados"
                        android:textSize="14sp"/>
                </LinearLayout>
            </LinearLayout>

        </androidx.cardview.widget.CardView>

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```
Ahora, creamos el fragment que va a recibir este item, a este se le llama el <b>RecyclerView</b>:
```bash
<!--fragment_recycler.xml-->
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".fragment.RecyclerFragment">

        <TextView
            android:id="@+id/textViewTitleRecycler"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="@string/welcomeRecycler"
            android:textSize="40sp"
            android:layout_marginTop="10dp"
            android:textStyle="bold"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <!--Aqui se crea el RecyclerView-->
        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerview"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            tools:listitem="@layout/item_super_hero"   <!--IMPORTANTE: pasarle el item aquì-->
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/textViewTitleRecycler"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"/>

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```
Ahora, debemos crear el <b>ViewHolder</b>, que se encarga de manejar y organizar los items en pantalla. 
En este caso tenemos unalista de superhéroes, cada superhéroe necesita un contenedor para mostrar su imagen, nombre y poder. 
El <b>ViewHolder</b> se encarga de mantener organizadas todas esas vistas (componentes) para cada ítem.
```bash
<!--RecyclerViewHolder.kt-->
package com.example.class_7

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.class_7.databinding.ItemSuperHeroBinding

// RecyclerViewHolder: Clase que gestiona la vista de un ítem (superhéroe) en el RecyclerView
class RecyclerViewHolder(binding: ItemSuperHeroBinding): RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding

    // Método para asignar la información del objeto SuperHeroe a las vistas correspondientes
    fun setHeroItem(superHero: SuperHero) {
        bindingItem.imageViewSuperHero.setImageResource(superHero.imageId)
        bindingItem.textViewSuperName.text = superHero.name
        bindingItem.textViewSuperPower.text = superHero.power

        // Configura un click listener para mostrar un mensaje cuando se haga clic en el ítem del superhéroe
        bindingItem.cvHeroes.setOnClickListener {
            // Muestra un Toast con el nombre del superhéroe
            Toast.makeText(it.context, "${superHeroe.nombre}", Toast.LENGTH_SHORT).show()
        }
    }
}
```
El siguiente paso es crear el <b>Adapter</b>, que actua como intermediario entre los datos y la vista.
Se encarga de tomar los datos de la lista y decirle al RecyclerView cómo mostrarlos en la pantalla.
```bash
<!--RecyclerAdapter.kt-->
package com.example.class_7

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.class_7.databinding.ItemSuperHeroBinding

// RecyclerAdapter: Adaptador para gestionar y mostrar una lista de objetos SuperHeroe en un RecyclerView.
class RecyclerAdapter(private val heroesList:MutableList<SuperHero>):
    RecyclerView.Adapter<RecyclerViewHolder>(){

    // Este método se llama cuando es necesario crear un nuevo ViewHolder (que representa un ítem visual en la lista).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflamos el layout del ítem (usamos ItemSuperHeroeBinding para asociarlo a la vista)
        val binding = ItemSuperHeroBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerViewHolder(binding)
    }

    // Este método devuelve el número total de elementos en la lista
    override fun getItemCount(): Int {
        return heroesList.size
    }

    // Este método vincula un objeto SuperHeroe con el ViewHolder para mostrar la información en la posición correcta
    override fun onBindViewHolder(
        recyclerViewHolder: RecyclerViewHolder,
        position: Int
    ) {
        // Obtenemos el SuperHeroe en la posición especificada
        val hero = listaHeroes[position]
        // Llamamos al método setItemHeroe para "pintar" o mostrar el SuperHeroe en pantalla usando el ViewHolder
        recyclerViewHolder.setItemHeroe(hero)
    }

}
```
Ahora, debemos crear la logica del Recyler, por ello en la siguiente ruta <b>app/src/main/java/com/example/clase7/fragment/</b>
creamos el archivo RecyclerFragment.kt
```bash
<!--RecyclerFragment.kt-->
package com.example.class_7.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.class_7.R
import com.example.class_7.RecyclerAdapter
import com.example.class_7.databinding.FragmentRecyclerBinding
import com.example.class_7.SuperHero

// RecyclerFragment: Fragmento encargado de mostrar una lista de superhéroes en un RecyclerView
class RecyclerFragment : Fragment() {
    // Variable para manejar la vista enlazada (binding) con FragmentRecycler
    private lateinit var binding: FragmentRecyclerBinding

    // Método para crear y configurar la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout usando el binding de FragmentRecycler y asignamos el lifecycleOwner
        binding = FragmentRecyclerBinding.inflate(inflater)
        binding.lifecycleOwner = this
        // Retornamos la vista principal del fragmento (binding.root)
        return binding.root
    }

    // Método que se ejecuta cuando la vista ya ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler()
    }

    // Función que configura el RecyclerView con los datos de los superhéroes
    fun recycler() {
        // Lista de superhéroes que se mostrará en el RecyclerView
        var heroesList = mutableListOf(
            SuperHero("SuperMan","Superman es conocido por su increíble fuerza física. Puede levantar objetos extremadamente pesados, desde automóviles hasta edificios.", R.drawable.superman),
            SuperHero("Hulk", "Radica en su capacidad para transformarse de un físico débil y tímido llamado Bruce Banner en un gigante verde y musculoso conocido como Hulk", R.drawable.hulk),
            SuperHero("Batman", "En lugar de depender de poderes extraordinarios, el superhéroe Batman confía en su inteligencia, entrenamiento físico y habilidades técnicas para combatir el crimen", R.drawable.batman),
            SuperHero("Capitan América","No posee poderes sobrenaturales, ha sido mejorado físicamente a través de un suero de súper soldado", R.drawable.capitan),
            SuperHero("Pantera Negra","Fuerza Sobrehumana, Sentidos Agudizados, Agilidad y Velocidad Mejoradas,Maestría en Artes Marciales", R.drawable.pantera),
            SuperHero("Spider-Man", "Sentido arácnido, agilidad", R.drawable.spiderman)
        )

        // Configuramos el RecyclerView con un layout vertical (LinearLayoutManager)
        val recycler = binding.recyclerview
        recycler.layoutManager = LinearLayoutManager(context)

        // Configuramos el adaptador con la lista de superhéroes y lo asignamos al RecyclerView
        val adapter = RecyclerAdapter(listaHeroes)
        recycler.adapter = adapter

        // Notificamos al adaptador que los datos han cambiado para que refresque la vista
        adapter.notifyDataSetChanged()
    }
}
```
