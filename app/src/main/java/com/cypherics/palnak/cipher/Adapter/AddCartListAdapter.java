package com.cypherics.palnak.cipher.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cypherics.palnak.cipher.AvalaibleApps;
import com.cypherics.palnak.cipher.R;
import com.pkmmte.view.CircularImageView;

import java.util.List;


/**
 * Created by palnak on 14-1-18.
 */


public class AddCartListAdapter extends RecyclerView.Adapter<AddCartListAdapter.MyViewHolder> {
    public Context context;
    private List<AvalaibleApps> cartList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CircularImageView thumbnail;
        public RelativeLayout viewBackgroundAdd, viewForegroundAdd, viewBackgroundDelete;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name_add);
            thumbnail = view.findViewById(R.id.thumbnail_add);
            viewBackgroundAdd = view.findViewById(R.id.view_background_add);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);

            viewForegroundAdd = view.findViewById(R.id.view_foreground_add);

        }
    }


    public AddCartListAdapter(Context context, List<AvalaibleApps> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AvalaibleApps avalaibleApps = cartList.get(position);
        holder.name.setText(avalaibleApps.getName());
        holder.thumbnail.setImageDrawable(avalaibleApps.getThumbnail());
//
//        Glide.with(context)
//                .load(item.getThumbnail())
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        cartList.remove(position);

        notifyItemRemoved(position);
    }

    public void restoreItem(AvalaibleApps avalaibleApps, int position) {
        cartList.add(position, avalaibleApps);
        // notify item added by position
        notifyItemInserted(position);
    }
}