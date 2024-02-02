package es.kingcreek.ft_hangouts.adapters;

import static es.kingcreek.ft_hangouts.helper.Constants.ADD_CONTACT_REQUEST_CODE;
import static es.kingcreek.ft_hangouts.helper.Constants.VIEW_CONTACT_REQUEST_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.activities.ContactDetails;
import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.database.SMSDataSource;
import es.kingcreek.ft_hangouts.helper.BitmapHelper;
import es.kingcreek.ft_hangouts.models.ContactModel;
import es.kingcreek.ft_hangouts.views.SwipeToDeleteCallback;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements SwipeToDeleteCallback.SwipeToDeleteListener {

    private final String TAG = "ContactAdapter";
    private List<ContactModel> contacts;
    private List<ContactModel> filteredContacts;
    private Context context;
    private final ItemTouchHelper itemTouchHelper;
    private boolean isFiltering = false;
    private String filter = "";

    public ContactAdapter(Context context, List<ContactModel> contacts, List<ContactModel> filteredContacts) {
        this.context = context;
        this.contacts = contacts;
        this.filteredContacts = filteredContacts;
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context, this, context.getString(R.string.confirmation_msg));
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
        if (!contact.getImage().isEmpty()) {
            BitmapHelper.LoadImageTask loadImageTask = new BitmapHelper.LoadImageTask(holder.imageViewContact);
            loadImageTask.execute(contact.getImage());
            //holder.imageViewContact.setImageURI(Uri.parse(contact.getImage()));
        }else{
            holder.imageViewContact.setImageDrawable(context.getDrawable(R.drawable.profile));
        }

        holder.element.setOnClickListener(v -> {
            Intent i = new Intent(context, ContactDetails.class);
            i.putExtra("contact", contact.getId());
            ((Activity) context).startActivityForResult(i, VIEW_CONTACT_REQUEST_CODE);
        });

    }

    @Override
    public int getItemCount() {
        return filteredContacts.size();
    }

    @Override
    public void onSwipeToDelete(int position, boolean confirmed) {
        if (confirmed) {
            int originalPosition = -1;
            for (int i = 0; i < filteredContacts.size(); i++) {
                if (contacts.get(i).getId() == filteredContacts.get(position).getId()) {
                    originalPosition = i;
                    break;
                }
            }
            if (originalPosition != -1) {
                //remove from DB
                ContactModel contactModel = contacts.get(originalPosition);

                ContactDataSource.getInstance(context).removeContactById(contactModel.getId());
                SMSDataSource.getInstance(context).removeMessagesFromContact(contactModel.getId());
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
        ImageView imageViewContact;
        LinearLayout element;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            textViewLastName = itemView.findViewById(R.id.textViewLastName);
            element = itemView.findViewById(R.id.element);
            imageViewContact = itemView.findViewById(R.id.imageViewContact);

        }
    }

    // Search filter
    public void filter(String query) {
        filteredContacts.clear();
        filter = query;
        // If search are empty
        if (TextUtils.isEmpty(query)) {
            filteredContacts.addAll(contacts);
            isFiltering = false;
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
            isFiltering = true;
        }
        notifyDataSetChanged();
    }

    public String getFilter()
    {
        return filter;
    }

    public boolean isFiltering()
    {
        return isFiltering;
    }
}