package com.rsetiapp.common.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rsetiapp.BuildConfig
import com.rsetiapp.R
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.model.request.SdrListReq
import com.rsetiapp.common.model.response.VisitData
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.basecomponent.BaseRecyclerAdapter
import com.rsetiapp.core.geoFancing.GeofenceHelper
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.toastLong
import com.rsetiapp.databinding.FragmentSdrListBinding
import com.rsetiapp.databinding.ItemSdrDataBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SdrListFragment : BaseFragment<FragmentSdrListBinding>(FragmentSdrListBinding::inflate) {

    private val viewModel: CommonViewModel by activityViewModels()

    private lateinit var geofenceHelper: GeofenceHelper
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var adapter: BaseRecyclerAdapter<VisitData, ItemSdrDataBinding>
    private var sdrList: MutableList<VisitData> = mutableListOf()
    private var formName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())
        formName = arguments?.getString("formName").orEmpty()

        geofenceHelper = GeofenceHelper(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        checkLocationPermission()
        setupRecycler()
        setupListeners()
        getSdrList()
        collectSdrListResponse()
    }

    private fun setupRecycler() {
        adapter = BaseRecyclerAdapter(
            items = sdrList,
            bindingInflater = ItemSdrDataBinding::inflate,
            diffChecker  = {old, new -> old.instituteId == new.instituteId},
            onBind = { item, binding, _ ->
                binding.apply {
                    tvInstituteName.text = item.instituteName
                    tvFinYear.text = context?.getString(R.string.fin_year) + "  ${item.finYear}"
                    tvMonth.text = context?.getString(R.string.month) + "  ${getMonthName(item.month)}"
                    tvStatus.text = item.sdrVisitStatus ?: "Not Available"
                    statusImage.setImageResource(
                        if (item.sdrVisitStatus.equals("Completed", ignoreCase = true))
                            com.rsetiapp.R.drawable.ic_verified
                        else
                            com.rsetiapp.R.drawable.baseline_pending_24
                    )
                }
            },
            onItemClick = { item, _ ->
                handleItemClick(item)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter =adapter
        }

    }

    private fun setupListeners() {
          binding.apply {
              formText.text = formName
              backButton.setOnClickListener {
                  findNavController().navigateUp()
              }
          }
    }

    private fun getSdrList() {
        viewModel.getSdrListApi(
            AppUtil.getSavedTokenPreference(requireContext()),
            SdrListReq(
                BuildConfig.VERSION_NAME,
                AppUtil.getAndroidId(requireContext()),
                userPreferences.getUseID()
            )
        )
    }

    private fun collectSdrListResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(viewModel.getSdrListApi) {
                when (it) {
                    is Resource.Loading -> showProgressBar()

                    is Resource.Error -> {
                        hideProgressBar()
                        showSnackBar("Internal Server Error")
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        val response = it.data ?: return@collectLatestLifecycleFlow
                        when (response.responseCode) {
                            200 -> {
                                sdrList.clear()
                                sdrList.addAll(response.wrappedList)
                                adapter.update(response.wrappedList)
                              //  adapter.notifyDataSetChanged()
                            }
                            401 -> {
                                AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                            }
                            else -> toastLong(response.responseDesc)
                        }
                    }
                }
            }
        }
    }

    private fun handleItemClick(item: VisitData) {
        val lat = item.lattitude.toDoubleOrNull() ?: return
        val lng = item.longitude.toDoubleOrNull() ?: return
        val radius = item.radius.toFloat()
        getCurrentLocation { location ->
            if (location != null) {
                val isInside = isUserInsideGeofence(location, lat, lng, radius)
                if (isInside) {
                    findNavController().navigate(
                        SdrListFragmentDirections.actionSdrListFragmentToSdrVisitReport(
                            formName = formName,
                            rsetiInstituteName = item.instituteName,
                            finYear = item.finYear,
                            rsetiInstituteId = item.instituteId.toString(),
                            monthCode = item.month.toString()
                        )
                    )
                } else {
                    toastLong("❌ You are outside the institute area")
                }

            } else {
                toastLong("❌ Unable to fetch current location")
            }
        }
    }

    private fun getCurrentLocation(callback: (Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            toastLong("❌ Location permission not granted")
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { callback(it) }
            .addOnFailureListener { callback(null) }
    }

    private fun isUserInsideGeofence(
        currentLocation: Location,
        lat: Double,
        lng: Double,
        radius: Float
    ): Boolean {
        val target = Location("").apply {
            latitude = lat
            longitude = lng
        }
        return currentLocation.distanceTo(target) <= radius
    }

    private fun checkLocationPermission() {
        val granted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        }
    }

    private fun getMonthName(month: Int): String = when (month) {
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
        else -> "Unknown"
    }
}
