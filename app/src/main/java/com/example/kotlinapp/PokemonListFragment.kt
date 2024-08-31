package com.example.kotlinapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.kotlinapp.databinding.FragmentMainBinding
import java.util.concurrent.Executors

class PokemonListFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val executor = Executors.newSingleThreadExecutor()
    private val pokemonsNetwork = PokemonsNetwork()
    private val limit = 20
    private var offsetFactor = 0
    private var offset = limit * offsetFactor
    private var pokemonList = mutableListOf<Pokemon>()
    private var adapter: PokemonAdapter? = null
    private var db: AppDatabase? = null
    private var favoritePokemonDao: FavoritePokemonDao? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java,
            "favorite_pokemons.db"
        ).build()

        favoritePokemonDao = db!!.favoritePokemonDao()

        adapter = PokemonAdapter(
            onPokemonClick = { pokemon: Pokemon ->
                val bundle = Bundle()
                bundle.putSerializable("pokemon", pokemon)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment, PokemonInfoFragment::class.java, bundle)
                    .addToBackStack("PokemonListFragment").commit()
            },
            onIsFavoriteClick = { pokemon: Pokemon ->
                executor.submit {
                    if (pokemon.isFavorite) {
                        pokemon.isFavorite = false
                        favoritePokemonDao!!.deleteByName(pokemon.name)
                    } else {
                        pokemon.isFavorite = true
                        favoritePokemonDao!!.insert(FavoritePokemon(pokemon.name))
                    }
                }

                adapter!!.notifyDataSetChanged()
            })
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1) &&
                        newState==RecyclerView.SCROLL_STATE_IDLE)
                    {
                        Toast.makeText(context, "загрузка...", Toast.LENGTH_LONG).show()

                        offsetFactor++
                        offset = limit * offsetFactor

                        getPokemons()
                    }
                }
            }
        )

        getPokemons()
    }

    private fun getPokemons() {
        executor.submit {
            try {
                pokemonList.addAll(pokemonsNetwork.getPokemons(limit, offset))

                val favoritePokemonNames = favoritePokemonDao!!.getAll().map {it.name}
                pokemonList.forEach {
                    if (it.name in favoritePokemonNames) {
                        it.isFavorite = true
                    }
                }

                requireActivity().runOnUiThread {
                    adapter?.setPokemons(pokemonList)
                }

            } catch (e: Exception) {
                handleNetworkError()
            }
        }
    }

    private fun handleNetworkError() {
        requireActivity().runOnUiThread() {
            Toast.makeText(requireActivity(), "Ошибка сети", Toast.LENGTH_LONG).show()
        }
    }
}