package es.kingcreek.ft_hangouts.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SettingsDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ajustes");

        builder.setItems(new CharSequence[]{"Cambiar color del toolbar", "Modo bonus"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleSettingsOption(which);
            }
        });

        return builder.create();
    }

    private void handleSettingsOption(int option) {
        switch (option) {
            case 0:
                // Change toolbar color
                break;
            case 1:
                // Active Bonus
                break;
        }
    }
}