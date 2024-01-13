package es.kingcreek.ft_hangouts.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.views.ColorPickerView;


public class SettingsDialogFragment extends DialogFragment {

    private ColorPickerView colorPickerView;
    private Switch switchDarkMode;
    private Switch switchBonusMode;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_settings, null);

        colorPickerView = view.findViewById(R.id.colorPicker);
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        switchBonusMode = view.findViewById(R.id.switchBonusMode);

        colorPickerView.setOnColorSelectedListener(new ColorPickerView.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {

            }
        });

        // Configura los listeners para los Switch
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        switchBonusMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        builder.setView(view)
                .setTitle("Configuración")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
}
