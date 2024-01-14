package es.kingcreek.ft_hangouts.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.views.NewMessageDialog;

public class CommunicationUtils {

    public static void makePhoneCall(Context context, String phoneNumber) {
        if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        } else {
            showPermissionExplanationDialog(context, Manifest.permission.CALL_PHONE);
        }
    }

    public static void sendSMS(final Context context, final String phoneNumber) {
        NewMessageDialog customDialog = new NewMessageDialog(context);
        customDialog.setDialogTitle(context.getString(R.string.sms_dialog));

        customDialog.setPositiveClickListener((dialogInterface, i) -> {
            String message = customDialog.getMessage();
        });

        customDialog.show();
    }

    private static void sendMessage(Context context, String phoneNumber, String message) {
        if (context.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } else {
            // Pedir permisos para enviar SMS
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
