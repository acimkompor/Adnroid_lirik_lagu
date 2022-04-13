package com.acimsoft.lha.lirik_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.R;
import com.acimsoft.lha.lirik_project.ViewLirik;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.acimsoft.lha.lirik_project.Utils.Pengaturan.formatRupiah;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.holder> {

    List<DataModel> mlist;
    Context context;

    public AdapterHistory(Context context, List<DataModel> mlist) {
        this.mlist = mlist;
        this.context = context;
    }



    @NonNull
    @Override
    public AdapterHistory.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_top_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterHistory.holder holder, int position) {
        final DataModel dm = mlist.get(position);
        holder.txtArtis.setText(dm.getVocal());
        holder.txtJudul.setText(dm.getJudul());
        holder.txtViewer.setText("dilihat " + dm.getViewer());
        Glide.with(context)
                .load(dm.getProfil())
                .centerCrop()
                .into(holder.imgArtis);

        holder.imgArtis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewLirik.class);
                intent.putExtra("data", dm);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        ImageView imgArtis;
        TextView txtArtis, txtJudul, txtViewer;

        public holder(@NonNull View itemView) {
            super(itemView);
            imgArtis = itemView.findViewById(R.id.imgArtis);
            txtArtis = itemView.findViewById(R.id.txtArtis);
            txtJudul = itemView.findViewById(R.id.txtJudul);
            txtViewer = itemView.findViewById(R.id.txtviewer);

        }
    }
}
