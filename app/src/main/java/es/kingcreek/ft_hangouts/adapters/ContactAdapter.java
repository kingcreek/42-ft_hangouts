package es.kingcreek.ft_hangouts.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.database.ContactDBHelper;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Cursor cursor;
    private Context context;

    public ContactAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_NUMBER));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_LAST_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_EMAIL));

            holder.textViewNumber.setText(number);
            holder.textViewFirstName.setText(firstName);
            holder.textViewLastName.setText(lastName);
            holder.textViewEmail.setText(email);
        }
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber, textViewFirstName, textViewLastName, textViewEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            textViewLastName = itemView.findViewById(R.id.textViewLastName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
        }
    }
}