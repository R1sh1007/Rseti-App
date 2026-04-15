package com.rsetiapp.common.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsetiapp.BuildConfig
import com.rsetiapp.R
import com.rsetiapp.bhashini.TranslationHelper
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
import com.rsetiapp.databinding.FragmentFollowUpBatchBinding
import com.rsetiapp.databinding.ItemBatchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowUpBatchFragment :
    BaseFragment<FragmentFollowUpBatchBinding>(FragmentFollowUpBatchBinding::inflate) {

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
    lateinit var translationHelper: TranslationHelper
    val translationCache = HashMap<String, String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        formName = arguments?.getString("formName").toString()
        translationHelper = TranslationHelper()
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
        binding.rvBatch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBatch.adapter = batchAdapter
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
        binding.spinnerMonth.setAdapter(monthAdapter)

        binding.spinnerYear.setOnItemClickListener { parent, _, position, _ ->
            selectedYear = parent.getItemAtPosition(position).toString()
            updateBatchList()
        }

        binding.spinnerMonth.setOnItemClickListener { parent, _, position, _ ->
            selectedMonth = parent.getItemAtPosition(position).toString()
            updateBatchList()
        }
    }


//    private fun updateBatchList() {
//        val filtered = batchList.filter { batch ->
//
//            val batchYear = extractYearFromDate(batch.complitionDate)
//            val batchMonth = extractMonthFromDate(batch.complitionDate)
//
//            when {
//                selectedYear == "All" && selectedMonth == "All" -> true
//
//                selectedYear != "All" && selectedMonth == "All" ->
//                    batchYear == selectedYear
//
//                selectedYear == "All" && selectedMonth != "All" ->
//                    batchMonth.equals(selectedMonth, ignoreCase = true)
//
//                else ->
//                    batchYear == selectedYear &&
//                            batchMonth.equals(selectedMonth, ignoreCase = true)
//            }
//        }
//
//        batchFilteredList.clear()
//        batchFilteredList.addAll(filtered)
//
//        batchAdapter.update(batchFilteredList)
//        //batchAdapter.notifyDataSetChanged()
//    }


    private fun updateBatchList() {

        val filtered = batchList.filter { batch ->

            val batchYear = extractYearFromDate(batch.complitionDate)
            val batchMonth = extractMonthFromDate(batch.complitionDate)

            when {
                selectedYear == "All" && selectedMonth == "All" -> true

                selectedYear != "All" && selectedMonth == "All" ->
                    batchYear == selectedYear

                selectedYear == "All" && selectedMonth != "All" ->
                    batchMonth.equals(selectedMonth, true)

                else ->
                    batchYear == selectedYear &&
                            batchMonth.equals(selectedMonth, true)
            }
        }

        // 🔥 IMPORTANT: direct update karo (clear/addAll hata do)
        batchAdapter.update(filtered)

        // debug ke liye
        Log.d("FILTER_DEBUG", "Filtered size: ${filtered.size}")
    }


    private fun convertMonthNumberToFullName(month: Int): String {
        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> ""
        }
    }
    private fun extractYearFromDate(date: String?): String {
        return try {
            date?.substring(0, 4) ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun extractMonthFromDate(date: String?): String {
        return try {
            val monthNumber = date?.substring(5, 7)?.toInt() ?: 0
            convertMonthNumberToFullName(monthNumber)
        } catch (e: Exception) {
            ""
        }
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

                                    // ✅ YAHI IMPORTANT LINE HAI
                                    updateBatchList()
                                }

                                301 -> showSnackBar("Please update the app from PlayStore")

                                401 -> AppUtil.showSessionExpiredDialog(
                                    findNavController(),
                                    requireContext()
                                )

                                else -> {
                                    toastLong(response.responseDesc)
                                    NoDataHelper.showNoData(
                                        parent = binding.container,
                                        title = response.responseDesc,
                                        iconRes = R.drawable.no_data,
                                    )
                                }
                            }

                        } ?: showSnackBar("Internal Server Error")
                    }
//                    is Resource.Success -> {
//                        hideProgressBar()
//
//                        resource.data?.let { response ->
//
//                            when (response.responseCode) {
//
//                                200 -> {
//                                    NoDataHelper.hideNoData(binding.container)
//                                    batchList.clear()
//                                    batchList.addAll(response.wrappedList)
//
//                                    batchFilteredList.clear()
//                                    batchFilteredList.addAll(batchList)
//
//                                    batchAdapter.update(batchFilteredList)
//                                    //batchAdapter.notifyDataSetChanged()
//
//                                }
//
//                                301 -> showSnackBar("Please update the app from PlayStore")
//
//                                401 -> AppUtil.showSessionExpiredDialog(
//                                    findNavController(),
//                                    requireContext()
//                                )
//
//                                else ->{ toastLong(response.responseDesc)
//                                    NoDataHelper.showNoData(
//                                        parent = binding.container,
//                                        title =response.responseDesc,
//                                        iconRes = R.drawable.no_data,
//                                    )
//                                }
//                            }
//
//                        } ?: showSnackBar("Internal Server Error")
//                    }
                }
            }
        }
    }
}
