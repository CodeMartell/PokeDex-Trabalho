package com.romulo.pokedex.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.romulo.pokedex.databinding.ActivitySplashBinding
import com.romulo.pokedex.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareInitialState()
        playIntroAnimation()

        lifecycleScope.launch {
            delay(SPLASH_DURATION)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun prepareInitialState() {
        binding.txtTitle.alpha = 0f
        binding.progressIndicator.apply {
            alpha = 0f
            isVisible = false
        }
    }

    private fun playIntroAnimation() {
        binding.imgLogo.apply {
            scaleX = 0.85f
            scaleY = 0.85f
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(900L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }

        binding.txtTitle.animate()
            .alpha(1f)
            .setStartDelay(250L)
            .setDuration(600L)
            .start()

        binding.progressIndicator.animate()
            .alpha(1f)
            .setStartDelay(450L)
            .setDuration(600L)
            .withStartAction { binding.progressIndicator.isVisible = true }
            .start()


    }

    companion object {
        private const val SPLASH_DURATION = 2200L
    }
}
