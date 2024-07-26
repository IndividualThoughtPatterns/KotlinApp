package com.example.kotlinapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.FragmentMainBinding
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager


class PokemonListFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val adapter: PokemonAdapter = PokemonAdapter(onPokemonClick = {
            pokemonName: String ->
            val bundle = Bundle()

            bundle.putString("name", pokemonName)

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, PokemonInfoFragment::class.java, bundle)
                .addToBackStack("PokemonListFragment")
                .commit()
        } )

        val limit: Int = 20

        PokemonsNetwork().getPokemons(
            limit,
            onResult = { pokemons ->
                requireActivity().runOnUiThread( ){
                    recyclerView.adapter = adapter
                    adapter.setPokemons(pokemons)
                }
            },
            onError = {
                requireActivity().runOnUiThread( ) {
                    Toast.makeText(requireActivity(), "Ошибка сети", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = PokemonListFragment()
    }
}