package com.bda.finance_test.ui.currencyPairs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bda.finance_test.R
import com.bda.finance_test.adapters.PairsAdapter
import com.bda.finance_test.databinding.FragmentCurrencyPairsBinding
import com.bda.finance_test.model.database.entity.CurrencyPair
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.item_pairs_adapter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CurrencyPairsFragment : Fragment(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val viewModel: CurrencyPairsViewModel by viewModel()
    private var _binding: FragmentCurrencyPairsBinding? = null
    private var pairsAdapter: PairsAdapter? = null

    private val binding
        get() = _binding!!

    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyPairsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initData()
        initPairAdapter()
    }

    private fun initData() {
        viewModel.pairsList.observe(viewLifecycleOwner, {
            if (it != null) pairsAdapter?.setPairList(it)
        })

        viewModel.fragmentType.value = CurrencyPairsViewModel.ALL_PAIRS
        binding.toolbar.title = getString(R.string.title_all)

        binding.searchEdit.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val search: String = s.toString()
                if (search.isNotEmpty() && search.length > 1) {
                    viewModel.searchPair(search.toUpperCase(Locale.getDefault()))
                } else {
                    viewModel.updatePairList()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        val navView: BottomNavigationView = binding.navView
        navView.setOnNavigationItemSelectedListener(this)
    }

    private fun initPairAdapter() {
        pairsAdapter = PairsAdapter(
            mutableListOf(),
            object : PairsAdapter.OnItemClickListener {
                override fun onItemClick(pair: CurrencyPair) {
                    val bundle = bundleOf("pair" to pair)
                    navController!!.navigate(
                        R.id.action_navigation_currency_pairs_to_navigation_chart,
                        bundle
                    )
                }

                override fun onStarClick(pair: CurrencyPair) {
                    pair.setFavorite(!pair.isFavorite())
                    viewModel.updatePair(pair)
                }

            });

        binding.pairsRV.apply {
            adapter = pairsAdapter
            layoutManager = LinearLayoutManager(this@CurrencyPairsFragment.context)
            setHasFixedSize(true)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fragmentType.value =
            if (binding.navView.selectedItemId == R.id.navigation_currency_pairs) {
                binding.searchEdit.visibility = View.VISIBLE
                0
            }else {
                binding.searchEdit.visibility = View.GONE
                1
            }
        binding.toolbar.title =
            if (viewModel.fragmentType.value == 0) getString(R.string.title_all)
            else getString(R.string.title_favorites)
        if (binding.searchEdit.visibility == View.VISIBLE && binding.searchEdit.text.length > 1) {
            viewModel.searchPair(binding.searchEdit.text.toString().toUpperCase(Locale.getDefault()))
        } else viewModel.updatePairList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.navigation_currency_pairs -> {
            Log.e("my_tag", "current")
            binding.toolbar.title = getString(R.string.title_all)
            viewModel.fragmentType.value = CurrencyPairsViewModel.ALL_PAIRS
            binding.searchEdit.visibility = View.VISIBLE
            if (binding.searchEdit.text.length > 1) {
                viewModel.searchPair(binding.searchEdit.text.toString().toUpperCase(Locale.getDefault()))
            } else viewModel.updatePairList()
            true
        }
        R.id.navigation_favorites -> {
            Log.e("my_tag", "favorite")
            binding.toolbar.title = getString(R.string.title_favorites)
            viewModel.fragmentType.value = CurrencyPairsViewModel.FAVORITES_PAIRS
            binding.searchEdit.visibility = View.GONE
            viewModel.updatePairList()
            true
        }
        else -> false
    }
}