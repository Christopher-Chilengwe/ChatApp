package chat_quickshare.dbhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sdfds";
    private static final String TABLE_CONTACTS = "contact_chat";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_MOBILE = "mobile";


    private static final String TABLE_GRP = "grp";
    private static final String GRP_ID = "id";
    private static final String GRP_NAME = "grp_name";
    private static final String USERS_ID = "users_id";
    private static final String CREATOR_ID = "creator_id";
    private static final String WHO_ADMIN = "admin_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_MOBILE + " TEXT "+")";
        db.execSQL(CREATE_CONTACTS_TABLE);


        String CREATE_CONTACTS_TABLE1 = "CREATE TABLE " + TABLE_GRP + "("
                + GRP_ID + " INTEGER PRIMARY KEY," + GRP_NAME + " TEXT,"
                + USERS_ID + " TEXT, "+CREATOR_ID+" TEXT,"+WHO_ADMIN+" TEXT "+")";
        db.execSQL(CREATE_CONTACTS_TABLE1);
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_CONTACTS,null);
        return res;
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRP);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO "+TABLE_CONTACTS+"(name,mobile) VALUES('"+contact.getName()
               +"','"+contact.getPhoneNumber()+"');");

    }


        return contactList;
    }



    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_GRP+"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }
    // Getting contacts Count
    public int getContactsCount(String uid) {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS+" where uid='"+uid+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }



    public String getQTY(String idStr,String sid,String uid,String bid)
    {
        String qty="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM cart WHERE id='"+idStr+"' and uid='"+uid+"' and storeid="+sid+" and brandid="+bid, null);
        if(c.moveToFirst())
        {
            qty=c.getString(5);
        }
        return  qty;
    }
    public String getViewQty(String sid,String bid)
    {
        String qty="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM cart WHERE storeid="+sid+" and brandid="+bid, null);
        if(c.moveToFirst())
        {
            qty=c.getString(5);
        }
        return  qty;
    }

    public String getViewAmount(String sid,String bid)
    {
        String amountTem="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM cart WHERE storeid="+sid+" and brandid="+bid, null);
        if(c.moveToFirst())
        {
            amountTem=c.getString(6);
        }
        return  amountTem;
    }




    public  void updateQTY(String qty,String updateId,String sid,String bid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE cart SET qty='"+qty+"'  WHERE  id='"+updateId+"' and storeid="+sid+" and brandid="+bid);
    }
    public void deleteCart(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart WHERE id='"+id+"'");
    }

   public void edtamount( String amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE cart SET amount='"+amount);
    }

    public String storeExist(String sid)
    {
        String  flgstatus="";
        String qty="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT storeid FROM cart WHERE storeid='"+sid+"'", null);
       int cout=c.getCount();
       if(cout==0)
       {
           Cursor c1=db.rawQuery("SELECT storeid FROM cart ", null);
           int cout1=c1.getCount();
           if(cout1!=0)
           {
               flgstatus="C";
           }
           else {
               flgstatus="N";
           }
       }
       else
       {
           flgstatus="N";
       }
        return  flgstatus;
    }

    public boolean itemsIncart(String sid)
    {
        boolean flgstatus=false;
        String qty="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT brandid FROM cart WHERE  storeid="+sid, null);
        int cout=c.getCount();
        if(cout<3)
        {
            flgstatus= true;
        }

        else {
            flgstatus=false;
        }
        return  flgstatus;
    }

    public String itemCheckEditORNot(String sid,String bId)
    {

        String flgstatus="";
        String qty="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM cart WHERE   storeid="+sid+" and brandid="+bId, null);
        int cout=c.getCount();
        if(cout>0)
        {
            flgstatus="ITEAMEXIST";
        }

        else {


          //  SQLiteDatabase db = this.getWritableDatabase();
            Cursor c1=db.rawQuery("SELECT * FROM cart WHERE  storeid="+sid, null);
            int cout1=c1.getCount();
            if(cout1<3)
            {
                flgstatus= "ADDNEWITEAM";
            }

            else {
                flgstatus="NOADDMORE";
            }


           // flgstatus="";
        }
        return  flgstatus;
    }

    public String getAmount(String id/*,String sid,String bid*/)
    {
        boolean flgstatus=false;
        String amount="";
        SQLiteDatabase db = this.getWritableDatabase();
      //  Cursor c=db.rawQuery("SELECT * FROM cart WHERE id='"+id+"' and uid='"+uID+"' and storeid="+sid+" and brandid="+bid, null);
        Cursor c=db.rawQuery("SELECT * FROM cart WHERE id="+id+"", null);
        if(c.moveToFirst())
        {
            amount=c.getString(6);
        }
        return  amount;
    }
    public String editAmountVal(String id, String rs /*, String sid, String bId*/)
    {
        boolean flgstatus=false;
        String amount="";
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("update cart set amount='"+rs+"' WHERE id='"+id+"' and uid='"+uID+"' and storeid="+sid+" and brandid="+bId);
     if(rs.equals("0"))
     {
         db.execSQL("DELETE FROM cart WHERE id="+id+"");
     }else {
         db.execSQL("update cart set amount="+rs+" WHERE id="+id+"");
     }


        //return  amount;
        return amount;
    }


    public List<Contact> addCartData()
    {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
       // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS+" where uid="+uID+" and storeid='"+sId+"'";
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS+"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

            /*    private static final String KEY_ID = "id";
                private static final String KEY_UID = "uid";
                private static final String KEY_STORE = "storeid";
                private static final String KEY_BRAND = "brandid";
                private static final String KEY_IMG = "img";
                private static final String KEY_QTY = "qty";
                private static final String KEY_AMT = "amount";*/


                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setUid(cursor.getString(1));
                contact.setStoreId(cursor.getString(2));
                contact.setBrandID(cursor.getString(3));
                contact.setImgUrl(cursor.getString(4));
                contact.setQty(cursor.getString(5));
                contact.setAmount(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void deleteUserCart()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart");
    }

    public void deleteUserCart(String sID,String bID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart WHERE  storeid="+sID+" and brandid="+bID);
    }

    public String itemsUpdate(String qty,String sid,String bId,String mnt)
    {
        String tem;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT brandid FROM cart WHERE   storeid="+sid+" and brandid="+bId, null);
        int cout=c.getCount();
        if(cout>0)
        {

            if(qty.length()!=0 && !qty.equals("0")) {
                mnt="0";
                db.execSQL("UPDATE cart SET qty='" + qty + "', amount='"+mnt+"' where  storeid=" + sid + " and brandid=" + bId);
            }
            if(!mnt.equals("0")&& mnt.length()!=0)
            {
                qty="0";
                db.execSQL("UPDATE cart SET  amount='" + mnt + "',qty='" + qty + "' where  storeid=" + sid + " and brandid=" + bId);
            }
                tem="update";
        }

        else {
            tem="add";
        }
      return  tem;
    }


    public int checkCartSubmit(String uId,String sId,String bId,String Qty,String amout)
    {

        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS+" where uid="+uId+" and storeid="+sId+" and brandid="+bId+" and qty="+Qty+" and amount="+amout;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    //my
    public String getViewAMT(String id,String uID, String sid, String bid)
        {
            String amt="";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c=db.rawQuery("SELECT * FROM cart WHERE id='"+id+"' and uid='"+uID+"' and storeid="+sid+" and brandid="+bid, null);
            if(c.moveToFirst())
            {
                amt=c.getString(5);
            }
            return  amt;
        }

   /* public boolean deletePairedBuddy(String uid,String sId,String bid)
    {

        boolean temFlag=false;
        String countQuery = "SELECT  brandid FROM " + TABLE_CONTACTS+" where uid="+uid+" and storeid="+sId+" and brandid="+bid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
       int rowC=cursor.getCount();
        if(cursor.moveToFirst())
        {
            temFlag=true;

        }
        else
        {
            temFlag=true;
        }

        return temFlag;
    }*/

   /* public void deletePairedBuddyWithStore(String uid,String sID,String bID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart WHERE uid='"+uid+"' and storeid="+sID+" and brandid="+bID);
    }*/

    public String getStorName()
    {
        String storeName="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM cart", null);
        if(c.moveToFirst())
        {
            storeName=c.getString(2);
        }
        return  storeName;
    }



public boolean getCartEmpty()
{
    boolean flag=false;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor c=db.rawQuery("SELECT * FROM cart", null);
    int cont=c.getCount();
    if(cont>0)
    {
        flag=true;
    }
    return  flag;
}


}