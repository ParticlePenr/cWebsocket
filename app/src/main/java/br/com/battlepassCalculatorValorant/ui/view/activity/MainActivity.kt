package br.com.battlepassCalculatorValorant.ui.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import br.com.battlepassCalculatorValorant.R
import br.com.battlepassCalculatorValorant.databinding.ActivityMainBinding
import br.com.battlepassCalculatorValorant.ui.view.dialog.DialogInput
import br.com.battlepassCalculatorValorant.ui.viewModel.activity.UIViewModel
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import dagger.hilt.android.AndroidEntryPoint

@Suppress("UNREACHABLE_CODE")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val uiViewModel by viewModels<UIViewModel>()

    private val navController
        get() = findNavController(R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.root.doOnLayout {
            NavigationUI.setupWithNavController(binding.bottomNav, navController)
        }

        setupObservers()
        createListeners()
    }

    private fun setupObservers() {
        uiViewModel.onHideBottomNav.observe(this, Observer {
            val params = binding.bottomNav.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior as HideBottomViewOnScrollBehavior

            if (it) {
                binding.bottomNav.doOnLayout { view -> behavior.slideUp(view) }
            } else {
                binding.bottomNav.doOnLayout { view -> behavior.slideDown(view) }
            }
            binding.bottomNav.transform(binding.fab, it)
        })
    }

    private fun createListeners() {
        binding.fab.setOnClickListener {
            DialogInput().show(
                supportFragmentManager,
                DialogInput.TAG
            )
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}