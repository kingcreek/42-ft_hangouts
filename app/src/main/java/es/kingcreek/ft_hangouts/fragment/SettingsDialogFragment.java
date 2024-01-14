package es.kingcreek.ft_hangouts.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.helper.PreferenceHelper;
import es.kingcreek.ft_hangouts.interfaces.OnDialogDismissListener;
import es.kingcreek.ft_hangouts.views.ColorPickerView;


public class SettingsDialogFragment extends DialogFragment {

    private ColorPickerView colorPickerView;
    private SwitchCompat switchDarkMode;
    private SwitchCompat switchBonusMode;
    private Button btnCancel, btnApply;
    private TextView time;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_settings, null);

        // Preferences
        PreferenceHelper preferences = PreferenceHelper.getInstance(getActivity());

        // Views
        colorPickerView = view.findViewById(R.id.colorPicker);
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        switchBonusMode = view.findViewById(R.id.switchBonusMode);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnApply = view.findViewById(R.id.btnApply);
        time = view.findViewById(R.id.time);

        switchDarkMode.setChecked(preferences.isDarkMode());
        switchBonusMode.setChecked(preferences.isBonusActive());
        time.setText(preferences.getLastTime());

        // Temp vars
        final int[] tcolor = {preferences.getToolbarColor()};
        final boolean[] tdarkMode = {preferences.isDarkMode()};
        final boolean[] tbonus = {preferences.isBonusActive()};

        // Set color picker saved color
        colorPickerView.setInitialColor(tcolor[0]);

        colorPickerView.setOnColorSelectedListener(new ColorPickerView.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                tcolor[0] = color;
            }
        });

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tdarkMode[0] = isChecked;
            }
        });

        switchBonusMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tbonus[0] = isChecked;
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.setBonusActive(tbonus[0]);
                preferences.setDarkMode(tdarkMode[0]);
                preferences.saveToolbarColor(tcolor[0]);
                notifyChanges();
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view).setTitle(getString(R.string.configuration));
        return builder.create();
    }

    private void notifyChanges()
    {
        if (getActivity() instanceof OnDialogDismissListener) {
            ((OnDialogDismissListener) getActivity()).onDialogDismissed();
        }
    }
}
