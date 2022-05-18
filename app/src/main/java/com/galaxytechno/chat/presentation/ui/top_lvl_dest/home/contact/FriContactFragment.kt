package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.contact


import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentFriContactBinding
import com.galaxytechno.chat.model.dto.ContactFriObj
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displaySnackTop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FriContactFragment :
    OtherLvlFragment<FragmentFriContactBinding>(FragmentFriContactBinding::inflate),
    FriContactDelegate {
    private val vm: FriContactViewModel by activityViewModels()
    private var friContactAdapter: FriContactAdapter? = null
    private var contactFriObjList: MutableList<ContactFriObj> = mutableListOf()
    private var isBlockState: String = ""
    private var count: Int = 0

    companion object {
        const val IS_BLOCK_STATE: String = "YouBlockOther"
        const val IS_BEING_BLOCK: String = "OtherBlockYou"
    }

    private var contactPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            /** access the contact access , get contact list form phone and call the api */
            vm.getContactsFromDevice()
        }
        /** deny the contact access ( user action ) */
        else {
            binding.permissionView.constOfflineState.visibility = View.VISIBLE
            binding.permissionView.tvHead.text = getString(R.string.premission_denied)
            binding.permissionView.tvDescription.text = getString(R.string.goAppSetting)

            binding.permissionView.btnEnableAccess.visibility = View.VISIBLE
            binding.permissionView.btnEnableAccess.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            when (count) {
                0 -> {
                    binding.permissionView.btnEnableAccess.setOnClickListener {
                        setupView()
                        count = 1
                    }
                }
                1 -> {
                    binding.permissionView.btnEnableAccess.setOnClickListener {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun initialize() {
        super.initialize()
        binding.toolbarContact.tvToolbarTitle.text = getString(R.string.contact)
    }

    private fun setupContactFriRecyclerView() {
        friContactAdapter = FriContactAdapter(requireContext(), this@FriContactFragment)
        binding.rvContactFriList.apply {
            this.adapter = friContactAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)

        }
    }

    private fun shouldShowLoading(flag: Boolean) {
        if (flag) {
            binding.progressBar2.visibility = View.VISIBLE
        } else
            binding.progressBar2.visibility = View.INVISIBLE
    }

    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.pickContactsStatus.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        binding.root.displaySnackTop("Some Error occur")
                    }
                    is RemoteEvent.LoadingEvent -> {
                        shouldShowLoading(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        shouldShowLoading(false)
                        if (it.data!!.isNullOrEmpty()) {
                            binding.permissionView.constOfflineState.visibility = View.VISIBLE
                            binding.permissionView.tvHead.text = getString(R.string.no_contact)
                            binding.permissionView.tvDescription.text =
                                getString(R.string.no_contact_in_phone)
                        } else
                            binding.permissionView.constOfflineState.visibility = View.INVISIBLE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.friContactEvent.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        shouldShowLoading(false)
                        if (it.data?.data?.appUserInfos.isNullOrEmpty()) {
                            binding.permissionView.constOfflineState.visibility = View.VISIBLE
                            binding.permissionView.tvHead.text = getString(R.string.no_acc)
                            binding.permissionView.tvDescription.text =
                                getString(R.string.have_register_app)
                        } else {
                            binding.permissionView.constOfflineState.visibility = View.INVISIBLE
                            /** setup recycler view and add the contact list to adapter */
                            setupContactFriRecyclerView()
                            contactFriObjList =
                                it.data?.data?.appUserInfos as MutableList<ContactFriObj>
                            friContactAdapter!!.setNewData(contactFriObjList)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.contactProfileResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        when (it.data!!.data!!.friendStatus) {
                            5 -> {
                                isBlockState = IS_BLOCK_STATE
                                val directions =
                                    FriContactFragmentDirections.actionFriContactFragmentToFriContactBlockStateFragment(
                                        isBlockState
                                    )
                                findNavController().navigate(directions)

                            }
                            6 -> {
                                isBlockState = IS_BEING_BLOCK
                                val directions =
                                    FriContactFragmentDirections.actionFriContactFragmentToFriContactBlockStateFragment(
                                        isBlockState
                                    )
                                findNavController().navigate(directions)
                            }
                            else -> {
                                findNavController().navigate(R.id.action_friContactFragment_to_friContactProfileFragment)

                            }
                        }
                    }
                }
            }
        }

    }

    override fun setupView() {
        super.setupView()
        contactPermissionResult.launch(Manifest.permission.READ_CONTACTS)
    }

    override fun setupListener() {
        super.setupListener()
        binding.toolbarContact.ivBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_friContactFragment_to_dest_top_home)
        }

        /** physical back key event for
         * */
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_friContactFragment_to_dest_top_home)
        }
    }

    override fun onClickedFriContact(data: ContactFriObj) {
        /** set Contact fri DATA TO viewmodel */
        vm.setContactFriData(data)
        /** friend status(blockYou? , youBlock?, other ) check in api call */
        vm.contactProfileDetail()
    }

}