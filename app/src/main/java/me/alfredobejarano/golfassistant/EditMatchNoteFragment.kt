package me.alfredobejarano.golfassistant

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import me.alfredobejarano.golfassistant.databinding.FragmentEditMatchNoteBinding
import me.alfredobejarano.golfassistant.utils.viewBinding

@AndroidEntryPoint
class EditMatchNoteFragment : DialogFragment() {
    companion object {
        const val SHOW_TAG = "EDIT_MATCH_NOTE_FRAGMENT"
    }

    private var note: String? = null
    private val binding by viewBinding(FragmentEditMatchNoteBinding::inflate)
    private var listener: (dialog: DialogFragment, note: String) -> Unit = { _, _ -> }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        binding.root

    override fun onResume() {
        super.onResume()

        val displayMetrics = DisplayMetrics()
        dialog?.window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        val screenWidth = (displayMetrics.widthPixels * 0.9).toInt()
        val screenHeight = (displayMetrics.heightPixels * 0.5).toInt()

        val width =
            if (screenWidth > 0) screenWidth else resources.getDimensionPixelSize(R.dimen.dialog_size)
        val height =
            if (screenHeight > 0) screenHeight else resources.getDimensionPixelSize(R.dimen.dialog_size)

        dialog?.window?.setLayout(width, height)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        note?.run(noteFieldEditText::setText)
        addNoteButton.setOnClickListener { checkMessageField() }
    }

    fun setMessageListener(listener: (dialog: DialogFragment, note: String) -> Unit) {
        this.listener = listener
    }

    fun setNote(note: String?) {
        this.note = note
    }

    private fun checkMessageField() = binding.noteFieldEditText.run {
        val message = text?.toString()
        if (message.isNullOrBlank()) {
            error = requireContext().getString(R.string.please_fill_this_field)
        } else {
            listener(this@EditMatchNoteFragment, message)
        }
    }
}