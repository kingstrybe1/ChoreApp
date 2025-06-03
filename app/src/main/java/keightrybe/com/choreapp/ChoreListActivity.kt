package keightrybe.com.choreapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import keightrybe.com.choreapp.data.ChoreDatabaseHandler
import keightrybe.com.choreapp.data.ChoreListAdapter
import keightrybe.com.choreapp.model.Chore

class ChoreListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChoreListAdapter
    private lateinit var choreList: ArrayList<Chore>
    private lateinit var dbHandler: ChoreDatabaseHandler
    private var dialogBuilder: AlertDialog.Builder? =null
    private var dialog: AlertDialog? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chore_list)



        // Initialize database and RecyclerView
        dbHandler = ChoreDatabaseHandler(this)
        recyclerView = findViewById(R.id.recyclerVw)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load data and setup adapter
        choreList = ArrayList(dbHandler.readChores(1))
        choreList.reverse()
        adapter = ChoreListAdapter(choreList, this)
        recyclerView.adapter = adapter

        // Log chores for debugging
        for (chore in choreList) {
            Log.d("ChoreDebug", "Assigned by: ${chore.assignedBy}, Assigned to: ${chore.assignedTo}, Chore: ${chore.choreName}")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_menu_button) {
            Log.d("Item Clicked", "Item Clicked")

            createPopupDialog()
        }
        return super.onOptionsItemSelected(item)
    }
    @SuppressLint("InflateParams", "NotifyDataSetChanged")
    fun createPopupDialog() {
        var view = layoutInflater.inflate(R.layout.popup, null)
        var choreName = view.findViewById<EditText>(R.id.popEnterChore)
        var assignedBy = view.findViewById<EditText>(R.id.popEnterAssignedBy)
        var assignedTo = view.findViewById<EditText>(R.id.popEnterAssignedTo)
        var saveButton = view.findViewById<Button>(R.id.popSaveChores)

        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder!!.create()
        dialog?.show()

        saveButton.setOnClickListener {
            val name = choreName.text.toString().trim()
            val by = assignedBy.text.toString().trim()
            val to = assignedTo.text.toString().trim()

            if (!TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(by)
                && !TextUtils.isEmpty(to)) {
                var chore = Chore()

                chore.choreName = name
                chore.assignedTo = to
                chore.assignedBy = by

                dbHandler.createChore(chore)

                dialog?.dismiss()

                startActivity(Intent(this, ChoreListActivity::class.java))
                finish()

            } else {
                // Optional: inform the user they must fill out all fields
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

private fun Chore.showHumanDate(lng: Long?) {}
