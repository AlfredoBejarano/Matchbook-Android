package me.alfredobejarano.golfassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_match.*
import me.alfredobejarano.golfassistant.databinding.FragmentCreateMatchBinding

class CreateMatchFragment : DialogFragment() {
    companion object {
        const val SHOW_TAG = "CREATE_MATCH_FRAGMENT"
    }

    private lateinit var binding: FragmentCreateMatchBinding
    private lateinit var listener: (name: String) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, state: Bundle?
    ): View = FragmentCreateMatchBinding.inflate(inflater, container, false).apply {
        binding = this
        setupAddButton()
    }.root


    override fun onStart() {
        super.onStart()
        dialog?.window?.run { setLayout(MATCH_PARENT, WRAP_CONTENT) }
    }

    fun addButtonListener(listener: (name: String) -> Unit): CreateMatchFragment {
        this.listener = listener
        return this
    }

    private fun setupAddButton() = binding.matchCreateButton.setOnClickListener {
        sendPlayerNameToObserver(binding.playerNameInput.text?.toString(), listener)
    }

    private fun sendPlayerNameToObserver(name: String?, listener: (name: String) -> Unit) =
        if (evaluateName(name)) {
            listener(name.orEmpty())
            dismissAllowingStateLoss()
        } else {
            playerNameInput.error = getString(R.string.player_name_too_short)
        }

    private fun evaluateName(name: String?) = when {
        name.isNullOrBlank() -> false
        name.length < 2 -> false
        else -> true
    }
}