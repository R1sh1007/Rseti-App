package com.rsetiapp.common.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsetiapp.BuildConfig
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.adapter.SettlementBatchAdapter
import com.rsetiapp.common.model.request.DistrictListReq
import com.rsetiapp.common.model.request.InstituteListReq
import com.rsetiapp.common.model.request.SettlementVeryficationBatchReq
import com.rsetiapp.common.model.response.DistrictList
import com.rsetiapp.common.model.response.Institutes
import com.rsetiapp.common.model.response.SettlementPercentage
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.AppUtil.convertMonthNumberToFullName
import com.rsetiapp.core.util.AppUtil.getCurrentYear
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.databinding.FragmentSettlementVeryficationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettlementVeryficationFragment : BaseFragment<FragmentSettlementVeryficationBinding>(FragmentSettlementVeryficationBinding::inflate) {

    private var formName = ""
    private lateinit var DistrictAdapter: ArrayAdapter<String>
    private lateinit var InstituteAdapter: ArrayAdapter<String>
    private var selectedDistrictIdValue: String? = null
    private var selectedInstituteIdValue: String? = null
    private var DistrictList: List<DistrictList> = mutableListOf()
    private var InstituteList: List<Institutes> = mutableListOf()
    private var DistrictName = ArrayList<String>()
    private var InstituteName = ArrayList<String>()
    private val commonViewModel: CommonViewModel by activityViewModels()


    private var years = arrayListOf<String>()
    private var months = arrayListOf<String>()
    private lateinit var settlementVeryfiationAdapter: SettlementBatchAdapter
    private val batchList = mutableListOf<SettlementPercentage>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        formName = arguments?.getString("formName").toString()

        init()

//        if (batchFilteredList.isEmpty()) {
//            NoDataHelper.showNoData(binding.container, title = "No Data Found")
//        } else {
//            NoDataHelper.hideNoData(binding.container)
//        }
    }

    private fun init() {
//        binding.tvTitleName.text = formName
        binding.tvTitleName.text = "Settlement Batch Batch"

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        years =
            (arrayListOf("All") + (2024..getCurrentYear()).map { it.toString() }) as ArrayList<String>
        months =
            (arrayListOf("All") + (1..12).map { convertMonthNumberToFullName(it) }) as ArrayList<String>
//        commonViewModel.getFollowTypeListAPI(AppUtil.getSavedTokenPreference(requireContext()),AppUtil.getAndroidId(requireContext()),userPreferences.getUseID())
//        commonViewModel.getdistrictListAPI(AppUtil.getSavedTokenPreference(requireContext()),AppUtil.getAndroidId(requireContext()),userPreferences.getUseID())
        val value = AppUtil.getSavedEntityPreference(requireContext())
        val lastTwoDigits = value.takeLast(2)
        commonViewModel.getdistrictListAPI(DistrictListReq(lastTwoDigits, BuildConfig.VERSION_NAME))
        collectFollowTypeResponse()

//        setupFilters()

        DistrictAdapter()

        Institutelistner()
//        setupAdapter()


    }


    private fun setupAdapter() {
        settlementVeryfiationAdapter = SettlementBatchAdapter(batchList)
        binding.rvBatch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBatch.adapter = settlementVeryfiationAdapter






    }

    private fun Institutelistner() {
        //Adapter Follow Up Type
        InstituteAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, InstituteName
        )
        binding.spinnerInstitute.setAdapter(InstituteAdapter)
        binding.spinnerInstitute.setOnItemClickListener { parent, view, position, id ->
            val selected = InstituteList[position]
            selectedInstituteIdValue = selected.instituteId
            commonViewModel.getsettledbatchAPI(SettlementVeryficationBatchReq(BuildConfig.VERSION_NAME,selected.instituteId))
            collectBatchesData()
        }

    }

    private fun DistrictAdapter() {
        //Adapter Follow Up Type


        DistrictAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, DistrictName
        )
        binding.spinnerDistrict.setAdapter(DistrictAdapter)
        binding.spinnerDistrict.setOnItemClickListener { parent, view, position, id ->
            val selected = DistrictList[position]
            selectedDistrictIdValue = selected.districtCode
            // Clear institute data
            InstituteAdapter.clear()
            InstituteName.clear()
            binding.spinnerInstitute.setText("", false)

            // Clear batch data
            batchList.clear()
            binding.rvBatch.adapter = null

//            settlementVeryfiationAdapter.update(emptyList()) // IMPORTANT

            InstituteAdapter.clear()
            InstituteName.clear()
            binding.spinnerInstitute.text
            val request = InstituteListReq(
                appVersion = BuildConfig.VERSION_NAME,
                login = userPreferences.getUseID(),
                imeiNo = AppUtil.getAndroidId(requireContext()),
                districtCode = selected.districtCode
//                districtCode = "0551"
            )

            commonViewModel.instituteListAPI(
                token = AppUtil.getSavedTokenPreference(requireContext()).removePrefix("Bearer ").trim(),
                request = request
            )

            InstituteListResponse()


            //Adapter Follow Up Type

        }

    }

    fun update(newList: List<SettlementPercentage>) {
        batchList.clear()
        batchList.addAll(newList)
//        notifyDataSetChanged()
    }
    private fun InstituteListResponse() {

        lifecycleScope.launch {
            commonViewModel.instituteListAPI.collectLatest { resource ->

                when (resource) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Error -> {
                        hideProgressBar()

                        Toast.makeText(
                            requireContext(),
                            resource.error?.message ?: "Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Success -> {
                        hideProgressBar()

                        val list = resource.data?.wrappedList

                        if (!list.isNullOrEmpty()) {

                            // 1️⃣ Assign full list
                            InstituteList = list

                            // 2️⃣ Populate district names
                            InstituteName.clear()
                            for (item in list) {
                                InstituteName.add(item.instituteName)
                            }

                            // 3️⃣ Notify adapter
                            InstituteAdapter.notifyDataSetChanged()

                        } else {
                            showSnackBar("No district data available")
                        }
                    }
                }
            }
        }
    }

    private fun collectFollowTypeResponse() {
        lifecycleScope.launch {
            commonViewModel.districtList.collectLatest { resource ->

                when (resource) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        showSnackBar(resource.error?.message ?: "Internal Server Error")
                    }

                    is Resource.Success -> {
                        hideProgressBar()

                        val list = resource.data?.districtList

                        if (!list.isNullOrEmpty()) {

                            // 1️⃣ Set adapter data
                            DistrictList = list

                            // 2️⃣ Populate the name list
                            DistrictName.clear()
                            for (x in list) {
                                DistrictName.add(x.districtName)
                            }

                            // 3️⃣ Notify adapter
                            DistrictAdapter.notifyDataSetChanged()

                        } else {
                            showSnackBar("No district data available")
                        }
                    }
                }
            }
        }












    }



    private fun collectBatchesData() {


        lifecycleScope.launch {
            commonViewModel.getsettledbatchAPI.collectLatest { resource ->
                when (resource) {

                    is Resource.Loading -> showProgressBar()

                    is Resource.Error -> {
                        hideProgressBar()
                        showSnackBar(resource.error?.message ?: "Internal Server Error")
                        batchList.clear()
                        binding.rvBatch.adapter = null

                    }

                    is Resource.Success -> {
                        hideProgressBar()

                        val list = resource.data?.wrappedList
                        if (!list.isNullOrEmpty()) {
                            batchList.clear()
                            batchList.addAll(list)
                            setupAdapter()
//                            settlementVeryfiationAdapter.notifyDataSetChanged()
                        } else {
                            batchList.clear()
                            binding.rvBatch.adapter = null
                            showSnackBar("No settlement data available")
                        }
                    }
                }
            }
        }
        }
    }

