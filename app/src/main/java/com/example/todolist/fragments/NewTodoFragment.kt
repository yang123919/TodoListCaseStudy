package com.example.todolist.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.MainActivity
import com.example.todolist.R
import com.example.todolist.database.toTodoDetails
import com.example.todolist.model.ToDoStatus
import com.example.todolist.model.TodoDetails
import com.example.todolist.recycleViewAdapters.TodoRecycleViewAdapter

class NewTodoFragment : Fragment(R.layout.new_todo_fragment) {

    private var sortOrder = "ascending"
    private var sortBy = "title"
    private var searchQuery = ""

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private var data: List<TodoDetails> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout)

        val btnAdd = view.findViewById<Button>(R.id.button)
        btnAdd.setOnClickListener {
            (activity as MainActivity).openEditOrAdd()
        }

        view.findViewById<EditText>(R.id.editTextText2).addTextChangedListener { text ->
            searchQuery = text.toString().trim()
            applyFilterAndSort()
        }

        view.findViewById<ImageButton>(R.id.imageButton3).setOnClickListener {
            SortDialogFragment(sortOrder, sortBy) { newOrder, newBy ->
                sortOrder = newOrder
                sortBy = newBy
                applyFilterAndSort()
            }.show(parentFragmentManager, "SortDialog")
        }
    }

    override fun onResume() {
        super.onResume()

        val btnAdd = view?.findViewById<Button>(R.id.button)
        btnAdd?.visibility = View.VISIBLE

        loadData()
    }
    private fun loadData() {
        val repo = (activity as MainActivity).repo
        Thread {
            data = repo.getByStatus(ToDoStatus.NEW).map { it.toTodoDetails() }
            activity?.runOnUiThread { applyFilterAndSort() }
        }.start()
    }

    private fun applyFilterAndSort() {
        val filtered = if (searchQuery.isEmpty()) data
        else data.filter { it.title.contains(searchQuery, ignoreCase = true) }

        val sorted = if (sortBy == "title") filtered.sortedBy { it.title.lowercase() }
        else filtered.sortedBy { it.date }

        val finalList = if (sortOrder == "ascending") sorted else sorted.reversed()
        updateList(finalList)
    }

    private fun updateList(list: List<TodoDetails>) {
        if (list.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = TodoRecycleViewAdapter(list)
        }
    }


}