package com.wakeupdev.joyfin.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.wakeupdev.joyfin.R
import com.wakeupdev.joyfin.databinding.FragmentHomeBinding
import com.wakeupdev.joyfin.models.networkres.ApiResponse
import com.wakeupdev.joyfin.models.networkres.Transaction
import com.wakeupdev.joyfin.utils.AppStringUtils
import com.wakeupdev.joyfin.utils.ModelUtils.networkTransactionToModel
import com.wakeupdev.joyfin.utils.UiUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

class HomeFragment : Fragment() {
    val TAG: String = HomeFragment::class.java.simpleName

    private var _binding: FragmentHomeBinding? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var smartSaverTransactions: List<Transaction> = emptyList()
    private var greenSaverTransactions: List<Transaction> = emptyList()
    private var fixedDepositTransactions: List<Transaction> = emptyList()
    private var smartSaverBalance = 0.0
    private var greenSaverBalance = 0.0
    private var fixedDepositBalance = 0.0
    private var apiResponse: ApiResponse? = null
    private var firstName = ""
    private var lastName = ""
    private var isEmailVerified = true
    private var emailAddress = ""
    private var tier = "zero"
    private var starList = emptyList<ImageView>()
    private lateinit var transactionAdapter: TransactionAdapter

    // Thproperty only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiResponse = requireActivity().intent.getParcelableExtra("responseBody") as? ApiResponse

        apiResponse?.let {
            if (it.userData != null) {
                smartSaverBalance = it.userData.smartSaverBalance
                greenSaverBalance = it.userData.greenSaverBalance
                fixedDepositBalance = it.userData.fixedDepositBalance

                firstName = it.userData.firstName ?: ""
                lastName = it.userData.lastName ?: ""
                emailAddress = it.userData.email ?: ""
                isEmailVerified = it.userData.emailVerified?.lowercase() == "true"

                tier = it.userData.tier ?: "zero"

                setSavingsAmounts()
            }

            if (it.smartSaverTransactions != null) {
                smartSaverTransactions = it.smartSaverTransactions
            }

            if (it.greenSaverTransactions != null) {
                greenSaverTransactions = it.greenSaverTransactions
            }

            if (it.fixedDepositTransactions != null) {
                fixedDepositTransactions = it.fixedDepositTransactions
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val total = smartSaverBalance + greenSaverBalance + fixedDepositBalance

        _binding?.totalBalance?.text = AppStringUtils.customAmountFormat(total)
        _binding?.givenName?.text = "$firstName $lastName"

        starList = listOf(
            binding.star1,
            binding.star2,
            binding.star3,
            binding.star4,
            binding.star5
        )

        rateTierStars()

        Log.d(TAG, "smart saver: $smartSaverTransactions")
        Log.d(TAG, "api response: $apiResponse")

        val transactions = networkTransactionToModel(
            smartSaverTransactions,
            "smart saver"
        ) + networkTransactionToModel(
            greenSaverTransactions,
            "green saver"
        ) + networkTransactionToModel(fixedDepositTransactions, "fixed deposit")

        _binding?.bottomSheet?.transactionRecycler?.adapter =
            TransactionAdapter(requireContext(), transactions)
        _binding?.bottomSheet?.transactionRecycler?.layoutManager =
            LinearLayoutManager(requireContext())

        //initialize bottom sheets
        bottomSheetBehavior = BottomSheetBehavior.from(_binding!!.bottomSheet.root)

        verificationDialog(isEmailVerified, emailAddress)

        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBottomSheets()
        _binding!!.bottomSheet.filter.setOnClickListener {
            showDateRangePicker()
        }

        //collapse the bottom sheet when back arrow icon from the bottom sheet toolbar clicked

        //collapse the bottom sheet when back arrow icon from the bottom sheet toolbar clicked
        _binding!!.bottomSheet.backButton.setOnClickListener { view ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setUpBottomSheets() {
        //based on the state of the bottom sheet , hide / show certain views
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                UiUtils.handleSchedulesBottomSheetState(
                    newState,
                    _binding!!,
                    ArrayList(),
                    requireContext()
                )
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun rateTierStars() {
        when (tier.lowercase()) {
            "one" -> binding.star1.setImageDrawable(drawableStarFilled)

            "two" -> starList.subList(0, 2).forEach {
                it.setImageDrawable(drawableStarFilled)
            }

            "three" -> starList.subList(0, 3).forEach {
                it.setImageDrawable(drawableStarFilled)
            }

            "four" -> starList.subList(0, 4).forEach {
                it.setImageDrawable(drawableStarFilled)
            }

            else -> starList.forEach {
                it.setImageDrawable(drawableStarFilled)
            }
        }
    }

    private val drawableStarOutline: Drawable?
        get() {
            return ResourcesCompat.getDrawable(
                resources,
                R.drawable.baseline_star_outline_24,
                null
            )
        }

    private fun setSavingsAmounts(){
        _binding?.fixedSavingsAmount?.text = AppStringUtils.customAmountFormat(fixedDepositBalance)
        _binding?.smartSavingsAmount?.text = AppStringUtils.customAmountFormat(smartSaverBalance)
        _binding?.greenSavingsAmount?.text = AppStringUtils.customAmountFormat(greenSaverBalance)
    }
    private val drawableStarFilled: Drawable?
        get() {
            return ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_star_filled,
                null
            )
        }


    private fun showDateRangePicker() {
        val constraintsBuilder =
            CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now())

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.MONTH, -6)

        val initialStartDate = Calendar.getInstance()
        initialStartDate.add(Calendar.WEEK_OF_YEAR, -1)

        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .setSelection(
                androidx.core.util.Pair(
                    initialStartDate.timeInMillis,
                    Calendar.getInstance().timeInMillis
                )
            )
            .setCalendarConstraints(
                CalendarConstraints.Builder().setStart(calendarStart.timeInMillis)
                    .setEnd(Calendar.getInstance().timeInMillis)
                    .setValidator(DateValidatorPointBackward.now())
                    .build()
            )

        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first // Long value representing milliseconds since epoch
            val endDate = selection.second  // Long value representing milliseconds since epoch

            // Convert milliseconds to formatted date strings (optional)
            val formattedStartDate = convertMillisToDateString(startDate)
            val formattedEndDate = convertMillisToDateString(endDate)

            Log.d(TAG, "Selected Start Date: $formattedStartDate")
            Log.d(TAG, "Selected End Date: $formattedEndDate")

            // Call your API/logic to fetch data based on start & end date range
//            fetchRemittanceDataWithDateRange(formattedStartDate, formattedEndDate)
        }

        datePicker.show(childFragmentManager, datePicker.toString())
    }

    // Optional function to convert milliseconds to formatted date string
    private fun convertMillisToDateString(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(millis)
        return formatter.format(date)
    }

    private fun verificationDialog(isEmailVerified: Boolean, emailAddress: String) {
        if (isEmailVerified) return

        val dialogView: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.verification_dialog, null, false)

        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val proceedButton = dialogView.findViewById<Button>(R.id.proceedButton)
        val emailEditText = dialogView.findViewById<EditText>(R.id.emailEditText)
        val iconEdit = dialogView.findViewById<ImageButton>(R.id.editEmail)
        val errorView = dialogView.findViewById<TextView>(R.id.errorMessage)

        iconEdit.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val isValidEmail = Pattern.matches(android.util.Patterns.EMAIL_ADDRESS.toString(), email)

            if (!isValidEmail && emailEditText.isEnabled) {
                errorView.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (emailEditText.isEnabled){
                emailEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
            }

            emailEditText.isEnabled = !emailEditText.isEnabled
            proceedButton.isEnabled = !emailEditText.isEnabled // disable proceed button during edit mode

            emailEditText.requestFocus()

            val resourceId = if (emailEditText.isEnabled) {
                R.drawable.ic_check
            } else {
                R.drawable.ic_edit_pencil
            }
            iconEdit.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    resourceId,
                    null
                )
            )
        }

        emailEditText.setText(emailAddress)
        emailEditText.hint = emailAddress

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasText = s?.toString()?.isNotEmpty() ?: false
                val drawable = if (hasText) ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_clear,
                    null
                ) else null
                emailEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )
                errorView.visibility = View.GONE

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        cancelButton.setOnClickListener { dialog.dismiss() }


//        AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .setCancelable(false)
//            .create()
//            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}