package com.galaxytechno.chat.presentation.single_activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.InternetChecker
import com.galaxytechno.chat.common.NetworkStatus
import com.galaxytechno.chat.databinding.ActivityMainBinding
import com.galaxytechno.chat.presentation.base.BaseActivity
import com.galaxytechno.chat.presentation.extension.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    @Inject
    lateinit var internetChecker: InternetChecker

    @Inject
    lateinit var socket: Socket

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
    }
    private val navController: NavController by lazy {
        navHostFragment.navController
    }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }
    private val viewModel: MainViewModel by viewModels()

    companion object {
        private var _isInternetAvailable = MutableLiveData<Boolean>(false)
        val isInternetAvailable: LiveData<Boolean> get() = _isInternetAvailable
    }

    override fun initialize() {
        super.initialize()
        setStatusBarColor()
//        socket.connect()
    }

    fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (isDarkTheme(this))
                this.setStatusBarColor(ContextCompat.getColor(this, R.color.black))
            else
                this.setStatusBarColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun isDarkTheme(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }

    override fun observe() {
        super.observe()
        internetChecker.observe(this) {
            _isInternetAvailable.value = when (it) {
                NetworkStatus.Available -> {
                    true
                }
                NetworkStatus.UnAvailable -> {
                    false
                }
            }
        }
        //todo : if authState is false && languageChosenState is false -> start with LANGUAGE CHOSEN SCREEN
        // if authState is false && languageChosenState is true -> start with LOGIN CHOSEN SCREEN
        // if authState is true, -> start with HOME SCREEN


        viewModel.authState.observe(this) {
            setupNavigation(it)
        }
        viewModel.languageChosenState.observe(this) {

        }
    }

    private fun setupNavigation(isLoggedIn: Boolean) {
        setSupportActionBar(binding.authToolbar)

        val navGraph = navController.navInflater.inflate(
            R.navigation.main_navigation
        )
        navGraph.startDestination =
            if (isLoggedIn) {
                if (isLoggedIn) R.id.home_navigation else R.id.auth_navigation
            } else {
                R.id.init_lang_navigation
            }
        navController.graph = navGraph
        appBarConfiguration = AppBarConfiguration(navGraph)

        binding.btnNavView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dest_top_home,
                R.id.dest_top_chat,
                R.id.dest_top_feeds,
                R.id.dest_top_setting
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun shouldShowToolbar(flag: Boolean) {
        if (flag) {
            binding.authToolbar.visibility = View.VISIBLE
        } else binding.authToolbar.visibility = View.GONE
    }

    fun shouldShowBottomNavigation(flag: Boolean) {
        if (flag) {
            binding.btnNavView.visibility = View.VISIBLE
        } else binding.btnNavView.visibility = View.GONE
    }

    fun showLoadingDialog(text: String) {
        loadingDialog.apply {
            setMessage(text)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            show()
        }
    }

    fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.dest_top_home) {
            showCustomDialog()
        } else
            super.onBackPressed()
    }

    private fun showCustomDialog() {
        val exitBtn: Button
        val cancelBtn: Button
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_exit_confirmation)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        /** animate dialog with window animation */
        dialog.window!!.setWindowAnimations(R.style.AnimationForDialog)
        dialog.setCanceledOnTouchOutside(false)
        exitBtn = dialog.findViewById(R.id.btnExit) as Button
        cancelBtn = dialog.findViewById(R.id.btnCancel) as Button

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        exitBtn.setOnClickListener {
            //todo delete dataStore userAccessToken key and LoginState
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }


    fun comingSoonDialog() {
        val ok: Button
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.coming_soon_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        /** animate dialog with window animation */
        dialog.window!!.setWindowAnimations(R.style.AnimationForDialog)
        dialog.setCanceledOnTouchOutside(true)
        ok = dialog.findViewById(R.id.btn_ok_progress) as Button

        ok.setOnClickListener {
            //todo delete dataStore userAccessToken key and LoginState
            dialog.dismiss()
        }
        dialog.show()
    }


}

