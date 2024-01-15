package es.kingcreek.ft_hangouts.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import es.kingcreek.ft_hangouts.R;

public class NewMessageDialog extends Dialog {

    private TextView textViewDialogTitle;
    private EditText editTextMessage;
    private Button buttonPositive;
    private Button buttonNegative;

    private OnClickListener positiveClickListener;
    private OnClickListener negativeClickListener;

    public NewMessageDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.custom_dialog_message);
        initView();
    }

    private void initView() {
        textViewDialogTitle = findViewById(R.id.textViewDialogTitle);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonPositive = findViewById(R.id.buttonPositive);
        buttonNegative = findViewById(R.id.buttonNegative);

        buttonPositive.setOnClickListener(view -> {
            if (positiveClickListener != null) {
                positiveClickListener.onClick(this, DialogInterface.BUTTON_POSITIVE);
            }
        });

        buttonNegative.setOnClickListener(view -> {
            if (negativeClickListener != null) {
                negativeClickListener.onClick(this, DialogInterface.BUTTON_NEGATIVE);
            }
            dismiss();
        });
    }

    public void setDialogTitle(String title) {
        textViewDialogTitle.setText(title);
    }

    public void setPositiveClickListener(OnClickListener listener) {
        this.positiveClickListener = listener;
    }

    public void setNegativeClickListener(OnClickListener listener) {
        this.negativeClickListener = listener;
    }

    public String getMessage() {
        return editTextMessage.getText().toString();
    }
}