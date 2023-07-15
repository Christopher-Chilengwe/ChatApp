package chat_quickshare.dbhelper;

public class GroupContact extends Contact {
    int _id;
    String Gname;

    String GuserId;
    String GcreatorId;
    String GadminId;



    public GroupContact(){   }
    public GroupContact(String name, String usersId,String creatorId,String whoAdminId){

        this.Gname = name;

        GuserId=usersId;
        GcreatorId=creatorId;
        GadminId=whoAdminId;
    }

    }

    public void setGadminId(String gadminId) {
        GadminId = gadminId;
    }
}