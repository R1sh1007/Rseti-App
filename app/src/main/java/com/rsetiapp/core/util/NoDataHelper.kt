package com.rsetiapp.core.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsetiapp.R
import com.rsetiapp.databinding.LayoutNoDataFoundBinding

class NoDataHelper {

    companion object {

        private const val NO_DATA_TAG = "NO_DATA_VIEW"

        fun showNoData(
            parent: ViewGroup,
            title: String? = null,
            description: String? = null,
            iconRes: Int? = null,
           // showActionButton: Boolean = false,
           // actionText: String? = null,
           // onActionClick: (() -> Unit)? = null
        ): View {

            hideNoData(parent)

            val inflater = LayoutInflater.from(parent.context)
            val binding = LayoutNoDataFoundBinding.inflate(inflater, parent, false)

            binding.root.tag = NO_DATA_TAG

            binding.tvTitle.text = title ?: parent.context.getString(R.string.no_data_title)
            binding.tvDescription.text = description ?: parent.context.getString(R.string.no_data_description)

            iconRes?.let { binding.ivNoData.setImageResource(it) }

//            // Action Button
//            if (showActionButton) {
//                binding.btnAction.visibility = View.VISIBLE
//                binding.btnAction.text = actionText ?: parent.context.getString(R.string.retry)
//                binding.btnAction.setOnClickListener { onActionClick?.invoke() }
//            } else {
//                binding.btnAction.visibility = View.GONE
//            }

            parent.addView(binding.root)
            return binding.root
        }

        fun hideNoData(parent: ViewGroup) {
            val view = parent.findViewWithTag<View>(NO_DATA_TAG)
            view?.let {
                parent.removeView(it)
            }
        }

        fun isNoDataShowing(parent: ViewGroup): Boolean {
            return parent.findViewWithTag<View>(NO_DATA_TAG) != null
        }
    }
}
