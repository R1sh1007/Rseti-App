package com.rsetiapp.common.fragments

import ChildAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.BuildConfig
import com.rsetiapp.R
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.model.response.Module
import com.rsetiapp.common.model.response.VisitData
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.basecomponent.BaseRecyclerAdapter
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.NoDataHelper
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.toastLong
import com.rsetiapp.core.util.toastShort
import com.rsetiapp.databinding.FragmentHomeBinding
import com.rsetiapp.databinding.ItemParentBinding
import com.rsetiapp.databinding.ItemSdrDataBinding
import com.rsetiapp.databinding.NavigationHeaderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()
    private lateinit var homeAdapter: BaseRecyclerAdapter<Module, ItemParentBinding>

    private val moduleList = mutableListOf<Module>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())

        setupRecyclerView()
        collectModulesData()
        if (moduleList.isEmpty()) {
            NoDataHelper.showNoData(binding.container, title = "No Data Found")
        } else {
            NoDataHelper.hideNoData(binding.container)
        }
        handleBackPress()
        val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)

        binding.apply {

            // Apply shake animation
            profilePic.startAnimation(shake)
            changeLanguage.startAnimation(shake)

            // Navigation Header Binding
            navigationView.getHeaderView(0)?.let { headerView ->
                val headerBinding = NavigationHeaderBinding.bind(headerView)

                headerBinding.apply {
                    loginId.text =
                        "${userPreferences.getUserName()} (${userPreferences.getUseID()})"
                }
            }

            // Profile click → Open Drawer
            profilePic.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }

            // Change language → Navigate
            changeLanguage.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFrahmentToLanguageChangeFragment()
                )
            }

            // Navigation menu clicks
            navigationView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {

                    R.id.nav_logout -> {
                        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
                        AppUtil.saveLoginStatus(requireContext(), false)

                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFrahmentToLoginFragment2()
                        )

                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
                true
            }
        }
    }


    private fun setupRecyclerView() {
        homeAdapter=BaseRecyclerAdapter(items = moduleList,
            bindingInflater = ItemParentBinding::inflate,
            {item,binding,position ->
                binding.apply {
                    tvModuleName.text = item.moduleName
                    tvModuleName.setOnClickListener {
                        homeAdapter.triggerViewClick(binding.tvModuleName, item, position)
                    }
                    rvChild.visibility = if (item.isExpanded) View.VISIBLE else View.GONE
                    if (item.isExpanded) {
                        rvChild.layoutManager = LinearLayoutManager(binding.root.context)
                        rvChild.adapter = ChildAdapter(item.forms)
                    }
                }
            },
            onViewClick  = {view,module,_ ->
                when (view.id) {
                    R.id.tvModuleName -> {
                        module.isExpanded = !module.isExpanded
                        homeAdapter.update(moduleList)
                        homeAdapter.notifyDataSetChanged()

                    }
                }
            }

            )
        binding.rvParent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvParent.adapter = homeAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun collectModulesData() {
        commonViewModel.getFormAPI(AppUtil.getSavedTokenPreference(requireContext()),BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext()))
        lifecycleScope.launch {
            commonViewModel.getFormAPI.collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        resource.error?.message?.let {
                            toastShort(it)
                            NoDataHelper.showNoData(
                                parent = binding.container,
                                title =it,
                                iconRes = R.drawable.no_data,
                            )
                        }

                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        NoDataHelper.hideNoData(binding.container)
                        resource.data?.let { response ->
                            if (response.responseCode == 200) {
                                moduleList.clear()
                                moduleList.addAll(response.wrappedList)
                               // parentAdapter.notifyDataSetChanged()
                                homeAdapter.update(response.wrappedList)
                                homeAdapter.notifyDataSetChanged()
                            }
                            else if (response.responseCode==401){
                                AppUtil.showSessionExpiredDialog(findNavController(),requireContext())
                            }
                            else {
                                toastLong(response.responseDesc)
                                NoDataHelper.showNoData(
                                    parent = binding.container,
                                    title =response.responseDesc,
                                    iconRes = R.drawable.no_data,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                private var backPressedTime: Long = 0
                private val exitInterval = 2000 // 2 seconds

                override fun handleOnBackPressed() {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < exitInterval) {
                        isEnabled =
                            false // Disable callback to let the system handle the back press
                        requireActivity().finish()
                    } else {
                        backPressedTime = currentTime
                        showSnackBar("Press back again to exit")
                    }
                }
            })
    }
}
