package es.kingcreek.ft_hangouts.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.database.ContactDBHelper;
import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.models.ContactModel;
import es.kingcreek.ft_hangouts.views.SwipeToDeleteCallback;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements SwipeToDeleteCallback.SwipeToDeleteListener {

    private List<ContactModel> contacts;
    private List<ContactModel> filteredContacts;
    private Context context;
    private final ItemTouchHelper itemTouchHelper;

    public ContactAdapter(Context context, List<ContactModel> contacts, List<ContactModel> filteredContacts) {
        this.context = context;
        this.contacts = contacts;
        this.filteredContacts = filteredContacts;
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context, this);
        itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactModel contact = filteredContacts.get(position);

        holder.textViewNumber.setText(contact.getNumber());
        holder.textViewFirstName.setText(contact.getFirstName());
        holder.textViewLastName.setText(contact.getLastName());

        holder.element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Name: " + contact.getFirstName(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredContacts.size();
    }

    @Override
    public void onSwipeToDelete(int position, boolean confirmed) {
        if (confirmed) {
            int originalPosition = contacts.indexOf(filteredContacts.get(position));
            if (originalPosition != -1) {
                //remove from DB
                ContactModel contactModel = contacts.get(originalPosition);
                ContactDataSource.getInstance(context).removeContactById(contactModel.getId());
                // Remove element from original list
                contacts.remove(originalPosition);
                // Remove element from filtered list
                filteredContacts.remove(position);
                // Notify changes
                notifyItemRemoved(position);
            }
        } else {
            notifyItemChanged(position);
        }
    }

    public void attachSwipeToDelete(RecyclerView recyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber, textViewFirstName, textViewLastName;
        LinearLayout element;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            textViewLastName = itemView.findViewById(R.id.textViewLastName);
            element = itemView.findViewById(R.id.element);

        }
    }

    // Search filter
    public void filter(String query) {
        filteredContacts.clear();
        // If search are empty
        if (TextUtils.isEmpty(query)) {
            filteredContacts.addAll(contacts);
        } else {
            // Filter
            for (ContactModel contact : contacts) {
                if (contact != null) {
                    if (contact.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                            contact.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                            contact.getNumber().contains(query)) {
                        filteredContacts.add(contact);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}