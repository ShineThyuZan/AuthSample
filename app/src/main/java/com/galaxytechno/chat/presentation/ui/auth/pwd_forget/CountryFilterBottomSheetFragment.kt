package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.galaxytechno.chat.databinding.CountryFilterBottomSheetBinding
import com.galaxytechno.chat.model.dto.Country

import com.galaxytechno.chat.presentation.base.BaseBottomSheet
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CountryFilterBottomSheetFragment :
    BaseBottomSheet<CountryFilterBottomSheetBinding>(CountryFilterBottomSheetBinding::inflate),
    CountrySelectedDelegate {

    private val viewModelAuth: AuthViewModel by activityViewModels()
    private lateinit var countryAdapter: CountrySelectedAdapter
    private var mCountryList: MutableList<Country> = mutableListOf()

    override fun setupView() {
        super.setupView()
        dialog?.setCanceledOnTouchOutside(true)
        shouldShowShimmer(true)
    }

    private fun shouldShowShimmer(flag: Boolean) {
        if (flag) {
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.rvCountry.visibility = View.GONE
            binding.shimmerLayout.startShimmer()
        } else {
            binding.shimmerLayout.visibility = View.GONE
            binding.rvCountry.visibility = View.VISIBLE
            binding.shimmerLayout.stopShimmer()
        }
    }

    private fun setUpCountryListRecyclerView() {
        countryAdapter =
            CountrySelectedAdapter(requireContext(), this@CountryFilterBottomSheetFragment)
        binding.rvCountry.apply {
            this.adapter = countryAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    override fun onClickSelectedCountry(data: Country) {
//        requireContext().displayToast(data.country)
        viewModelAuth.setSelectedCountry(data)
        findNavController().popBackStack()
    }

    override fun observe() {
        super.observe()

        viewModelAuth.countryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is RemoteEvent.SuccessEvent -> {
                    shouldShowShimmer(false)
                    setUpCountryListRecyclerView()
                    mCountryList = it.data!!.data?.countryList as MutableList<Country>
                    countryAdapter.setNewData(mCountryList)
                }
                else -> {
                    Toast.makeText(context, "fail ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}