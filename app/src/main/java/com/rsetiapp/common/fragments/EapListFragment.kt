package com.rsetiapp.common.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsetiapp.BuildConfig
import com.rsetiapp.R
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.model.request.EapListReq
import com.rsetiapp.common.model.response.EapList
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.basecomponent.BaseRecyclerAdapter
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.NoDataHelper
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.toastLong
import com.rsetiapp.databinding.EapListFragmentBinding
import com.rsetiapp.databinding.ItemEapBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class EapListFragment :
    BaseFragment<EapListFragmentBinding>(EapListFragmentBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()

    private lateinit var eapAdapter: BaseRecyclerAdapter<EapList, ItemEapBinding>
    private var eapList: MutableList<EapList> = mutableListOf()

    private var eapIdValue = ""
    private var eapStatusValue = ""
    private var eapDateValue = ""
    private var formName = ""
    private var stateNme = ""
    private var stateCode = ""
    private var districtCode = ""
    private var districtName = ""
    private var blockName = ""
    private var blockCode = ""
    private var gpName = ""
    private var gpCode = ""
    private var villageName = ""
    private var villageCode = ""
    private var eapName = ""
    private var programCode = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        init()
        if (eapList.isEmpty()) {
            NoDataHelper.showNoData(binding.container, title = "No Data Found")
        } else {
            NoDataHelper.hideNoData(binding.container)
        }

    }

    private fun init() {
        formName = arguments?.getString("formName").toString()

        setupAdapter()
      binding.apply{
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = eapAdapter
        }

        // API Call
        commonViewModel.eapDetailsAPI(
            AppUtil.getSavedTokenPreference(requireContext()),
            EapListReq(
                BuildConfig.VERSION_NAME,
                userPreferences.getUseID(),
                AppUtil.getAndroidId(requireContext())
            )
        )
        collectEapListResponse()
    }

    private fun setupAdapter() {
        eapAdapter = BaseRecyclerAdapter(
            items = eapList,
            bindingInflater = ItemEapBinding::inflate,
            onBind =  { item, binding, _ ->
                binding.apply {
                    tvEapName.text = item.eapName
                    tvMonthYear.text = item.monthYear
                    tvEapAddress.text = item.eapAddress
                    tvStatus.text = item.status

                    when (item.status) {
                        "Completed" -> statusImage.setImageResource(R.drawable.ic_dark_verified)
                        "Expired" -> statusImage.setImageResource(R.drawable.baseline_dangerous_24)
                        "Active" -> statusImage.setImageResource(R.drawable.ic_verified)
                    }
                }
            },
            diffChecker = { old, new ->
                old.eapID == new.eapID
            },
            onItemClick = { item, _ ->
                getValue(item)
            }
        )
    }

    @SuppressLint("DefaultLocale")
    private fun getValue(eapItem: EapList) {
        eapIdValue = eapItem.eapID.toString()
        eapDateValue = eapItem.monthYear
        eapStatusValue = eapItem.status
        stateNme = eapItem.stateNme
        stateCode = eapItem.stateCode
        districtCode = eapItem.districtCode
        districtName = eapItem.districtName
        blockName = eapItem.blockName
        blockCode = eapItem.blockCode
        gpName = eapItem.gpName
        gpCode = eapItem.gpCode
        villageName = eapItem.villageName
        villageCode = eapItem.villageCode
        eapName = eapItem.eapName
        programCode = eapItem.programCode.toString()

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.isLenient = false

        val currentDate = sdf.format(Date())

        try {
            val eapDate = sdf.parse(eapDateValue)
            val today = sdf.parse(currentDate)

            when {
                eapStatusValue == "Active" && eapDate == today -> {
                    findNavController().navigate(
                        EapListFragmentDirections.actionEapListFragmentToEAPAwarnessFormFragment(
                            formName, eapIdValue, stateNme, stateCode, districtCode, districtName,
                            blockName, blockCode, gpName, gpCode, villageName, villageCode, eapName, programCode
                        )
                    )
                }

                eapStatusValue == "Expired" ->
                    AppUtil.showAlertDialog(requireContext(), "Alert", "Eap Expired")

                eapStatusValue == "Completed" ->
                    AppUtil.showAlertDialog(requireContext(), "Alert", "Eap Completed")

                else ->
                    showMismatchAlert(requireContext(), eapDateValue, currentDate)
            }

        } catch (e: Exception) {
            AppUtil.showAlertDialog(requireContext(), "Error", "Invalid date format in EAP data.")
        }
    }

    private fun showMismatchAlert(context: Context, eapDate: String, currentDate: String) {
        AlertDialog.Builder(context)
            .setTitle("Alert")
            .setMessage("You can only proceed on: $eapDate\nToday: $currentDate")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun collectEapListResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.eapDetailsAPI) {
                when (it) {
                    is Resource.Loading -> showProgressBar()

                    is Resource.Error -> {
                        hideProgressBar()
                        showSnackBar("Internal Server Error")
                        NoDataHelper.showNoData(
                            parent = binding.container,
                            title ="Internal Server Error",
                            iconRes = R.drawable.no_data,
                        )
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        NoDataHelper.hideNoData(binding.container)
                        it.data?.let { response ->
                            if (response.responseCode == 200) {

                                eapList.clear()

                                eapList.addAll(response.wrappedList)
                                eapAdapter.update(response.wrappedList)
                                eapAdapter.notifyDataSetChanged()
                            }
                            else if (response.responseCode == 401) {
                                AppUtil.showSessionExpiredDialog(
                                    findNavController(),
                                    requireContext()
                                )
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
}
