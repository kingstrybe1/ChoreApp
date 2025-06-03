package keightrybe.com.choreapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import keightrybe.com.choreapp.data.ChoreDatabaseHandler
import keightrybe.com.choreapp.data.ChoreListAdapter
import keightrybe.com.choreapp.model.Chore



class MainActivity : ComponentActivity() {
    private lateinit var enterChore : TextView
    private lateinit var assignedTo : TextView
    private lateinit var assignedBy : TextView
    private lateinit var save : Button


    // 🔗 Database handler for chores
    var dbHandler: ChoreDatabaseHandler? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 🖼️ Set full-screen layout (optional)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        enterChore = findViewById(R.id.enterChoreId)
        assignedBy = findViewById(R.id.assignedById)
        assignedTo = findViewById(R.id.assignedToId)
        save = findViewById(R.id.saveId)

        checkDB()

        // 🧱 Initialize the database handler
        dbHandler = ChoreDatabaseHandler(this)
        progressDialog = ProgressDialog(this)

        save.setOnClickListener {

            progressDialog!!.setMessage("Saving...")
            progressDialog!!.show()

            if (!TextUtils.isEmpty(enterChore.text.toString())
                && !TextUtils.isEmpty(assignedTo.text.toString())
                && !TextUtils.isEmpty(assignedBy.text.toString())) {
                //save to database
                var chore = Chore()
                    chore.choreName = enterChore.text.toString()
                    chore.assignedTo = assignedTo.text.toString()
                    chore.assignedBy = assignedBy.text.toString()

                    saveToDB(chore)
                    progressDialog!!.cancel()

                    startActivity(Intent(this, ChoreListActivity::class.java))

                }else {
                Toast.makeText(this, "Please enter a chore", Toast.LENGTH_LONG).show()
            }

            }

        }

    fun saveToDB(chore: Chore) {
        dbHandler?.createChore(chore)
    }
    fun checkDB() {
        dbHandler?.getChoresCount()?.let {
            if (it > 0){
                startActivity(Intent(this, ChoreListActivity::class.java))
            }
        }
    }
}
