package chat_quickshare.dbhelper;

public class Contact {
    int _id;
    String _name;
    String _phone_number;

    String Uid;
    String StoreId;
    String BrandID;
    String ImgUrl;
    String Qty;
    String Amount;
    String bName;
    String isImg;



    public Contact(){   }
    public Contact(int id, String name, String _phone_number){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
    }


    

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}