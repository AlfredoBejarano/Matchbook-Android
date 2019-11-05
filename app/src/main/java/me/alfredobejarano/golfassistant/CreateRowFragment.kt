package me.alfredobejarano.golfassistant

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_create_row.*
import me.alfredobejarano.golfassistant.databinding.FragmentCreateRowBinding
import me.alfredobejarano.golfassistant.utils.toFloat

class CreateRowFragment : DialogFragment() {
    companion object {
        const val SHOW_TAG = "CREATE_ROW_FRAGMENT"
    }

    private lateinit var binding: FragmentCreateRowBinding
    private lateinit var listener: (won: Float, loss: Float) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, state: Bundle?
    ): View = FragmentCreateRowBinding.inflate(inflater, container, false).apply {
        binding = this
        setupAddButton()
        setupEarningListeners()
    }.root

    override fun onStart() {
        super.onStart()
        dialog?.window?.run { setLayout(MATCH_PARENT, WRAP_CONTENT) }
    }

    fun addButtonListener(listener: (won: Float, loss: Float) -> Unit): CreateRowFragment {
        this.listener = listener
        return this
    }

    private fun setupEarningListeners() = binding.apply {
        wonInput.run(::setupEarningListener)
        lossInput.run(::setupEarningListener)
    }

    @SuppressLint("SetTextI18n")
    private fun setupEarningListener(view: TextInputEditText) = view.addTextChangedListener {
        val viewValue = it.toFloat()
        val isWinView = view.id == wonInput.id
        val otherViewValue = if (isWinView) {
            lossInput.text
        } else {
            wonInput.text
        }.toFloat()

        binding.totalRow.text =
            "$${if (isWinView) viewValue - otherViewValue else otherViewValue - viewValue}"
    }

    private fun setupAddButton() = binding.addRowButton.setOnClickListener {
        sendFieldToObserver(listener)
    }

    private fun sendFieldToObserver(listener: (won: Float, loss: Float) -> Unit) {
        listener(
            wonInput.text?.toString()?.toFloatOrNull() ?: 0f,
            lossInput.text?.toString()?.toFloatOrNull() ?: 0f
        )
        dismissAllowingStateLoss()
    }
}