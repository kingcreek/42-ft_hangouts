package es.kingcreek.ft_hangouts.adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.database.ContactDBHelper;
import es.kingcreek.ft_hangouts.models.ContactModel;
import es.kingcreek.ft_hangouts.views.SwipeToDeleteCallback;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements SwipeToDeleteCallback.SwipeToDeleteListener {

    private List<ContactModel> contacts;
    private List<ContactModel> filteredContacts;
    private Context context;
    private final ItemTouchHelper itemTouchHelper;

    public ContactAdapter(Context context, List<ContactModel> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.filteredContacts = new ArrayList<>(contacts);
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
    }

    @Override
    public int getItemCount() {
        return filteredContacts.size();
    }

    @Override
    public void onSwipeToDelete(int position, boolean confirmed) {
        if (confirmed) {
            filteredContacts.remove(position);
            contacts.clear();
            contacts.addAll(filteredContacts);
            notifyItemRemoved(position);
        } else {
            notifyItemChanged(position);
        }
    }

    public void attachSwipeToDelete(RecyclerView recyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber, textViewFirstName, textViewLastName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            textViewLastName = itemView.findViewById(R.id.textViewLastName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ContactModel contact);
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
                if (contact.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                        contact.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                        contact.getNumber().contains(query)) {
                    filteredContacts.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }
}