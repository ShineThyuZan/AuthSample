package com.galaxytechno.chat.presentation.ui.top_lvl_dest.setting

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentSettingPrivacyBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SettingPrivacyFragment :
    OtherLvlFragment<FragmentSettingPrivacyBinding>(FragmentSettingPrivacyBinding::inflate) {
    private val vm: TopSettingViewModel by activityViewModels()

    override fun setupView() {
        super.setupView()
        binding.toolbarPrivacyAndSecurity.tvToolbarTitle.text = getString(R.string.privacy_and_security)

        /** call api to check two factor state  */
    }

    override fun setupListener() {
        super.setupListener()
        binding.toolbarPrivacyAndSecurity.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvBlockList.setOnClickListener {
            findNavController().navigate(R.id.action_settingPrivacyFragment_to_blockListFragment)
        }

        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
            {
                if(!vm.user.value.isTwoFactor) {
                    vm.requestDoTwoFactor(isChecked)
                }
            }
            else
                vm.requestDoTwoFactor(isChecked)
        }
    }

    private fun loadingProgressStatus(status: TwoFactorStatus) {
        when (status) {
            TwoFactorStatus.Loading -> {
                binding.twoFactorStatus.progressBar.visibility = View.VISIBLE
                binding.twoFactorStatus.ivFail.visibility = View.GONE
                binding.twoFactorStatus.ivSuccess.visibility = View.GONE

            }
            TwoFactorStatus.Success -> {
                binding.twoFactorStatus.progressBar.visibility = View.GONE
                binding.twoFactorStatus.ivFail.visibility = View.GONE
                binding.twoFactorStatus.ivSuccess.visibility = View.VISIBLE

            }
            TwoFactorStatus.Fail -> {
                binding.twoFactorStatus.progressBar.visibility = View.GONE
                binding.twoFactorStatus.ivFail.visibility = View.VISIBLE
                binding.twoFactorStatus.ivSuccess.visibility = View.GONE
            }
        }
    }

    private fun loadingStatus(flag: Boolean) {
        if (flag) {
            binding.twoFactorStatus.statusView.visibility = View.VISIBLE
        } else
            binding.twoFactorStatus.statusView.visibility = View.GONE
    }

    override fun observe() {
        super.observe()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.user.collectLatest {
                binding.switch1.isChecked = it.isTwoFactor
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.twoFactorText.collectLatest {
                binding.twoFactorStatus.tvTowFactorState.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.isTwoFactorLoading.collectLatest {
                loadingStatus(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.twoFactorStatus.collectLatest {
                loadingProgressStatus(it)
            }
        }

        /** show loading state form logout */
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.isLogoutLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.waiting))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.twoFactorEvent.collectLatest { event ->
                when (event) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        event.data!!.status
                    }
                }
            }
        }
    }
}