package com.vs.views.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.vs.R
import com.vs.models.Note
import com.vs.utils.Result
import com.vs.utils.Utils
import com.vs.veronica.utils.C
import com.vs.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_add_note.*


/**
 * Created By Sachin
 */
class AddNoteFragment : Fragment() {

    private val notesViewModel by lazy { ViewModelProviders.of(this).get(NotesViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelChanges()
        btnSaveNote.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = noteTitleTextInputEditText?.text.toString()
        val desc = noteDescTextInputEditText?.text.toString()
        if (title.isEmpty() || desc.isEmpty()) {
            Utils.showToastMessage("Please enter required details!")
            return
        }
        activity?.also { notesViewModel.addNote(it, title, desc) }
    }

    private fun observeViewModelChanges() {
        notesViewModel.noteAdded.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Result.Success<Note> -> {
                    goToNoteDetailsScreen(it.data)
                }
                is Result.Failure -> {
                    Utils.showToastMessage("Something went wrong ${it.throwable.localizedMessage}")
                }
            }
        })
    }

    private fun goToNoteDetailsScreen(note: Note) {
        val noteDetailsFragment = NoteDetailsFragment()
        val bundle = Bundle()
        bundle.putSerializable(C.NOTE, note)
        noteDetailsFragment.arguments = bundle

        activity?.also {
            it.supportFragmentManager.beginTransaction()
                .add(R.id.rlContainer, noteDetailsFragment,C.NOTE_DETAILS).addToBackStack("NoteDetailsFragment")
                .commitAllowingStateLoss()
        }
    }

}
