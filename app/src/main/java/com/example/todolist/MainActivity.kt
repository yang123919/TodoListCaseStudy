package com.example.todolist

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.ViewPagerAdapters.NewTodoViewPagerAdapter
import com.example.todolist.database.TodoDatabase
import com.example.todolist.database.TodoRepository
import com.example.todolist.database.toTodoEntity
import com.example.todolist.fragments.AddWordFragment
import com.example.todolist.fragments.WordDetailsFragment
import com.example.todolist.model.ToDoStatus
import com.example.todolist.model.TodoDetails
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var repo: TodoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = TodoDatabase.getDatabase(this)
        repo = TodoRepository(db.todoDao())

        setupViewPager()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val container = findViewById<FrameLayout>(R.id.detailsContainer)
                if (container.visibility == View.VISIBLE) {
                    container.visibility = View.GONE
                    supportFragmentManager.popBackStack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    // Add this property
    private var currentTab = 0

    // In setupViewPager(), save the current page before resetting
    private fun setupViewPager(returnToTab: Int = 0) {
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager.adapter = NewTodoViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "New Word"
                1 -> "Completed Word"
                else -> ""
            }
        }.attach()
        viewPager.currentItem = returnToTab  // ← return to correct tab
    }

    fun openDetails(todo: TodoDetails) {
        // ✅ Save which tab we're on before opening details
        currentTab = findViewById<ViewPager2>(R.id.viewPager).currentItem

        val container = findViewById<FrameLayout>(R.id.detailsContainer)
        container.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.detailsContainer, WordDetailsFragment.newInstance(todo))
            .addToBackStack(null)
            .commit()
    }


    fun openEdit(todo: TodoDetails) {
        val container = findViewById<FrameLayout>(R.id.detailsContainer)
        container.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.detailsContainer, AddWordFragment.newInstance(todo))
            .addToBackStack(null)
            .commit()
    }

    fun closeAddWord() {
        val container = findViewById<FrameLayout>(R.id.detailsContainer)
        container.visibility = View.GONE
        supportFragmentManager.popBackStack()
        setupViewPager(currentTab)  // ✅ Return to same tab
    }

    fun closeDetails() {
        val container = findViewById<FrameLayout>(R.id.detailsContainer)
        container.visibility = View.GONE
        supportFragmentManager.popBackStack()
    }

    fun moveToCompleted(todo: TodoDetails) {
        Thread {
            repo.updateStatus(todo.id.toInt(), ToDoStatus.DONE)
            runOnUiThread {
                closeDetails()
                setupViewPager(currentTab)  // ✅ Return to same tab
            }
        }.start()
    }


    fun deleteTodo(todo: TodoDetails) {
        Thread {
            repo.deleteById(todo.id.toInt())
            runOnUiThread {
                closeDetails()
                setupViewPager(currentTab)  // ✅ Return to same tab
            }
        }.start()
    }
}