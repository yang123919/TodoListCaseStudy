package com.example.todolist.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.todolist.MainActivity
import com.example.todolist.R
import com.example.todolist.model.TodoDetails

class WordDetailsFragment : Fragment(R.layout.word_details_fragment) {

    companion object {
        private const val ARG_WORD = "word_item"

        fun newInstance(todo: TodoDetails): WordDetailsFragment {
            val fragment = WordDetailsFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_WORD, todo)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todo = arguments?.getSerializable(ARG_WORD) as? TodoDetails

        val tvTitle    = view.findViewById<TextView>(R.id.tvWordTitle)
        val tvMeaning  = view.findViewById<TextView>(R.id.tvMeaning)
        val tvSynonyms = view.findViewById<TextView>(R.id.tvSynonyms)
        val tvDetails  = view.findViewById<TextView>(R.id.tvDetails)
        val btnDone    = view.findViewById<Button>(R.id.btnDone)
        val btnUpdate  = view.findViewById<Button>(R.id.btnUpdate)
        val btnDelete  = view.findViewById<Button>(R.id.btnDelete)

        tvTitle.text    = todo?.title    ?: "-"
        tvMeaning.text  = todo?.meaning  ?: "-"
        tvSynonyms.text = todo?.synonyms ?: "-"
        tvDetails.text  = todo?.details  ?: "-"

        // Move to completed
        btnDone.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Move this word to completed list?")
                .setPositiveButton("Yes") { _, _ ->
                    todo?.let { (activity as MainActivity).moveToCompleted(it) }
                }
                .setNegativeButton("No", null)
                .show()
        }

        // Edit word — opens AddWordFragment pre-filled
        btnUpdate.setOnClickListener {
            todo?.let { (activity as MainActivity).openEdit(it) }
        }

        // Delete word
        btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Word")
                .setMessage("This action cannot be undone.")
                .setPositiveButton("Delete") { _, _ ->
                    todo?.let { (activity as MainActivity).deleteTodo(it) }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}