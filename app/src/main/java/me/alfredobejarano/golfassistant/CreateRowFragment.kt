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
import me.alfredobejarano.golfassistant.databinding.FragmentCreateRowBinding

class CreateRowFragment : DialogFragment() {
    companion object {
        const val SHOW_TAG = "CREATE_ROW_FRAGMENT"
    }

    private var withHandicap = true
    private lateinit var binding: FragmentCreateRowBinding
    private lateinit var listener: (handicap: Int?, match: String, moneyAmount: Float, isLoss: Boolean) -> Unit

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

    fun addButtonListener(listener: (handicap: Int?, match: String, moneyAmount: Float, isLoss: Boolean) -> Unit): CreateRowFragment {
        this.listener = listener
        return this
    }

    private fun setupAddButton() = binding.addRowButton.setOnClickListener {
        sendFieldToObserver(listener)
    }

    private fun sendFieldToObserver(listener: (handicap: Int?, match: String, moneyAmount: Float, isLoss: Boolean) -> Unit) {
        checkFields(handicapInput, matchInput, moneyAmountInput) {
            listener(
                handicapInput.text?.toString()?.toIntOrNull(),
                matchInput.text?.toString().orEmpty(),
                moneyAmountInput.text?.toString()?.toFloatOrNull() ?: 0f,
                isLossCheckBox.isChecked
            )
            dismissAllowingStateLoss()
        }
    }

    fun setHandicap(handicap: Boolean) {
        withHandicap = handicap
    }

    private fun checkField(editText: EditText?): Boolean {
        val empty = editText?.text.isNullOrBlank()
        if(empty) {
            editText?.error = getString(R.string.please_fill_this_field)
        }
        return !empty
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