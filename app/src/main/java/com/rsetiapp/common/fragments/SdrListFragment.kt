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
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.adapter.SdrAdapter
import com.rsetiapp.common.model.request.SdrListReq
import com.rsetiapp.common.model.response.VisitData
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.geoFancing.GeofenceHelper
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.toastLong
import com.rsetiapp.databinding.FragmentSdrListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SdrListFragment : BaseFragment<FragmentSdrListBinding>(FragmentSdrListBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()
    private lateinit var geofenceHelper: GeofenceHelper
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var sdrAdapter: SdrAdapter
    private var sdrList: MutableList<VisitData> = mutableListOf()
    private var formName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        formName = arguments?.getString("formName").toString()
        geofenceHelper = GeofenceHelper(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()
        init()
    }

    private fun init() {
        commonViewModel.getSdrListApi(
            AppUtil.getSavedTokenPreference(requireContext()),
            SdrListReq(
                BuildConfig.VERSION_NAME,
                AppUtil.getAndroidId(requireContext()),

                userPreferences.getUseID()
            )
        )
        collectSdrListResponse()
        setupListeners()
    }

    private fun setupListeners() {
        binding.formText.text = formName
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        sdrAdapter = SdrAdapter(sdrList) { selectedItem ->
            val lat = selectedItem.lattitude.toDoubleOrNull() ?: return@SdrAdapter
            val lng = selectedItem.longitude.toDoubleOrNull() ?: return@SdrAdapter
            val radius = selectedItem.radius.toFloat()
            val instituteName = selectedItem.instituteName
            val finYear = selectedItem.finYear
            val instituteId = selectedItem.instituteId.toString()
            val monthCode = selectedItem.month

            getCurrentLocation { location ->
                if (location != null) {
                    val isInside = isUserInsideGeofence(location, lat, lng, radius)
                    // val isInside = isUserInsideGeofence(location, 26.2153, 84.3588, radius)


                    if (isInside) {

                        findNavController().navigate(SdrListFragmentDirections.actionSdrListFragmentToSdrVisitReport(formName,instituteName,finYear,instituteId,
                            monthCode.toString()
                        ))
                    } else {
                        toastLong("❌ You are outside the institute area")
                    }
                } else {
                    toastLong("❌ Failed to retrieve current location")
                }
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = sdrAdapter
        }
    }

    private fun collectSdrListResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getSdrListApi) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        showSnackBar("Internal Server Error")
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { response ->
                            if (response.responseCode == 200) {
                                sdrList.clear()
                                sdrList.addAll(response.wrappedList)
                                sdrAdapter.notifyDataSetChanged()
                            } else if (response.responseCode == 401) {
                                AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                            } else {
                                toastLong(response.responseDesc)
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        }
    }

    private fun getCurrentLocation(onLocationResult: (Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            toastLong("❌ Location permission not granted")
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            onLocationResult(location)
        }.addOnFailureListener {
            onLocationResult(null)
        }
    }

    private fun isUserInsideGeofence(
        currentLocation: Location,
        lat: Double,
        lng: Double,
        radius: Float
    ): Boolean {
        val targetLocation = Location("").apply {
            latitude = lat
            longitude = lng
        }
        val distance = currentLocation.distanceTo(targetLocation)
        return distance <= radius
    }
}
