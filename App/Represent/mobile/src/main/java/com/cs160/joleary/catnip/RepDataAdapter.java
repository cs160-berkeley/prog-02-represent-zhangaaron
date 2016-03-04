package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aaron on 3/1/16.
 */
public class RepDataAdapter extends RecyclerView.Adapter<RepDataAdapter.RepViewHolder>  {
    List<Representative> repList;
    Context app_context;
    public RepDataAdapter (List<Representative> repList, Context app_context) {
        this.repList = repList;
        this.app_context = app_context;
    }
    public static class RepViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView website;
        TextView email;
        TextView name;
        ImageView rep_photo;
        Button button;

        RepViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            website = (TextView)itemView.findViewById(R.id.website_text);
            name = (TextView)itemView.findViewById(R.id.representative_name);
            email = (TextView)itemView.findViewById(R.id.email_text);
            rep_photo = (ImageView)itemView.findViewById(R.id.rep_photo);
            button = (Button)itemView.findViewById(R.id.button);
        }
    }

    @Override
    public int getItemCount() {
        return repList.size();
    }

    @Override
    public RepViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        RepViewHolder rep = new RepViewHolder(v);
        return rep;
    }

    @Override
    public void onBindViewHolder(RepViewHolder repView, int i) {
        repView.website.setText(repList.get(i).website);
        repView.name.setText(repList.get(i).name);
        repView.email.setText(repList.get(i).email);
        repView.rep_photo.setImageResource(repList.get(i).photoID);
        repView.button.setOnClickListener(new ButtonListener(i, app_context));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ButtonListener implements View.OnClickListener {
        private int id;
        private Context context;
        ButtonListener(int id, Context context) {
            this.id = id;
            this.context = context;
        }
        @Override
        public void onClick(View v) {
            Intent detailed_view = new Intent(context, DetailedView.class);
            detailed_view.putExtra("name", repList.get(id).name);
            detailed_view.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(detailed_view);
        }
    }

}
