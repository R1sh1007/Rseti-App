package com.rsetiapp.common.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsetiapp.BuildConfig
import com.rsetiapp.R
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.model.response.Batch
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.basecomponent.BaseRecyclerAdapter
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.AppUtil.convertMonthNumberToFullName
import com.rsetiapp.core.util.AppUtil.extractMonthFromDate
import com.rsetiapp.core.util.AppUtil.extractYearFromDate
import com.rsetiapp.core.util.AppUtil.getCurrentYear
import com.rsetiapp.core.util.NoDataHelper
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.toastLong
import com.rsetiapp.databinding.FragmentSettlementBatchBinding
import com.rsetiapp.databinding.ItemBatchBinding
import com.rsetiapp.databinding.ItemBatchBinding.inflate
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class SettlementBatchFragment :   BaseFragment<FragmentSettlementBatchBinding>(FragmentSettlementBatchBinding::inflate) {

    private var formName = ""

    private lateinit var batchAdapter: BaseRecyclerAdapter<Batch, ItemBatchBinding>
    private val batchList = mutableListOf<Batch>()
    private val batchFilteredList = mutableListOf<Batch>()

    private val commonViewModel: CommonViewModel by activityViewModels()

    private lateinit var yearAdapter: ArrayAdapter<String>
    private lateinit var monthAdapter: ArrayAdapter<String>

    private var years = arrayListOf<String>()
    private var months = arrayListOf<String>()

    private var selectedYear = "All"
    private var selectedMonth = "All"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        formName = arguments?.getString("formName").toString()

        init()
        collectBatchesData()
        if (batchFilteredList.isEmpty()) {
            NoDataHelper.showNoData(binding.container, title = "No Data Found")
        } else {
            NoDataHelper.hideNoData(binding.container)
        }
    }

    private fun init() {
        binding.tvTitleName.text = formName

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        years = (arrayListOf("All") + (2024..getCurrentYear()).map { it.toString() }) as ArrayList<String>
        months = (arrayListOf("All") + (1..12).map { convertMonthNumberToFullName(it) }) as ArrayList<String>

        setupAdapter()
        setupFilters()

    }


    private fun setupAdapter() {
        batchAdapter = BaseRecyclerAdapter(
            items = batchFilteredList,
            bindingInflater = ItemBatchBinding::inflate,

            onBind = { batch, binding, _ ->
                binding.tvBatchIdName.text = "-"+batch.batchRegNumber
                binding.tvBatchName.text = "-"+batch.batchName
            },

//            diffChecker = { old, new ->
//                old.batchCode == new.batchCode
//            },

            onItemClick = { batch, _ ->
                val action =
                    FollowUpBatchFragmentDirections.actionFollowUpBatchFragmentToFollowUpCandidateFragment(
                        batch.batchCode ?: "0",
                        batch.batchName ?: "Batch Name"
                    )
                findNavController().navigate(action)
            }
        )
        binding.rvSettleMent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSettleMent.adapter = batchAdapter
    }


    private fun setupFilters() {

        yearAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            years
        )
        binding.spinnerYear.setAdapter(yearAdapter)

        monthAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            months
        )

        binding.spinnerYear.setOnItemClickListener { parent, _, position, _ ->
            selectedYear = parent.getItemAtPosition(position).toString()
            updateBatchList()
        }
//        binding.spinnerMonth.setAdapter(monthAdapter)
//
//
//
//        binding.spinnerMonth.setOnItemClickListener { parent, _, position, _ ->
//            selectedMonth = parent.getItemAtPosition(position).toString()
//            updateBatchList()
//        }
    }


    private fun updateBatchList() {
        val filtered = batchList.filter { batch ->

            val batchYear = extractYearFromDate(batch.complitionDate)
            val batchMonth = extractMonthFromDate(batch.complitionDate)

            when {
                selectedYear == "All" && selectedMonth == "All" -> true

                selectedYear != "All" && selectedMonth == "All" ->
                    batchYear == selectedYear

                selectedYear == "All" && selectedMonth != "All" ->
                    batchMonth.equals(selectedMonth, ignoreCase = true)

                else ->
                    batchYear == selectedYear &&
                            batchMonth.equals(selectedMonth, ignoreCase = true)
            }
        }

        batchFilteredList.clear()
        batchFilteredList.addAll(filtered)

        batchAdapter.update(batchFilteredList)
        //batchAdapter.notifyDataSetChanged()
    }


    private fun collectBatchesData() {
        commonViewModel.getBatchAPI(
            AppUtil.getSavedTokenPreference(requireContext()),
            BuildConfig.VERSION_NAME,
            userPreferences.getUseID(),
            AppUtil.getAndroidId(requireContext()),
            AppUtil.getSavedEntityPreference(requireContext())
        )

        lifecycleScope.launch {
            commonViewModel.getBatchAPI.collectLatest { resource ->
                when (resource) {

                    is Resource.Loading -> showProgressBar()

                    is Resource.Error -> {
                        hideProgressBar()
                        showSnackBar("Internal Server Error")
                    }

                    is Resource.Success -> {
                        hideProgressBar()

                        resource.data?.let { response ->

                            when (response.responseCode) {

                                200 -> {
                                    NoDataHelper.hideNoData(binding.container)
                                    batchList.clear()
                                    batchList.addAll(response.wrappedList)

                                    batchFilteredList.clear()
                                    batchFilteredList.addAll(batchList)

                                    batchAdapter.update(batchFilteredList)
                                    //batchAdapter.notifyDataSetChanged()

                                }

                                301 -> showSnackBar("Please update the app from PlayStore")

                                401 -> AppUtil.showSessionExpiredDialog(
                                    findNavController(),
                                    requireContext()
                                )

                                else ->{ toastLong(response.responseDesc)
                                    NoDataHelper.showNoData(
                                        parent = binding.container,
                                        title =response.responseDesc,
                                        iconRes = R.drawable.no_data,
                                    )
                                }
                            }

                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }
}
