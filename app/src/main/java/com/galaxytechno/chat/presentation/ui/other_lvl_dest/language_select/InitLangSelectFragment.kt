package com.galaxytechno.chat.presentation.ui.other_lvl_dest.language_select

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.InternetChecker
import com.galaxytechno.chat.common.NetworkStatus
import com.galaxytechno.chat.databinding.FragmentLanguageSelectBinding
import com.galaxytechno.chat.model.dto.LanguageVos
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class InitLangSelectFragment : OtherLvlFragment<FragmentLanguageSelectBinding>(
    FragmentLanguageSelectBinding::inflate
), InitLangSelectDelegate {

    private val vm: LanguageViewModel by activityViewModels()
    private lateinit var languageAdapter: InitLangSelectAdapter
    private fun setUpLanguageListRecyclerView() {
        languageAdapter = InitLangSelectAdapter(requireContext(), this)
        binding.rvLanguage.apply {
            this.adapter = languageAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    override fun initialize() {
        super.initialize()
        MainActivity.isInternetAvailable.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
//                    binding.cvInternetStatus.visibility = View.GONE
                    vm.getSupportLanguageList()
                }
                false -> {
                    findNavController().navigate(R.id.action_initLangSelectFragment_to_initLangNoInternetFragment)
                }
            }
        }
    }

    override fun onClickSelectedLanguage(data: LanguageVos) {
        vm.setLanguageObj(data)
    }

    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            //use StateFlow with collectLatest EVER
            vm.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.dialog_saving_language))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.languagesList.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        //todo : show error message
                    }
                    is RemoteEvent.LoadingEvent -> {
                        //todo : show loading progress
                    }
                    is RemoteEvent.SuccessEvent -> {
                        setUpLanguageListRecyclerView()
                        languageAdapter.setNewData(it.data!!.data!!.languageList.toMutableList())
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.done.collectLatest {
                if (it) {
                    findNavController().navigate(R.id.action_initLangSelectFragment_to_auth_navigation)
                }
            }
        }



    }
}