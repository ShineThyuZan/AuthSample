package com.galaxytechno.chat.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewbinding.ViewBinding

typealias InflateActivity<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB : ViewBinding>(
    private val inflate: InflateActivity<VB>
) : AppCompatActivity() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    open fun initialize() {}
    open fun setupListener() {}
    open fun setupView() {}
    open fun observe() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashView = installSplashScreen()
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            splashScreen.setOnExitAnimationListener {
                v ->

                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    v,
                    View.TRANSLATION_Y,
                    0f,
                    -v.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 200L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd {
                    v.remove() }

                // Run your animation.
                slideUp.start()
            }*/
        /*// Get the duration of the animated vector drawable.
        val animationDuration = splashScreen.iconAnimationDuration
        // Get the start time of the animation.
        val animationStart = splashScreen.iconAnimationStart
        // Calculate the remaining duration of the animation.
        val remainingDuration = if (animationDuration != null && animationStart != null) {
            (animationDuration - Duration.between(animationStart, Instant.now()))
                .toMillis()
                .coerceAtLeast(0L)
        } else {
            0L
        }
         // Set up an OnPreDrawListener to the root view.
    val content: View = findViewById(android.R.id.content)
    content.viewTreeObserver.addOnPreDrawListener { // Check if the initial data is ready.
        /* return if (viewModel.isReady) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }*/
        true
    }



    }*/

        _binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
        initialize()
        setupListener()
        setupView()
        observe()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
