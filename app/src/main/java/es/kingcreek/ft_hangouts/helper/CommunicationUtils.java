package es.kingcreek.ft_hangouts.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.database.SMSDataSource;
import es.kingcreek.ft_hangouts.models.ContactModel;
import es.kingcreek.ft_hangouts.models.SMSModel;
import es.kingcreek.ft_hangouts.views.NewMessageDialog;

public class CommunicationUtils {

    public static void makePhoneCall(final Context context, final String phoneNumber) {
        if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        } else {
            showPermissionExplanationDialog(context, Manifest.permission.CALL_PHONE);
        }
    }

    public static void sendSMS(final Context context, final ContactModel contact) {
        if (context.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            NewMessageDialog customDialog = new NewMessageDialog(context);
            customDialog.setDialogTitle(context.getString(R.string.sms_dialog));

            customDialog.setPositiveClickListener((dialogInterface, i) -> {
                String time = Helper.getCurrentDateTimeString();
                String message = customDialog.getMessage();
                if (!message.isEmpty()) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(contact.getNumber(), null, message, null, null);
                    SMSDataSource.getInstance(context).insertSMS(new SMSModel(contact.getId(), contact.getNumber(), message, time, 0));

                    //send broadcast to update ContactDetails
                    Intent contactBroadcast = new Intent();
                    contactBroadcast.setAction(Constants.SMS_RECEIVED_DETAILS);
                    contactBroadcast.putExtra("contactID", contact.getId());
                    contactBroadcast.putExtra("phoneNumber", contact.getNumber());
                    contactBroadcast.putExtra("message", message);
                    contactBroadcast.putExtra("time", time);
                    contactBroadcast.putExtra("inOut", 0);
                    context.sendBroadcast(contactBroadcast);
                    customDialog.dismiss();
                } else {
                    Toast.makeText(context, context.getText(R.string.empty_sms), Toast.LENGTH_LONG).show();
                }
            });
            customDialog.show();
        } else {
            showPermissionExplanationDialog(context, Manifest.permission.SEND_SMS);
        }
    }


    private static void showPermissionExplanationDialog(final Context context, final String permission) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.permisson_req));
        builder.setMessage(context.getString(R.string.permisson_explain) + " " + permission);

        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, Constants.PERMISSION_REQUEST_CODE);
            }
        });

        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
