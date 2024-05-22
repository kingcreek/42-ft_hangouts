package es.kingcreek.ft_hangouts.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.database.SMSDataSource;
import es.kingcreek.ft_hangouts.helper.Constants;
import es.kingcreek.ft_hangouts.helper.Helper;
import es.kingcreek.ft_hangouts.helper.PreferenceHelper;
import es.kingcreek.ft_hangouts.models.ContactModel;
import es.kingcreek.ft_hangouts.models.SMSModel;

public class SMSReceiver extends BroadcastReceiver {

    private final String TAG = "SMSReceiver";
    @Override
    public void onReceive(Context context, Intent intent)
    {

        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        StringBuilder strNumber = new StringBuilder();
        StringBuilder strMessage = new StringBuilder();

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

            // Check if bonus is active or not
            if (!PreferenceHelper.getInstance(context).isBonusActive())
            {
                // Prevent add number if not registered on own contacts
                if (ContactDataSource.getInstance(context).getContactByNumber(strNumber.toString()) == -1) {
                    Toast.makeText(context, context.getString(R.string.only_bonus), Toast.LENGTH_LONG).show();
                    return;
                }
            }

            // Add contact to DB
            int contactID = (int)ContactDataSource.getInstance(context).insertContact(new ContactModel(strNumber.toString(), strNumber.toString()));
            String time = Helper.getCurrentDateTimeString();


            // Add message to DB
            if (contactID == -1)
                SMSDataSource.getInstance(context).insertSMS(new SMSModel((int)ContactDataSource.getInstance(context).getContactByNumber(strNumber.toString()), strNumber.toString(), strMessage.toString(), time, 1));
            else
                SMSDataSource.getInstance(context).insertSMS(new SMSModel(contactID, strNumber.toString(), strMessage.toString(), time, 1));

            // Send broadcast to update MainActivity
            Intent mainBroadcast = new Intent();
            mainBroadcast.setAction(Constants.SMS_RECEIVED_MAIN);
            mainBroadcast.putExtra("contactID", contactID);
            context.sendBroadcast(mainBroadcast);

            // Send broadcast to update ContactDetails
            Intent contactBroadcast = new Intent();
            contactBroadcast.setAction(Constants.SMS_RECEIVED_DETAILS);
            contactBroadcast.putExtra("contactID", (int)ContactDataSource.getInstance(context).getContactByNumber(strNumber.toString()));
            contactBroadcast.putExtra("phoneNumber", strNumber.toString());
            contactBroadcast.putExtra("message", strMessage.toString());
            contactBroadcast.putExtra("time", time);
            contactBroadcast.putExtra("inOut", 1);
            context.sendBroadcast(contactBroadcast);
        }
    }
}