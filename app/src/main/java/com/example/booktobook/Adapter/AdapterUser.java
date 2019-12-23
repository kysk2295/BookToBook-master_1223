package com.example.booktobook.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.example.booktobook.R;
import com.example.booktobook.Model.User;

import java.util.ArrayList;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {
    ArrayList<User> userArrayList;

    public AdapterUser(ArrayList<User> userArrayList) {
        this.userArrayList=userArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ExpandableCardView expandableCardView;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            expandableCardView=itemView.findViewById(R.id.item_rank_expandable);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final User user =userArrayList.get(position);

        holder.expandableCardView.setTitle(""+user.getId());
        holder.expandableCardView.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged(View v, boolean isExpanded) {

                TextView point=v.findViewById(R.id.textview_point);
                TextView read=v.findViewById(R.id.textview_readbookCnt);
                TextView enroll=v.findViewById(R.id.textview_enrollbookCnt);

                point.setText(""+user.getPoint());
                read.setText(""+user.getBorrowed_book_count());
                enroll.setText(""+user.getUploaded_book_count());

            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
}
