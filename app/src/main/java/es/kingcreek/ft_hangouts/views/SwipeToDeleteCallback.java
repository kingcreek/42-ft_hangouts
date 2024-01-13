package es.kingcreek.ft_hangouts.views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

    private final SwipeToDeleteListener listener;
    private final Context context;

    public SwipeToDeleteCallback(Context context, SwipeToDeleteListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT); // Allow only Left slice
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT) {
            // Show confirmation dialog
            showDeleteConfirmationDialog(viewHolder.getAdapterPosition());
        }
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar eliminación")
                .setMessage("¿Seguro que quieres eliminar este contacto?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onSwipeToDelete(position, true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onSwipeToDelete(position, false);
                    }
                })
                .show();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // Slice effect personalization
        // Scale, transparency, rotation... but let's do simple
        // X axis translation
        final float translationX = Math.min(-dX, viewHolder.itemView.getWidth() / 2);
        viewHolder.itemView.setTranslationX(translationX);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public interface SwipeToDeleteListener {
        void onSwipeToDelete(int position, boolean confirmed);
    }
}