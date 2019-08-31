package me.alfredobejarano.golfassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_row.*
import me.alfredobejarano.golfassistant.databinding.FragmentCreateRowBinding

class CreateRowFragment : DialogFragment() {
    companion object {
        const val SHOW_TAG = "CREATE_ROW_FRAGMENT"
    }

    private lateinit var binding: FragmentCreateRowBinding
    private lateinit var listener: (handicap: Int, match: Int, won: Float, loss: Float) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, state: Bundle?
    ): View = FragmentCreateRowBinding.inflate(inflater, container, false).apply {
        binding = this
        setupAddButton()
        setupEarningListeners()
    }.root


    fun addButtonListener(listener: (handicap: Int, match: Int, won: Float, loss: Float) -> Unit): CreateRowFragment {
        this.listener = listener
        return this
    }

    private fun setupEarningListeners() {
        setupWonEarnings()
        setupLossEarnings()
    }

    private fun setupWonEarnings() = binding.wonInput.addTextChangedListener {
        val value = it?.toString()?.toFloatOrNull() ?: 0f
        val lossValue = binding.lossInput?.text?.toString()?.toFloatOrNull() ?: 0f
        binding.totalRow.text = (value - lossValue).toString()
    }

    private fun setupLossEarnings() = binding.lossInput.addTextChangedListener {
        val value = it?.toString()?.toFloatOrNull() ?: 0f
        val wonValue = binding.wonInput?.text?.toString()?.toFloatOrNull() ?: 0f
        binding.totalRow.text = (wonValue - value).toString()
    }

    private fun setupAddButton() = binding.addRowButton.setOnClickListener {
        sendFieldToObserver(listener)
    }

    private fun sendFieldToObserver(listener: (handicap: Int, match: Int, won: Float, loss: Float) -> Unit) =
        if (evaluateFields() == FieldValidationError.FIELDS_OK) {
            listener(
                handicapInput.text?.toString()?.toIntOrNull() ?: 0,
                matchInput.text?.toString()?.toIntOrNull() ?: 0,
                wonInput.text?.toString()?.toFloatOrNull() ?: 0f,
                lossInput.text?.toString()?.toFloatOrNull() ?: 0f
            )
            dismissAllowingStateLoss()
        } else {
            displayErrorMessage()
        }

    private fun evaluateFields() = binding.run {
        validateFields(
            handicapInput.text?.toString(),
            matchInput.text?.toString(),
            wonInput.text?.toString(),
            lossInput.text?.toString(),
            totalRow.text?.toString()
        )
    }

    private fun displayErrorMessage() =
        Snackbar.make(binding.root, R.string.player_name_too_short, Snackbar.LENGTH_SHORT).show()

    private fun validateFields(
        handicap: String?,
        match: String?,
        won: String?,
        loss: String?,
        total: String?
    ) = when {
        handicap.isNullOrEmpty() || handicap.toIntOrNull() == null -> FieldValidationError.HANDICAP_INVALID
        match.isNullOrEmpty() || match.toIntOrNull() == null -> FieldValidationError.MATCH_INVALID
        won.isNullOrEmpty() || won.toFloatOrNull() == null -> FieldValidationError.WON_EARNINGS_INVALID
        loss.isNullOrEmpty() || loss.toFloatOrNull() == null -> FieldValidationError.LOSS_EARNING_INVALID
        total.isNullOrEmpty() || total.toFloatOrNull() == null -> FieldValidationError.TOTAL_INVALID
        else -> FieldValidationError.FIELDS_OK
    }

    enum class FieldValidationError {
        HANDICAP_INVALID,
        MATCH_INVALID,
        WON_EARNINGS_INVALID,
        LOSS_EARNING_INVALID,
        TOTAL_INVALID,
        FIELDS_OK
    }
}