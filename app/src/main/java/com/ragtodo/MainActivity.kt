package com.ragtodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfItems = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove the item from the list
                listOfItems.removeAt(position)

                // Notify the adapter that our data has changed
                adapter.notifyDataSetChanged()

                // Save items to file
                saveItems()
            }

        }

        // Load saved items
        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfItems, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addItemField)

        // Get a reference to the button
        // and then set an onclick listener
        findViewById<Button>(R.id.button).setOnClickListener {
            val userAddedItem = inputTextField.text.toString()

            // Add the String to our list of tasks: listOfTasks
            listOfItems.add(userAddedItem)
            // Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfItems.size - 1)

            // Reset text field
            inputTextField.setText("")
            // Save items to file
            saveItems()
        }
    }

    // Save the data user has inputted
    // Save data by writing and reading from a file

    // Get the file we need
    fun getDataFile(): File {
        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "ragtodo.txt")
    }

    // Load the tasks by reading every line in the data file
    fun loadItems() {
        try {
            listOfItems = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    //Save tasks by writing items into the data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfItems)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}