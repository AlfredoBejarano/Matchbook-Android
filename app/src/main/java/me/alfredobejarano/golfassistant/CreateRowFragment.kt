package me.alfredobejarano.golfassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_create_row.*
import me.alfredobejarano.golfassistant.data.MatchResult
import me.alfredobejarano.golfassistant.databinding.FragmentCreateRowBinding

class CreateRowFragment : DialogFragment() {
    companion object {
        const val SHOW_TAG = "CREATE_ROW_FRAGMENT"
    }

    private var withHandicap = true
    private lateinit var binding: FragmentCreateRowBinding
    private lateinit var listener: (bet: Double, match: String, result: MatchResult, handicap: Int?) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, state: Bundle?
    ): View = FragmentCreateRowBinding.inflate(inflater, container, false).apply {
        handicapInputContainer.visibility = if (withHandicap) View.VISIBLE else View.GONE
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddButton()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run { setLayout(MATCH_PARENT, WRAP_CONTENT) }
    }

    fun addButtonListener(listener: (bet: Double, match: String, result: MatchResult, handicap: Int?) -> Unit): CreateRowFragment {
        this.listener = listener
        return this
    }

    private fun setupAddButton() = binding.addRowButton.setOnClickListener {
        sendFieldToObserver(listener)
    }

    private fun sendFieldToObserver(listener: (bet: Double, match: String, result: MatchResult, handicap: Int?) -> Unit) {
        checkFields(handicapInput, matchInput, moneyAmountInput) {
            listener(
                moneyAmountInput.text?.toString()?.toDoubleOrNull() ?: 0.0,
                matchInput.text?.toString().orEmpty(),
                getMatchResult(),
                handicapInput.text?.toString()?.toIntOrNull()
            )
            dismissAllowingStateLoss()
        }
    }

    fun setHandicap(handicap: Boolean) {
        withHandicap = handicap
    }

    private fun checkField(editText: EditText?): Boolean {
        val empty = editText?.text.isNullOrBlank()
        if (empty) {
            editText?.error = getString(R.string.please_fill_this_field)
        }
        return !empty
    }

    private fun getMatchResult() = when (binding.matchResult.checkedRadioButtonId) {
        R.id.won_match -> MatchResult.WIN
        R.id.loss_match -> MatchResult.LOSS
        else -> MatchResult.TIE
    }

    private fun checkFields(vararg views: EditText?, onSuccess: () -> Unit) {
        var filled = false
        if (!views.isNullOrEmpty()) {
            views.forEach {
                filled = checkField(it)
                if (!filled) return@forEach
            }
        }
        if (filled) {
            onSuccess()
        }
    }
}