package chat_quickshare.adapter;


    public class ChatModel {
        String buddyId;
        String youId;
       // String Unreaded;
       // String whoIs;
        String messageTxt;
   String sadeeename;
   String whors;
   String mesgtype;
   String multimideapath;


        public ChatModel( String receiver_id, String sender_id, String msg_txt,String wSR,String smtyp,String multimideapa) {
        buddyId=receiver_id;
        youId=sender_id;
        mesgtype=smtyp;
        multimideapath=multimideapa;
           // Unreaded=unreaded;
       // whoIs=whoIstem;
           // sadeeename=sname;
        messageTxt=msg_txt;
            whors=wSR;
    }

        public String getMultimideapath() {
            return multimideapath;
        }

        public void setMultimideapath(String multimideapath) {
            this.multimideapath = multimideapath;
        }

        public String getMesgtype() {
            return mesgtype;
        }

        public void setMesgtype(String mesgtype) {
            this.mesgtype = mesgtype;
        }

        public String getWhors() {
            return whors;
        }

        public void setWhors(String whors) {
            this.whors = whors;
        }
/* public String getUnreaded() {
            return Unreaded;
        }

        public void setUnreaded(String unreaded) {
            Unreaded = unreaded;
        }*/

        public String getBuddyId() {
            return buddyId;
        }

        public void setBuddyId(String buddyId) {
            this.buddyId = buddyId;
        }

        public String getYouId() {
            return youId;
        }

        public void setYouId(String youId) {
            this.youId = youId;
        }

      /* // public String getWhoIs() {
            return whoIs;
        }

        public void setWhoIs(String whoIs) {
            this.whoIs = whoIs;
        }
*/
        public String getMessageTxt() {
            return messageTxt;
        }

        public void setMessageTxt(String messageTxt) {
            this.messageTxt = messageTxt;
        }


        public String getsadeeename() {
            return sadeeename;
        }
    }

