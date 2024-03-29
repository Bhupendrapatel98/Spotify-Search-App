package com.app.growtaskapplication.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.app.growtaskapplication.R
import com.app.growtaskapplication.databinding.FragmentHomeBinding
import com.app.growtaskapplication.ui.viewmodel.SearchUserViewModel
import com.app.growtaskapplication.ui.viewmodel.token.GetTokenViewModel
import com.app.growtaskapplication.utills.Constants
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.TokenManager
import com.app.growtaskapplication.utills.UserType
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private val tokenViewModel: GetTokenViewModel by viewModels()
    private val searchUserViewModel: SearchUserViewModel by activityViewModels()
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        callGetTokeApi()
        changeEvents()
        setUpViewPager()
        observeToken()

        return fragmentHomeBinding.root
    }

    private fun callGetTokeApi() {
            tokenViewModel.getToken(
                Constants.CLIENT_CREDENTIAL,
                Constants.CLIENT_ID,
                Constants.CLIENT_SECRET
            )
    }

    private fun observeToken() {

        lifecycleScope.launch {
            tokenViewModel.token.collect {
                when (it) {
                    is Resource.Loading -> {
                        //show Loader
                    }
                    is Resource.Success -> {
                        tokenManager.saveToken(it.data.access_token)
                    }
                    is Resource.Failed -> {
                        //handle failure
                    }
                }
            }
        }
    }

    private fun changeEvents() {
        fragmentHomeBinding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(str: Editable?) {
                if (str.toString().isNotEmpty()) {
                    searchUserViewModel.searchFlowQuery.value = str.toString()
                }
            }
        })
    }

    private fun setUpViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        fragmentHomeBinding.viewpager.adapter = viewPagerAdapter

        TabLayoutMediator(fragmentHomeBinding.tabs, fragmentHomeBinding.viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.album)
                1 -> tab.text = getString(R.string.artist)
                2 -> tab.text = getString(R.string.playlist)
                3 -> tab.text = getString(R.string.track)
            }
        }.attach()

        fragmentHomeBinding.viewpager.registerOnPageChangeCallback(object  : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }
}