package keightrybe.com.choreapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiContext
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView
import keightrybe.com.choreapp.ChoreListActivity
import keightrybe.com.choreapp.R
import keightrybe.com.choreapp.R.layout.popup
import keightrybe.com.choreapp.model.Chore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("KotlinConstantConditions")
class ChoreListAdapter(private val list: ArrayList<Chore>,
                       private  val context: Context): RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View, context: Context, list: ArrayList<Chore>) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mContext = context
        var mList = list

        var choreName: TextView = itemView.findViewById(R.id.listChoreName)
        var assignedBy: TextView = itemView.findViewById(R.id.listAssignedBy)
        var assignedDate: TextView = itemView.findViewById(R.id.listDate)
        var assignedTo: TextView = itemView.findViewById(R.id.listAssignedTo)
        var deleteButton: Button = itemView.findViewById(R.id.listDeleteButton)
        var editButton: Button = itemView.findViewById(R.id.listEditButton)

        @SuppressLint("SetTextI18n")
        fun bindViews(chore: Chore) {
            choreName.text = "Chore: ${chore.choreName}"
            assignedBy.text = "Assigned By: ${chore.assignedBy}"
            assignedTo.text = "Assigned To: ${chore.assignedTo}"
            assignedDate.text = chore.timeAssigned?.let { showHumanDate(it) } ?: "No Date"

            deleteButton.setOnClickListener(this)
            editButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            var mPosition: Int = adapterPosition
            var chore = mList[mPosition]


            when (v!!.id) {
                deleteButton.id -> {
                    deleteChore(chore.id!!)
                    mList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)

                }

                editButton.id -> {
                    editChore(chore)
                    notifyItemChanged(adapterPosition, chore)
                }
            }
        }

        fun deleteChore(id: Int) {
            var db: ChoreDatabaseHandler = ChoreDatabaseHandler(mContext)
            db.deleteChore(id)
        }

        fun editChore(chore: Chore) {

            var dialogBuilder: AlertDialog.Builder?
            var dbHandler: ChoreDatabaseHandler = ChoreDatabaseHandler(context)


            var view = LayoutInflater.from(context).inflate(popup, null)
            var choreName = view?.findViewById<EditText>(R.id.popEnterChore)
            var assignedBy = view?.findViewById<EditText>(R.id.popEnterAssignedBy)
            var assignedTo = view?.findViewById<EditText>(R.id.popEnterAssignedTo)
            var saveButton = view?.findViewById<Button>(R.id.popSaveChores)

            dialogBuilder = AlertDialog.Builder(context).setView(view)
            var dialog = dialogBuilder!!.create()
            dialog.show()

            saveButton?.setOnClickListener {
                val name = choreName?.text.toString().trim()
                val by = assignedBy?.text.toString().trim()
                val to = assignedTo?.text.toString().trim()

                if (!TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(by)
                    && !TextUtils.isEmpty(to)
                ) {
//                    var chore = Chore()

                    chore.choreName = name
                    chore.assignedTo = to
                    chore.assignedBy = by

                    dbHandler.updateChore(chore)
                    notifyItemChanged(adapterPosition, chore)

                    dialog.dismiss()

//                    startActivity(Intent(this, ChoreListActivity::class.java))
//                    finish()

                } else {
                    // Optional: inform the user they must fill out all fields
//                    Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            position: Int
        ): ChoreListAdapter.ViewHolder {
            //create view from our XML file
            val view = LayoutInflater.from(context)
                .inflate(R.layout.list_row, parent, false)

            return ViewHolder(view, context, list)
        }

        override fun onBindViewHolder(holder: ChoreListAdapter.ViewHolder, position: Int) {
            holder.bindViews(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }

    private fun ChoreListAdapter.ViewHolder.showHumanDate(time: Long): CharSequence? {
        val date = Date(time)
        val format = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
        return "Created: ${format.format(date)}"
    }

