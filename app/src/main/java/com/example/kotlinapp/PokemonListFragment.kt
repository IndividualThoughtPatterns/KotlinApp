package com.example.kotlinapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.FragmentMainBinding
import java.io.IOException
import java.util.concurrent.Executors

class PokemonListFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val executor = Executors.newSingleThreadExecutor()
    private var favoritePokemonDao: FavoritePokemonDao? = null
    private var favoritePokemonListLiveData: LiveData<List<FavoritePokemon>>? = null
    private val pokemonNetwork = App.instance.pokemonNetwork
    private val limit = 20
    private var offsetFactor = 0
    private var offset = limit * offsetFactor
    private var pokemonList = MutableLiveData<List<Pokemon>>()
    private var pokemonItems = mutableListOf<PokemonItem>()
    private var adapter: PokemonAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        favoritePokemonDao = App.instance.db.favoritePokemonDao()

        val favoritePokemonListObserver = Observer<List<FavoritePokemon>> { favoritePokemonList ->
            val favoritePokemonNames = favoritePokemonList.map {it.name}
            pokemonList.value!!.forEach {
                it.isFavorite = it.name in favoritePokemonNames
            }

            pokemonItems = pokemonList.value!!.map { it ->
                PokemonItem(
                    sprite = it.sprite,
                    name = it.name,
                    isFavorite = it.isFavorite
                )
            }.toMutableList()

            adapter!!.setPokemons(
                pokemonItems
            )
        }

        val pokemonListObserver = Observer<List<Pokemon>> {
            favoritePokemonListLiveData = favoritePokemonDao!!.getAll()
            favoritePokemonListLiveData!!.observe(viewLifecycleOwner, favoritePokemonListObserver)
        }

        pokemonList.observe(viewLifecycleOwner, pokemonListObserver)

        executor.submit {
            try {
                pokemonList.postValue(pokemonNetwork.getPokemons(limit,offset))
            } catch (e: IOException) {
                handleNetworkError()
            }
        }

        adapter = PokemonAdapter(
            onPokemonClick = { pokemonItem: PokemonItem ->
                val bundle = Bundle()

                var pokemon: Pokemon? = null
                pokemonList.value!!.forEach {
                    if (it.name == pokemonItem.name) {
                        pokemon = it
                    }
                }

                bundle.putSerializable("pokemon", pokemon)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment, PokemonInfoFragment::class.java, bundle)
                    .addToBackStack("PokemonListFragment").commit()
            },
            onIsFavoriteClick = { pokemonItem: PokemonItem ->
                executor.submit {
                    if (pokemonItem.isFavorite) {
                        favoritePokemonDao!!.deleteByName(pokemonItem.name)
                    } else {
                        favoritePokemonDao!!.insert(FavoritePokemon(pokemonItem.name))
                    }

                    favoritePokemonListLiveData = favoritePokemonDao!!.getAll()
                }
            }
        )

        adapter!!.setPokemons(pokemonItems)
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

                        executor.submit {
                            try {
                                val pokemonListUpdated = (pokemonList.value as MutableList)
                                pokemonListUpdated.addAll(pokemonNetwork.getPokemons(limit,offset))
                                pokemonList.postValue(pokemonListUpdated)
                            } catch (e: IOException) {
                                handleNetworkError()
                            }
                        }
                    }
                }
            }
        )
    }

    private fun handleNetworkError() {
        requireActivity().runOnUiThread {
            Toast.makeText(requireActivity(), "Ошибка сети", Toast.LENGTH_LONG).show()
        }
    }
}