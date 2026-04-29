package com.example.todolist.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.todolist.MainActivity
import com.example.todolist.R
import com.example.todolist.database.TodoEntity
import com.example.todolist.database.toTodoEntity
import com.example.todolist.model.ToDoStatus
import com.example.todolist.model.TodoDetails
import java.util.Date

class AddWordFragment : Fragment(R.layout.add_new_word) {

    companion object {
        private const val ARG_TODO = "edit_todo"

        // Call this when editing an existing item
        fun newInstance(todo: TodoDetails): AddWordFragment {
            val fragment = AddWordFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_TODO, todo)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTitle    = view.findViewById<EditText>(R.id.etTitle)
        val etDetails  = view.findViewById<EditText>(R.id.etDetails)
        val etMeaning  = view.findViewById<EditText>(R.id.etMeaning)
        val etSynonyms = view.findViewById<EditText>(R.id.etSynonyms)
        val btnAdd     = view.findViewById<Button>(R.id.btnAdd)

        // Check if editing an existing todo
        val existingTodo = arguments?.getSerializable(ARG_TODO) as? TodoDetails
        existingTodo?.let {
            etTitle.setText(it.title)
            etDetails.setText(it.details)
            etMeaning.setText(it.meaning)
            etSynonyms.setText(it.synonyms)
            btnAdd.text = "Update"
        }

        val repo = (activity as MainActivity).repo

        btnAdd.setOnClickListener {
            val title    = etTitle.text.toString().trim()
            val details  = etDetails.text.toString().trim()
            val meaning  = etMeaning.text.toString().trim()
            val synonyms = etSynonyms.text.toString().trim()

            if (title.isEmpty() || details.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Thread {
                if (existingTodo != null) {
                    // UPDATE existing
                    val updated = TodoEntity(
                        id       = existingTodo.id.toInt(),
                        title    = title,
                        meaning  = meaning.ifEmpty { null },
                        synonyms = synonyms.ifEmpty { null },
                        details  = details,
                        status   = existingTodo.status,
                        date     = existingTodo.date
                    )
                    repo.update(updated)
                } else {
                    // INSERT new
                    val newEntity = TodoEntity(
                        title    = title,
                        meaning  = meaning.ifEmpty { null },
                        synonyms = synonyms.ifEmpty { null },
                        details  = details,
                        status   = ToDoStatus.NEW,
                        date     = Date()
                    )
                    repo.insert(newEntity)
                }

                activity?.runOnUiThread {
                    val msg = if (existingTodo != null) "Word updated!" else "Word added!"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    (activity as MainActivity).closeAddWord()
                }
            }.start()
        }
    }
}