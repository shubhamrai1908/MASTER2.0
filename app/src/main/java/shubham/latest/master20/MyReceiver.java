package shubham.latest.master20;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVE_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = MyReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    MasterDatabase masterDatabase;
    SubscriptionInfo sim;
    SubscriptionInfo sim1;

    @TargetApi(Build.VERSION_CODES.Q)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), SMS_RECEIVE_ACTION)) {
            Log.d("sendREQtoSER","process started");
            Bundle data = intent.getExtras();

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(context, "user not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            StringBuilder strMessage = new StringBuilder();
            String format = bundle.getString("format");
            Object[] pdus = (Object[]) bundle.get(pdu_type);
            msgs = new SmsMessage[pdus.length];
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(context);
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
            sim = (SubscriptionInfo) localList.get(0);
            sim1 = (SubscriptionInfo) localList.get(0);
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            int pos = preferences.getInt("SelectedSim", 0);
            int pos1 = preferences.getInt("SelectedSim1", 0);
            if (pos == 0) {
                sim = (SubscriptionInfo) localList.get(0);
                // Toast.makeText(context,"sim1",Toast.LENGTH_SHORT).show();
            }
            if (pos == 1) {
                sim = (SubscriptionInfo) localList.get(1);
                //Toast.makeText(context,"sim2",Toast.LENGTH_SHORT).show();

            }
            if (pos1 == 0) {
                sim1 = (SubscriptionInfo) localList.get(0);
                // Toast.makeText(context,"sim1",Toast.LENGTH_SHORT).show();
            }
            if (pos1 == 1) {
                sim1 = (SubscriptionInfo) localList.get(1);
                //Toast.makeText(context,"sim2",Toast.LENGTH_SHORT).show();

            }

            masterDatabase = new MasterDatabase(context);
            StringBuilder sender = new StringBuilder();
            long time = 0;

            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                sender.append(msgs[i].getOriginatingAddress());
                strMessage.append(msgs[i].getMessageBody());
                time = msgs[i].getTimestampMillis();
                Log.d("BROADCAST", "onReceive: " + "SENDER:" + sender + "\n" + "MESSAGE:" + strMessage + "\n" + "TIME:" + time);
                //Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                // smsSendMessage(String.valueOf(strMessage));
            }
            DateFormat f = new SimpleDateFormat();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            final String myTime = f.format(calendar.getTime());
            try {
                  mainMethod(String.valueOf(sender), String.valueOf(strMessage), context, myTime);
            }
            catch (Exception e)
            {
                Log.d("sendREQtoSER","err--"+e.toString());
            }
        }
    }
    private void mainMethod(String sender, String message, Context context, String time) {
        //  Toast.makeText(context,"MAIN method called",Toast.LENGTH_SHORT).show();
        switch (verifySender(sender, message, context)) {
            case 1:
                String reqno = message.substring(2, 12);
                String opname = message.substring(13).trim();
                //Toast.makeText(context,opname,Toast.LENGTH_SHORT).show();
                insertIntoDB("gnd_to_master_table", sender, reqno, opname, "-----", message, time);
                sendReqToServer(sender, reqno, opname);

                break;
            case 2:
                Log.d("sendREQtoSER","Moved back to case 2");
                identifyAndReply(sender, message, time, context);

                break;
            default:
                break;

        }

    }
    private void sendReqToServer(String gnd, String reqno, String opname) {
        Log.d("sendREQtoSER","sending start");
        Date mytime = Calendar.getInstance().getTime();
        String server = "";
        String msg = "";
        Cursor cursor = masterDatabase.getAll("serverlist_table");
        HashMap<String, String> serverlist = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                serverlist.put(cursor.getString(1),cursor.getString(2));
                Log.d("sendREQtoSER",cursor.getString(1)+"-"+serverlist.get(cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        switch (opname.toLowerCase()) {

            case "jio":
                server = serverlist.get("jio");
                msg = "91" + reqno.trim();
                Log.d("sendREQtoSER","server-"+server+" msg-"+msg);
                break;
            case "airtel":
                server = serverlist.get("airtel");
                msg = "91" + reqno.trim();
                break;
            case "idea":
                server = serverlist.get("vi");
                msg = "CEL 91" + reqno.trim();
                break;
            case "bsnl":
                server = serverlist.get("bsnl");
                msg = "getloc 91" + reqno.trim();
                break;
            default:
                return;
        }
        try {
            SmsManager.getSmsManagerForSubscriptionId(sim.getSubscriptionId()).sendTextMessage(server, null, msg, null, null);
            insertIntoDB("master_to_server_table", server, reqno, opname.trim(), gnd, msg, mytime.toString().trim());
            insertIntoDB("request_table", gnd, reqno, opname.trim(), "", "", mytime.toString().trim());
        }catch (Exception e)
        {
            Log.d("sendREQtoSER","err-"+e.toString());
        }
    }
    private void insertIntoDB(String TableName, String sender, String reqno, String opname,String gnd, String message, String time) {
        switch(TableName)
        {
            case "gnd_to_master_table":
                masterDatabase.groundTOmaster(sender,reqno,opname,message,time);
                break;
            case "master_to_server_table":
                masterDatabase.masterTOserver(sender,reqno,opname,gnd,message,time);
                break;
            case "server_to_master_table":
                masterDatabase.serverTOmaster(sender,reqno,opname,message,time);
                break;
            case "master_to_gnd_table":
                masterDatabase.masterTOground(sender,reqno,opname,message,time);
                break;
            case "request_table":
                masterDatabase.requestTable(sender,reqno,opname,time);
                break;


        }

    }
    private void identifyAndReply(String sender,String message, String time,Context context)
    {
        Log.d("sendREQtoSER","identifying penidng req");
        Cursor cursor = masterDatabase.getAll("request_table");
        if(cursor.moveToFirst()){
            do{
                String req=cursor.getString(2);
                if(message.contains(req))
                {
                    Toast.makeText(context,"Request verified",Toast.LENGTH_SHORT).show();
                    String gnd=cursor.getString(1);
                    String opname=cursor.getString(3);
                    insertIntoDB("server_to_master_table", sender, req, "--", opname.trim(), message, time);
                    if(message.contains("Received Request for")&&opname.equalsIgnoreCase("jio"))
                    {
                        Log.d("sendREQtoSER","only ack msg");
                        return;
                    }

                    //SmsManager.getSmsManagerForSubscriptionId(sim.getSubscriptionId()).sendTextMessage(server, null,msg  , null, null);

                    try {
                        Log.d("sendREQtoSER","ground-"+gnd+"\nmsg-"+message);
                        SmsManager smsManager = SmsManager.getDefault();
                        ArrayList<String> parts = smsManager.divideMessage(message);

                        SmsManager.getSmsManagerForSubscriptionId(sim1.getSubscriptionId()).sendMultipartTextMessage(gnd, null, parts, null, null);
                    }
                    catch(Exception e)
                    {
                        Log.d("sendREQtoSER",e.toString());
                        Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                        return;
                    }


                    insertIntoDB("master_to_gnd_table", gnd, req, opname.trim(), "--", message, time);
                    masterDatabase.deleteData(cursor.getString(0), "request_table");
                    return;
                }
            }while (cursor.moveToNext());
        }
    }


    private int verifySender(String sender, String message,Context context) {
        //for ground sender

        Log.d("sendREQtoSER","verify sender proccess started");
        Log.d("sendREQtoSER","trying to verify ground");
        Cursor cursor = masterDatabase.getAll("groundlist_table");
        List<String> groundlist=new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                groundlist.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        for(int i=0;i<groundlist.size();i++) {
            if (sender.contains(groundlist.get(i))) {
                // Toast.makeText(context,"Ground Verified",Toast.LENGTH_SHORT).show();
                if (message.length() >= 16 && message.length() <= 20){
                    Toast.makeText(context,"Ground and msg Verified",Toast.LENGTH_SHORT).show();
                    return 1;
                }

            }

        }

        //ground ends
        //for server
        Log.d("sendREQtoSER","trying to verify server");
        if(sender.contains("9418099998")||sender.contains("7000253007")||sender.contains("29532562")||sender.contains("84235562")||sender.contains("2954724")||sender.contains("VI-CELLOC")||sender.contains("AR-LEALOC")||sender.contains("7021265165")||sender.contains("54051")||sender.contains("8800112112")||message.contains("MSISDN")||message.contains("Cell ID")||message.contains("IMEI")||message.contains("IMSI")||message.contains("MOB")||message.contains("CGI")||message.contains("Request ID"))
        {
            Log.d("sendREQtoSER","server-"+sender+" msg-"+message);
            Toast.makeText(context,"Server Verified",Toast.LENGTH_SHORT).show();
            return 2;

        }
        Log.d("sendREQtoSER","no server no ground");

        //Toast.makeText(context,"nothing Verified"+"sender: "+sender,Toast.LENGTH_SHORT).show();
        return 0;

    }

}