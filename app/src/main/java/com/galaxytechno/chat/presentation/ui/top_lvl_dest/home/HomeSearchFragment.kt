package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentHomeSearchBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment.HomeFeatureViewModel
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber


class HomeSearchFragment :
    OtherLvlFragment<FragmentHomeSearchBinding>(FragmentHomeSearchBinding::inflate) {
    private val vm: HomeFeatureViewModel by activityViewModels()
    private var offset = 0
    private var limit = 30

    override fun initialize() {
        vm.setSearchText("")
        super.initialize()
        /** request focusing center of the search View */
        binding.svHome.requestFocus()
        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.svHome, InputMethodManager.SHOW_FORCED)
    }

//    @RequiresApi(Build.VERSION_CODES.Q)

    override fun setupView() {
        super.setupView()
        vm.getUserFromDb()
        val autoCompleteTextView =
            binding.svHome.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
        setCursorDrawable( R.drawable.cursor , autoCompleteTextView)
//        autoCompleteTextView.textCursorDrawable =
//            ContextCompat.getDrawable(requireContext(), R.drawable.cursor)


        setupPager()
    }
    private fun  setCursorDrawable(resId : Int, autoCompleteTextView : SearchView.SearchAutoComplete){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            autoCompleteTextView.textCursorDrawable =   ContextCompat.getDrawable(requireContext(), R.drawable.cursor)
            return;
        }
        try {
            @SuppressLint("SoonBlockedPrivateApi") val field = TextView::class.java.getDeclaredField("mCursorDrawableRes");
            field.isAccessible = true;
            field.set(autoCompleteTextView,  ContextCompat.getDrawable(requireContext(), R.drawable.cursor));
        } catch ( throwable : Throwable) {
            Timber.tag("searchView").d(throwable.localizedMessage)
        }

}

    override fun setupListener() {
        super.setupListener()

        binding.ivBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_home_search_to_dest_top_home)
        }
        /** key action on back press */
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_home_search_to_dest_top_home)
        }
        /** search view Text change listener */
        binding.svHome.setOnQueryTextListener(
            (object : SearchView.OnQueryTextListener,
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideKeyBoard()
                    binding.offlineState.constOfflineState.visibility = View.INVISIBLE
                    /** saving search text to view model  */
                    vm.getFriendSearch(offset, limit, query.toString().ifEmpty { "ShineThyuZan" })
                    vm.setSearchText(query.toString())
                    binding.idTabConstraint.visibility = View.VISIBLE
                    return true
                }

                override fun onQueryTextChange(inputSearchText: String?): Boolean {
                    if (inputSearchText.toString().isEmpty()) {
                        vm.setSearchText("")
                        binding.idTabConstraint.visibility = View.INVISIBLE
                        binding.offlineState.constOfflineState.visibility = View.VISIBLE
                    }
                    return false
                }
            })
        )

    }

    /** setup Pager create Friend , Message , Feeds, Service */
    private fun setupPager() {
        val homePagerAdapter = HomePagerAdapter(
            this.childFragmentManager,
            lifecycle
        )
        binding.homePager.apply {
            this.isUserInputEnabled = true
            this.adapter = homePagerAdapter
            this.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            this.registerOnPageChangeCallback(orderPagerCallback)
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }


        TabLayoutMediator(binding.homeTab, binding.homePager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.people)
                }
                1 -> {
                    tab.text = getString(R.string.messages)
                }
                2 -> {
                    tab.text = getString(R.string.feeds)
                }
                3 -> {
                    tab.text = getString(R.string.services)
                }
            }
            //todo something

        }.attach()
    }

    private var orderPagerCallback = object : ViewPager2.OnPageChangeCallback() {

    }

    fun hideKeyBoard() {
        // Hide the keyboard
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        Timber.tag("Hide Keyboard").d("Hide True")
    }


}