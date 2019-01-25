package masteroogway.smsreader

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.DefaultItemAnimator


class LandingPage : AppCompatActivity(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is ViewHolderSms) {
            // get the removed item name to display it in snack bar
            val msg = smsMutableList.get(viewHolder.getAdapterPosition()).msg

            // backup of removed item for undo purpose
            val deletedItem = smsMutableList.get(viewHolder.getAdapterPosition())
            val deletedIndex = viewHolder.getAdapterPosition()

            // remove the item from recycler view
            smsAdapter.removeItem(viewHolder.getAdapterPosition(),contentResolver)


            // showing snack bar with Undo option
           /* val snackbar = Snackbar
                    .make(coordinatorLayout, msg + " removed from cart!", Snackbar.LENGTH_LONG)
            snackbar.setAction("UNDO", object : View.OnClickListener() {
                fun onClick(view: View) {

                    // undo is selected, restore the deleted item
                   // adapter.restoreItem(deletedItem, deletedIndex)
                }
            })
            snackbar.setActionTextColor(Color.YELLOW)
            snackbar.show()*/
        }
    }

    lateinit var smsAdapter: SmsAdapter
    lateinit var smsMutableList: MutableList<ListItems>
    lateinit var rvList: RecyclerView
    private val TAG = "SMSPERMISSION"
    private  val RECORD_REQUEST_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        setupPermissions()
    }
  /*  private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }*/
  private fun setupPermissions() {
      val permission = ContextCompat.checkSelfPermission(this,
              Manifest.permission.READ_SMS)

      if (permission != PackageManager.PERMISSION_GRANTED) {
          Log.i(TAG, "Permission to record denied")
          if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                          Manifest.permission.READ_SMS)) {
              val builder = AlertDialog.Builder(this)
              builder.setMessage("Permission  required  for this app to read sms")
                      .setTitle("Permission required")

              builder.setPositiveButton("OK"
              ) { dialog, id ->
                  Log.i(TAG, "Clicked")
                  makeRequest()
              }

              val dialog = builder.create()
              dialog.show()
          } else {
              makeRequest()
          }
      }
      else {
          setSmsRecyclerview()
          //smsMutableList=getSms()
      }
  }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_SMS),
                RECORD_REQUEST_CODE)
    }

   override fun onRequestPermissionsResult(requestCode: Int,
                                           permissions: Array<String>, grantResults: IntArray) {
       when (requestCode) {
           RECORD_REQUEST_CODE -> {
               // If request is cancelled, the result arrays are empty.
               if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                   // permission was granted, yay! Do the
                   // contacts-related task you need to do.


                   Log.i(TAG, "Permission has been denied by user")

//               smsMutableList=getSms()

                   setSmsRecyclerview()

               } else {
                   // permission denied, boo! Disable the
                   // functionality that depends on this permission.
                   Log.i(TAG, "Permission has not been granted by user")
                   /*Snackbar.make(
                           root_layout, // Parent view
                           "Hello Snackbar!", // Message to show
                           Snackbar.LENGTH_SHORT // How long to display the message.
                   ).show()*/
               Toast.makeText(this,"Permission has not been granted by user",Toast.LENGTH_LONG).show()
               }
               return
           }

           // Add other 'when' lines to check for other
           // permissions this app might request.
           else -> {
               // Ignore all other requests.
           }
       }
   }

    private fun setSmsRecyclerview() {
        rvList=findViewById(R.id.recycleview)

        val list = ArrayList<ListItems>()
        var listItem: ListItems
        //   val num = number.getText().toString()
        val uriSms = Uri.parse("content://sms/inbox")
        val cursor = contentResolver.query(uriSms,
                arrayOf("_id", "address", "date", "body"), null, null, null)

        while (cursor!!.moveToNext()) {
            val address = cursor.getString(1)
            // if (address == num) {
            val msg = cursor.getString(3)
            val date=cursor.getString(2)
            val smsId=cursor.getString(0)
            listItem = ListItems()
            listItem.setNumber(address)
            listItem.setMessage(msg)
            listItem.setDateSms(date)
            listItem.setId(smsId)
            list.add(listItem)
            // }

        }
        smsMutableList=list;    
       // return  list
/*
         val listAdapter = ListAdapter(list,
                 baseContext)
         rvList.setLayoutManager(LinearLayoutManager(
                 baseContext))
         rvList.setAdapter(listAdapter)
*/

        rvList.layoutManager = LinearLayoutManager(baseContext)
        rvList.setItemAnimator(DefaultItemAnimator())
        rvList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        smsAdapter=SmsAdapter(list,this)
      //  rvList.adapter=SmsAdapter(list,this)
        rvList.adapter=smsAdapter
        //rvList.addItemDecoration(DividerItemDecoration(this,
          //      DividerItemDecoration.VERTICAL))

        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        rvList.addItemDecoration(itemDecorator)

       /* val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                // view the background view
            }
        }*/
        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        //ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvList)


// attaching the touch helper to recycler view
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvList)
    }

}
