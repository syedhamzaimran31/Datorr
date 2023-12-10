package com.example.taskclass.Adapters

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.taskclass.Actvities.AdvanceActivity
import com.example.taskclass.Actvities.FemaleActivity
import com.example.taskclass.Actvities.MaleActivity
import com.example.taskclass.R
import com.example.taskclass.models.ItemsViewModel
import com.example.taskclass.room.AppDatabase
import com.example.taskclass.room.FemaleActivityData
import com.example.taskclass.room.MaleActivityData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CustomAdapter(private val mList: List<ItemsViewModel>, private val database: AppDatabase) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.genderlayout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        holder.itemView.setOnClickListener {
            try {
                when (position) {
                    0 -> {
                        val intent = Intent(holder.itemView.context, MaleActivity::class.java);
                        holder.itemView.context.startActivity(intent);
                    }

                    1 -> {
                        val intent = Intent(holder.itemView.context, FemaleActivity::class.java);
                        holder.itemView.context.startActivity(intent);
                    }

                    2 -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val userListMale: List<MaleActivityData> = database.userDao().getAllMales()
                                val userListFemale: List<FemaleActivityData> = database.userDao().getAllFemales()

                                withContext(Dispatchers.Main) {
                                    Log.d("CustomAdapter", "User List Male Size: ${userListMale.size}")
                                    Log.d("CustomAdapter", "User List Female Size: ${userListFemale.size}")

                                    if ( userListMale.isNotEmpty() || userListFemale.isNotEmpty()) {

                                        val intent = Intent(holder.itemView.context, AdvanceActivity::class.java)
                                        holder.itemView.context.startActivity(intent)

                                    } else {
                                        Toast.makeText(
                                            holder.itemView.context,
                                            "Fill at least one form",
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Log.e("CustomAdapter", "An error occurred: ${e.message}", e)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "An error occurred: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    holder.itemView.context,
                    "An error occurred: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        holder.imageView.setImageResource(ItemsViewModel.image)

        holder.textView.text = ItemsViewModel.text

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}
