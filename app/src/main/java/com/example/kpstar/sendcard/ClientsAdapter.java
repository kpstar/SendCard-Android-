package com.example.kpstar.sendcard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by KpStar on 3/10/2018.
 */

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.MyViewHolder> {

    private static RecyclerViewClickListener itemListener;

    private List<ClientsModel> mClientItems;
    private Context mContext;

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Pass in the contact array into the constructor
    public ClientsAdapter(Context context, List<ClientsModel> notifyItems) {
        mClientItems = notifyItems;
        mContext = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, email, no, created, num;

        public MyViewHolder(View view) {
            super(view);

            no = (TextView)view.findViewById(R.id.txtNo);

            name = (TextView)view.findViewById(R.id.txtUsername);
            email = (TextView)view.findViewById(R.id.txtEmail);
            created = (TextView)view.findViewById(R.id.txtCreated);
            num = (TextView)view.findViewById(R.id.txtNum);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }

    public ClientsAdapter(Context context, List<ClientsModel> notifyItems, RecyclerViewClickListener itemListener) {
        this.mContext = context;

        mClientItems = notifyItems;

        this.itemListener = itemListener;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tablecell, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        ClientsModel notifyItem = mClientItems.get(position);

        TextView tempTxt = viewHolder.name;
        tempTxt.setText(notifyItem.getmName());
        tempTxt = viewHolder.email;
        tempTxt.setText(notifyItem.getmEmail());
        tempTxt = viewHolder.created;
        tempTxt.setText(notifyItem.getmCreated());
        tempTxt = viewHolder.no;
        tempTxt.setText(notifyItem.getmID());
        tempTxt = viewHolder.num;
        tempTxt.setText(notifyItem.getmNum());
    }

    @Override
    public int getItemCount() {
        return mClientItems.size();
    }



    public interface RecyclerViewClickListener
    {
        public void recyclerViewListClicked(View v, int position);
    }

    public void removeItem(int position) {
        mClientItems.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ClientsModel item, int position) {
        mClientItems.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
