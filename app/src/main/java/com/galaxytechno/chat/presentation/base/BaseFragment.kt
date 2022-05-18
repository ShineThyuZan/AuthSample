package com.galaxytechno.chat.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.galaxytechno.chat.presentation.single_activity.MainActivity

typealias InflateFragment<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

open class BaseFragment<VB : ViewBinding>(
    private val inflate: InflateFragment<VB>
) : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    open fun doLogic() {}
    open fun initialize() {}
    open fun setupListener() {}
    open fun setupView() {}
    open fun observe() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        doLogic()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListener()
        setupView()
        observe()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

open class TopFragment<VB : ViewBinding>(
    inflate: InflateFragment<VB>
) : BaseFragment<VB>(
    inflate
) {
    override fun doLogic() {
        super.doLogic()
        (activity as MainActivity).shouldShowToolbar(false)
        (activity as MainActivity).shouldShowBottomNavigation(true)
//        (activity as MainActivity).setStatusBarColor()

    }
}

open class AuthFragment<VB : ViewBinding>(
    inflate: InflateFragment<VB>
) : BaseFragment<VB>(
    inflate
) {
    override fun doLogic() {
        super.doLogic()
        (activity as MainActivity).shouldShowToolbar(true)
        (activity as MainActivity).shouldShowBottomNavigation(false)
    }
}

open class OtherLvlFragment<VB : ViewBinding>(
    inflate: InflateFragment<VB>
) : BaseFragment<VB>(
    inflate
) {
    override fun doLogic() {
        super.doLogic()
        (activity as MainActivity).shouldShowToolbar(false)
        (activity as MainActivity).shouldShowBottomNavigation(false)
    }
}