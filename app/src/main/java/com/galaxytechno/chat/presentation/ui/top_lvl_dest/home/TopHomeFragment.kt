package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home

import android.app.Activity
import android.content.res.Configuration
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.common.InternetChecker
import com.galaxytechno.chat.common.NetworkStatus
import com.galaxytechno.chat.databinding.TopLvlHomeBinding
import com.galaxytechno.chat.presentation.base.TopFragment
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class TopHomeFragment : TopFragment<TopLvlHomeBinding>(TopLvlHomeBinding::inflate) {
    @Inject
    lateinit var internetChecker: InternetChecker
    private var internetStatus: Boolean = false
    private val viewModelHome: TopHomeViewModel by viewModels()
    private var phoneNoList: ArrayList<String> = arrayListOf()


    override fun setupView() {
        super.setupView()
        viewModelHome.getUserFromDb()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
    }


    override fun setupListener() {
        super.setupListener()
        binding.ivQrScanner.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivShakePhone.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivPeopleNearby.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivMyNotes.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivSearchView.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_search)
        }

        binding.ivContact.setOnClickListener {
            if (MainActivity.isInternetAvailable.value == true) {
                findNavController().navigate(R.id.action_dest_top_home_to_friContactFragment)
            } else {
                displayToast("Please check internet connection")
            }
        }
        binding.ivNoti.setOnClickListener {
            findNavController().navigate(R.id.action_dest_top_home_to_notificationFragment)
        }


    }

    override fun observe() {
        super.observe()
        /** internet checking state  */
        internetChecker.observe(viewLifecycleOwner) {
            when (it) {
                NetworkStatus.Available -> {
                    binding.cvInternetStatus.visibility = View.GONE
                    binding.profileItem.isEnabled = true
                    binding.profileItem.setOnClickListener {
                        findNavController().navigate(R.id.action_dest_top_home_to_myProfileFragment)
                    }
                }
                NetworkStatus.UnAvailable -> {
                    binding.profileItem.isEnabled = false
                    binding.cvInternetStatus.visibility = View.VISIBLE
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            /**use StateFlow with collectLatest EVER */
            viewModelHome.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.dialog_login))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }

        /** binding DB's user data in Home screen( name and profileImage )*/
        viewModelHome.userEntity.observe(viewLifecycleOwner) {
            if (it.name.isNotEmpty()) {
                binding.tvUserName.text = it.name.ifEmpty { "Hello" }
            }
            val options: RequestOptions = RequestOptions()
                .placeholder(R.drawable.home_profile_placeholder)
                .error(R.drawable.home_profile_placeholder)
//                .centerCrop()
//                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.HIGH)
            val headurl = it.headUrl.ifEmpty { Constant.SERVER_IMAGE_URL }

            Glide.with(requireContext())
                .load(headurl)
                .placeholder(R.drawable.home_profile_placeholder)
                //.apply(options.circleCrop())
                .into(binding.ivUser)


            binding.tvbio.text = it.bio.ifEmpty { resources.getString(R.string.bio) }

        }
    }

    private fun isDarkTheme(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}