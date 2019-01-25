package masteroogway.smsreader

class ListItems {
    lateinit var address:String
    lateinit var msg:String
    lateinit var idSms:String
lateinit var date:String

    fun setNumber(address:String)
    {
        this.address=address
    }
    fun setMessage(msg:String)
    {
        this.msg=msg
    }

    fun setId(id:String)
    {
        this.idSms=id
    }
fun setDateSms(date:String)
{
    this.date=date
}

}
