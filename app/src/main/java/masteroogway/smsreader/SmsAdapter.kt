package masteroogway.smsreader

import android.content.ContentResolver
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.RelativeLayout





class ViewHolderSms(view:View):RecyclerView.ViewHolder(view)
{
    val txtHeaderSms=view.findViewById<TextView>(R.id.txtHeaderSms)
    val txtBodySms=view.findViewById<TextView>(R.id.txtBodySms)
    var viewBackground:RelativeLayout = view.findViewById(R.id.view_background)
    var viewForeground:ConstraintLayout = view.findViewById(R.id.view_foreground)

}

class SmsAdapter(val listSms: MutableList<ListItems>,val  contextlandingPage: LandingPage) : RecyclerView.Adapter<ViewHolderSms>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSms {
return  ViewHolderSms(LayoutInflater.from(contextlandingPage).inflate(R.layout.rowstructure_smslist,parent,false))
    }

    override fun getItemCount(): Int {
        return  listSms.size
    }

    override fun onBindViewHolder(holder: ViewHolderSms, position: Int) {
              holder?.txtHeaderSms?.text= listSms.get(position).address
        holder?.txtBodySms?.text= listSms.get(position).msg

    }

    fun removeItem(position: Int, contentResolver: ContentResolver):Boolean {
       /* val uri = Uri.parse("content://sms/inbox")    ///" + smsMutableList[viewHolder.adapterPosition].idSms)
        val cursor =contentResolver.delete(uri, listSms[position].idSms,null)*/
        //val uri = Uri.parse("content://sms/inbox")
        val cr = contentResolver
        val uriSms = Uri.parse("content://sms/inbox")
        val cursor = contentResolver.query(uriSms,
                arrayOf("_id", "address", "date", "body"), "_id='"+listSms[position].idSms +"'", null, null)

        while (cursor!!.moveToNext()) {
            val v=cursor.getString(0)
//            val count=     contentResolver.delete(Uri.parse("content://sms"), "_id=?",arrayOf<String>((listSms[position].idSms )))
            val count=   contentResolver.delete(
                    Uri.parse("content://sms/" + listSms[position].idSms), "date=?",
                    arrayOf(cursor.getString(2)))
            //val thread = Uri.parse("content://sms")
            //val deleted = contentResolver.delete(thread, "thread_id=? and _id=?", arrayOf(String.valueOf(thread_id), String.valueOf(id)))
           // context.getContentResolver().delete(
             //       Uri.parse("content://sms/" + id), null, null);
        }
        //cr.delete(Uri.parse("content://sms/"+listSms[position].idSms), null, null)
        //val c = cr.query(uri, null, "_id = ?", arrayOf("" + listSms[position].idSms), null)
        /*if (c != null && c!!.moveToFirst()) {
            do {
                val pid = c!!.getString(c!!.getColumnIndex("_id"))
                val pnumber = c!!.getString(c!!.getColumnIndex("address"))

                Log.d("MyAPP", "id: $pid")
                if (id == pid && number.equals(pnumber)) {
                    Log.d("MyAPP", "Deleting SMS with id: $id")
                    try {
                        cr.delete(Uri.parse("content://sms/$id"), null, null)
                        Log.d("MyAPP", "Delete success.........")
                    } catch (ex: Exception) {
                        Log.d("MyAPP", "Error deleting msg")
                    }

                }
            } while (c!!.moveToNext())
        }*/
        listSms.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)

    return true
    }

}
