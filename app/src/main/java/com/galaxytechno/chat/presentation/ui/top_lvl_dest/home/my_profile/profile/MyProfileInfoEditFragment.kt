package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.profile

import android.Manifest
import android.app.DatePickerDialog
import android.net.Uri
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentMyProfileInfoEditBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displaySnack
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.extension.isVerifiedAnswer
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.MyProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class MyProfileInfoEditFragment :
    OtherLvlFragment<FragmentMyProfileInfoEditBinding>(FragmentMyProfileInfoEditBinding::inflate) {
    private val vm: MyProfileViewModel by activityViewModels()
    private var tempUriProfile: Uri? = null
    private var tempUriCover: Uri? = null
    private lateinit var calendar: Calendar
    private lateinit var fullName: String
    private lateinit var bio: String
    private lateinit var email: String
    private lateinit var birthDate: String
    private var isProfileRemove: Boolean? = false
    private var isCoverRemove: Boolean? = false
    private var gender: Int = 0
    private var emptyFile: File? = null


    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                findNavController().navigate(R.id.action_myProfileEditFragment_to_imagePickBottomSheet)
                binding.tvUploadCover.isEnabled = true
            }
        }

    private val permissionCover =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                findNavController().navigate(R.id.action_myProfileEditFragment_to_pickCoverImageBottomSheetFragment)
                binding.ivMyProfile.isEnabled = true
            }
        }

    private val fromCameraProfile = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            tempUriProfile?.let { uri ->
                binding.ivMyProfile.setImageURI(uri)

                val tempFile =
                    File.createTempFile(
                        "you_know_profile",
                        ".png",
                        requireContext().externalCacheDir
                    )
                val inputStream: InputStream? =
                    requireContext().contentResolver.openInputStream(uri)

                inputStream?.let { input ->
                    FileOutputStream(tempFile, false).use { output ->
                        var read: Int
                        val bytes = ByteArray(DEFAULT_BUFFER_SIZE)
                        while (input.read(bytes).also { index ->
                                read = index
                            } != -1) {
                            output.write(bytes, 0, read)
                        }
                        output.close()
                    }
                    input.close()
                    val file = tempFile.absoluteFile
                    vm.setProfileFile(file = file)
                }
            }
        }
    }

    private val fromCameraCover = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            tempUriCover?.let { uri ->
                binding.ivCoverImage.setImageURI(uri)

                val tempFile =
                    File.createTempFile("you_know_cover", ".png", requireContext().externalCacheDir)
                val inputStream: InputStream? =
                    requireContext().contentResolver.openInputStream(uri)

                inputStream?.let { input ->
                    FileOutputStream(tempFile, false).use { output ->
                        var read: Int
                        val bytes = ByteArray(DEFAULT_BUFFER_SIZE)
                        while (input.read(bytes).also { index ->
                                read = index
                            } != -1) {
                            output.write(bytes, 0, read)
                        }
                        output.close()
                    }
                    input.close()
                    val file = tempFile.absoluteFile
                    vm.setCoverFile(
                        file = file
                    )
                }
            }
        }
    }

    private val fromGalleryProfile = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let { uri ->
            binding.ivMyProfile.setImageURI(uri)
            val tempFile =
                File.createTempFile("you_know_profile", ".png", requireContext().externalCacheDir)
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)

            inputStream?.let { input ->
                FileOutputStream(tempFile, false).use { output ->
                    var read: Int
                    val bytes = ByteArray(DEFAULT_BUFFER_SIZE)
                    while (input.read(bytes).also { index ->
                            read = index
                        } != -1) {
                        output.write(bytes, 0, read)
                    }
                    output.close()
                }
                input.close()
                val file = tempFile.absoluteFile
                vm.setProfileFile(file)
            }
        }
    }

    private val fromGalleryCover = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let { uri ->
            binding.ivCoverImage.setImageURI(uri)

            val tempFile =
                File.createTempFile("you_know_cover", ".png", requireContext().externalCacheDir)
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)

            inputStream?.let { input ->
                FileOutputStream(tempFile, false).use { output ->
                    var read: Int
                    val bytes = ByteArray(DEFAULT_BUFFER_SIZE)
                    while (input.read(bytes).also { index ->
                            read = index
                        } != -1) {
                        output.write(bytes, 0, read)
                    }
                    output.close()
                }
                input.close()
                val file = tempFile.absoluteFile
                vm.setCoverFile(file)
            }
        }
    }

    override fun setupView() {
        super.setupView()
        calendar = Calendar.getInstance()
    }

    /** fragment's lifecycle methods */
    override fun setupListener() {
        super.setupListener()
        binding.toolbarEditProfile.ivEditProfileBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivMyProfile.setOnClickListener {
            binding.tvUploadCover.isEnabled = false
            permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        binding.tvUploadCover.setOnClickListener {
            binding.ivMyProfile.isEnabled = false
            permissionCover.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        binding.etFullName.doAfterTextChanged {
            fullName = binding.etFullName.text!!.trim().toString()
        }
        binding.etBio.doAfterTextChanged {
            bio = binding.etBio.text!!.trim().toString()
        }
        binding.etEditEmail.doAfterTextChanged {
            email = binding.etEditEmail.text!!.trim().toString()
        }


        binding.rgRegisterGender.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = requireActivity().findViewById(checkedId)
            when (radio) {
                binding.rbRegisterMale -> {
                    gender = 1
                }
                binding.rbRegisterFemale -> {
                    gender = 2
                }
                binding.rbRegisterOther -> {
                    gender = 3
                }
            }
        }


        /** update user info when validation  */
        binding.toolbarEditProfile.btnUpdate.setOnClickListener {
            val tempFile =
                File.createTempFile("you_empty_file", ".png", requireContext().externalCacheDir)
            vm.setEmptyFile(tempFile)

            if (validate()) {
                updateUserProfile()
            }
            vm.setLoadingState(true)
            findNavController().popBackStack()

        }

        /** choose birthdate from android sup library */
        binding.tilBirthDay.setOnClickListener {
            DatePickerDialog(
                requireContext(), date, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun observe() {
        super.observe()

        /** loading state  */
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.update_profile_info))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }

        /** profile update info response */
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.editResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        displayToast("Try your internet connection")
                    }
                    is RemoteEvent.LoadingEvent -> {

                    }
                    is RemoteEvent.SuccessEvent -> {
                        binding.root.displaySnack(it.data!!.status)
                    }
                }
            }
        }

        /** pick up state in view model */
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.profileActionState.collectLatest {
                when (it) {
                    "Camera" -> {
                        takeImageFromCameraProfile()
                    }
                    "Gallery" -> {
                        takeImageFromGalleryProfile()
                    }
                    "Remove" -> {
                        Glide.with(requireActivity())
                            .load(Constant.SERVER_IMAGE_URL)
                            .placeholder(R.drawable.place_holder)
                            .into(binding.ivMyProfile)
                        isProfileRemove = true

                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.coverActionState.collectLatest {
                when (it) {
                    "Camera" -> {
                        takeImageFromCameraCover()
                    }
                    "Gallery" -> {
                        takeImageFromGalleryCover()
                    }
                    "Remove" -> {
                        Glide.with(requireActivity())
                            .load(Constant.SERVER_IMAGE_URL)
                            .placeholder(R.drawable.place_holder)
                            .into(binding.ivCoverImage)
                        isCoverRemove = true

                    }
                }
            }
        }

        /** data set to the profile edit screen */
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.userProfileInfoEvent.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        displayToast(it.data!!.error ?: "Error")
                    }
                    is RemoteEvent.LoadingEvent -> {
                    }
                    is RemoteEvent.SuccessEvent -> {
                        it.data.let { response ->
                            response!!.data!!.coverImgUrl.let {
                                Glide.with(requireContext())
                                    .load(response.data!!.coverImgUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .into(binding.ivCoverImage)
                            }
                            response.data!!.headUrl.let {
                                Glide.with(requireContext())
                                    .load(response.data.headUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .into(binding.ivMyProfile)
                            }
                            binding.etFullName.setText(response.data.name)
                            response.data.bio.let { bio ->
                                binding.etBio.setText(bio)
                            }
                            response.data.email.let { email ->
                                binding.etEditEmail.setText(email)
                            }
                            response.data.birthDate.let { dob ->
                                binding.etBirthDate.text = dob
                            }
                            response.data.gender.let { gender ->
                                when (gender) {
                                    1 -> binding.rbRegisterMale.isChecked = true
                                    2 -> binding.rbRegisterFemale.isChecked = true
                                    3 -> binding.rbRegisterOther.isChecked = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /** our business logic of taking picture for profile photo */
    private fun takeImageFromGalleryProfile() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            fromGalleryProfile.launch("image/*")
        }
    }

    private fun takeImageFromCameraProfile() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            getTmpFileUri().let {
                tempUriProfile = it
                fromCameraProfile.launch(tempUriProfile)
            }
        }
    }

    /** our business logic of taking picture for cover photo */
    private fun takeImageFromGalleryCover() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            fromGalleryCover.launch("image/*")
        }
    }

    private fun takeImageFromCameraCover() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            getTmpFileUri().let {
                tempUriCover = it
                fromCameraCover.launch(tempUriCover)
            }
        }
    }

    /** create temp file to show in profile view and cover image in UI */
    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("just_temp_file", ".png", requireActivity().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(
            requireActivity(),
            "com.example.klt_clean_architecture_sample.provider",
            tmpFile
        )
    }

    /** data picker for android support */
    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.etBirthDate.text = sdf.format(calendar.time)
    }

    private val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateLabel()
    }

    /** validation the required field and posting user info to server */
    private fun validate(): Boolean {
        if (!binding.etFullName.isVerifiedAnswer()) {
            binding.tilFullName.error = getString(R.string.fill_your_full_name)
            return false
        }
        return true
    }

    /** update user info status */
    private fun updateUserProfile() {
        vm.editProfile(
            name = binding.etFullName.text?.trim().toString(),
            bio = binding.etBio.text?.trim().toString(),
            email = binding.etEditEmail.text?.trim().toString(),
            birthDate = binding.etBirthDate.text?.trim().toString(),
            gender = gender,
            isProfileImgRemove = isProfileRemove!!,
            isCoverImageRemove = isCoverRemove!!
        )
    }
}

