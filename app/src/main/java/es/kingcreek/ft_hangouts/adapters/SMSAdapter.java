package es.kingcreek.ft_hangouts.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.database.SMSDataSource;
import es.kingcreek.ft_hangouts.models.SMSModel;
import es.kingcreek.ft_hangouts.views.SwipeToDeleteCallback;

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.ViewHolder> implements SwipeToDeleteCallback.SwipeToDeleteListener {

    private List<SMSModel> sms;
    private Context context;
    private final ItemTouchHelper itemTouchHelper;

    public SMSAdapter(Context context, List<SMSModel> sms) {
        this.context = context;
        this.sms = sms;
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context, this, context.getString(R.string.confirmation_msg_sms));
        itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sms_list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SMSModel smsModel = sms.get(position);

        holder.textViewDate.setText(smsModel.getTime());
        holder.textViewMessage.setText(smsModel.getMessage());
        if (smsModel.getInOut() == 1)
            holder.imageViewMessageType.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowdown));
        else
            holder.imageViewMessageType.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowup));

    }

    @Override
    public int getItemCount() {
        return sms.size();
    }

    @Override
    public void onSwipeToDelete(int position, boolean confirmed) {
        if (confirmed) {

            SMSModel smsModel = sms.get(position);
            SMSDataSource.getInstance(context).removeSMSById(smsModel.getId());
            sms.remove(position);
            notifyItemRemoved(position);
        } else {
            notifyItemChanged(position);
        }
    }

    public void attachSwipeToDelete(RecyclerView recyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewMessage;
        ImageView imageViewMessageType;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            imageViewMessageType = itemView.findViewById(R.id.imageViewMessageType);
        }
    }
}