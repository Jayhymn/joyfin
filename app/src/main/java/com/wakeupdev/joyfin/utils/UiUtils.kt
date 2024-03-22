package com.wakeupdev.joyfin.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.wakeupdev.joyfin.R
import com.wakeupdev.joyfin.databinding.FragmentHomeBinding
import java.util.ArrayList

object UiUtils {
    fun handleSchedulesBottomSheetState(
        bottomSheetState: Int,
        fragmentHomeBinding: FragmentHomeBinding,
        arrayList: ArrayList<Any>,
        context: Context
    ) {
        when (bottomSheetState) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                fragmentHomeBinding.bottomSheet.indicator.visibility = View.GONE
                fragmentHomeBinding.bottomSheet.titleCollapse.visibility = View.GONE
                fragmentHomeBinding.bottomSheet.appBarLayout.visibility = View.VISIBLE
                fragmentHomeBinding.bottomSheet.root.setBackgroundColor(
                    ResourcesCompat.getColor(
                        context.resources, R.color.surface, null
                    )
                )
            }

            BottomSheetBehavior.STATE_DRAGGING -> {
                fragmentHomeBinding.bottomSheet.indicator.visibility = View.VISIBLE
                fragmentHomeBinding.bottomSheet.titleCollapse.visibility = View.GONE
                fragmentHomeBinding.bottomSheet.appBarLayout.visibility = View.GONE
                fragmentHomeBinding.bottomSheet.root.setBackgroundColor(
                    ResourcesCompat.getColor(
                        context.resources, R.color.highlight, null
                    )
                )
            }

            BottomSheetBehavior.STATE_COLLAPSED -> {
                fragmentHomeBinding.bottomSheet.indicator.visibility = View.VISIBLE
                fragmentHomeBinding.bottomSheet.titleCollapse.visibility = View.VISIBLE
                fragmentHomeBinding.bottomSheet.appBarLayout.visibility = View.GONE
                fragmentHomeBinding.bottomSheet.root.setBackgroundColor(
                    ResourcesCompat.getColor(
                        context.resources, R.color.highlight, null
                    )
                )
                fragmentHomeBinding.bottomSheet.root.setBackgroundResource(
                    R.drawable.bg_bottom_sheet
                )
            }
        }
    }
}