package es.kingcreek.ft_hangouts.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.helper.Constants;
import es.kingcreek.ft_hangouts.models.ContactModel;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        StringBuilder strNumber = new StringBuilder();
        StringBuilder strMessage = new StringBuilder();

        //DatabaseHelper myDb = DatabaseHelper.getInstance(context);

        if (myBundle != null)
        {
            Object [] pdus = (Object[]) myBundle.get("pdus");

            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++)
            {
                String format = myBundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);

                strNumber.append(messages[i].getOriginatingAddress());
                strMessage.append(messages[i].getMessageBody());
            }

            // Add contact to DB
            int contactID = (int)ContactDataSource.getInstance(context).insertContact(new ContactModel(strNumber.toString(), strNumber.toString()));

            // Add message to DB

            // Send broadcast to update MainActivity
            Intent broadcastReceiver = new Intent();
            broadcastReceiver.setAction(Constants.SMS_RECEIVED);
            broadcastReceiver.putExtra("contactID", contactID);
            context.sendBroadcast(broadcastReceiver);
        }
    }
}