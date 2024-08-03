package com.example.kotlinapp

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.FragmentMainBinding
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.Serializable


class PokemonListFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val adapter: PokemonAdapter = PokemonAdapter(onPokemonClick = { pokemon: Pokemon ->
            val bundle = Bundle()

            bundle.putString("name", pokemon.name)
            bundle.putString("sprite", pokemon.sprite)
            bundle.putString("height", pokemon.height)
            bundle.putString("weight", pokemon.weight)
            bundle.putString("hp", pokemon.hp)
            bundle.putString("defense", pokemon.defense)
            bundle.putString("attack", pokemon.attack)
            bundle.putString("speed", pokemon.speed)
            bundle.putSerializable("types", pokemon.types as Serializable)
            bundle.putSerializable("abilities", pokemon.abilities as Serializable)

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, PokemonInfoFragment::class.java, bundle)
                .addToBackStack("PokemonListFragment")
                .commit()
        })

        val limit: Int = 20

        PokemonsNetwork().getPokemonNames(
            limit,
            onResult = { pokemonNames ->
                var pokemons: MutableList<Pokemon> = mutableListOf<Pokemon>()
                var counter = 1
                val size = pokemonNames.size

                pokemonNames.forEach {
                    PokemonsNetwork().getPokemon(
                        it,
                        onResult = { pokemon ->
                            pokemons.add(pokemon)
                            counter++

                            if (counter == pokemonNames.size) {
                                requireActivity().runOnUiThread() {
                                    recyclerView.adapter = adapter
                                    adapter.setPokemons(pokemons)
                                }
                            }
                        },
                        onError = {
                            handleNetworkError()
                        }
                    )
                }
            },
            onError = {
                handleNetworkError()
            }
        )
    }

    private fun handleNetworkError() {
        requireActivity().runOnUiThread() {
            Toast.makeText(requireActivity(), "Ошибка сети", Toast.LENGTH_LONG).show()
        }
    }
}