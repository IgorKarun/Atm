package com.ikar.atm.main;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikar.atm.R;
import com.ikar.atm.common.db.DbQuery;
import com.ikar.atm.common.models.CashDeskItem;
import com.ikar.atm.common.utils.cache.ColumnIndexCache;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iKar on 11/5/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final static String TAG = RecyclerViewAdapter.class.getName();

    private Activity activity;
    private Cursor cursor;
    private ColumnIndexCache cache;

    public RecyclerViewAdapter(final Activity activity, Cursor cursor) {
        this.activity = activity;
        this.cursor = cursor;
        cache = new ColumnIndexCache();
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

        final CashDeskItem cashDeskItem = new CashDeskItem(cursor, cache);

        holder.price.setText(Integer.toString(cashDeskItem.getDenomination()) + "$");
        holder.amount.setText(Integer.toString(cashDeskItem.getInventory()));
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbQuery.deleteCashDeskItem(cashDeskItem.getRowId());
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_cashdesk_tv_price) TextView price;
        @BindView(R.id.item_cashdesk_tv_amount) TextView amount;
        @BindView(R.id.item_cashdesk_tv_del) TextView deleteItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
