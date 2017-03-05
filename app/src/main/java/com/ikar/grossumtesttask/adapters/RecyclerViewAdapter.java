package com.ikar.grossumtesttask.adapters;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikar.grossumtesttask.R;
import com.ikar.grossumtesttask.db.UriMatcherHelper;
import com.ikar.grossumtesttask.db.scheme.TableCashDesk;

/**
 * Created by iKar on 11/5/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final static String TAG = RecyclerViewAdapter.class.getName();

    private Activity activity;
    private Cursor cursor;

    public RecyclerViewAdapter(final Activity activity, Cursor cursor) {
        this.activity = activity;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cashdesk, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);

        final int rowId = cursor.getInt(cursor.getColumnIndex(TableCashDesk._ID));
        final String price = cursor.getString(cursor.getColumnIndex(TableCashDesk._DENOMINATION));
        final String amount = cursor.getString(cursor.getColumnIndex(TableCashDesk._INVENTORY));

        holder.price.setText(price + "$");
        holder.amount.setText(amount);
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(rowId);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public Cursor getItem(int pos) {
        if (cursor != null) {
            cursor.moveToPosition(pos);
        }
        return cursor;
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (cursor == newCursor) {
            return null;
        }
        Cursor oldCursor = cursor;
        this.cursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    private void deleteItem(int rowId) {
        String[] selectionArgs=new String[]{String.valueOf(rowId)};
        activity.getContentResolver().delete(UriMatcherHelper.CONTENT_URI,
                TableCashDesk._ID +"=?", selectionArgs);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView price;
        private TextView amount;
        private TextView deleteItem;

        public ViewHolder(View view) {
            super(view);
            price = (TextView) itemView.findViewById(R.id.item_cashdesk_tv_price);
            amount = (TextView) itemView.findViewById(R.id.item_cashdesk_tv_amount);
            deleteItem = (TextView) itemView.findViewById(R.id.item_cashdesk_tv_del);
        }
    }

}
