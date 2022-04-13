package com.acimsoft.lha.lirik_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.FavoriteFragment;
import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.acimsoft.lha.lirik_project.R;
import com.acimsoft.lha.lirik_project.ViewLirik;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.holder>{

    private Context context;
    private List<DataModel> list;

    public AdapterFavorite(Context context, List<DataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterFavorite.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext())
                .inflate( R.layout.item_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavorite.holder holder, int position) {
        final DataModel dm = list.get(position);
        holder.txtNama.setText(dm.getNama());
        holder.txtTgl.setText(dm.getCreate());
        holder.txtLike.setText(dm.getLiked());
        holder.txtJenis.setText(dm.getJenis());
        holder.txtArtis.setText(dm.getVocal() + " - " + dm.getJudul());
//        holder.imgProf.setText(dm.getNama().substring(0,1));

        Glide.with(context)
                .load(dm.getProfil())
                .centerCrop()
                .into(holder.imgProf);

        holder.rvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, dm.getProfil(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), ViewLirik.class);
                intent.putExtra("data", dm);
                context.startActivity(intent);
            }
        });


        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiRetro api = RetroClient.getApiRetro();
                Call<ResponModel> del = api.delfavorite(Build.ID, dm.getId());
                del.enqueue(new Callback<ResponModel>() {
                    @Override
                    public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                        Toast.makeText(context, "satu data dihapus dari favorite", Toast.LENGTH_SHORT).show();
                        FavoriteFragment.getInstance().fetching();
                    }

                    @Override
                    public void onFailure(Call<ResponModel> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        CircleImageView imgProf;
        ImageView imglike, imgDel;
        TextView txtTgl, txtNama, txtLike, txtJenis, txtArtis;
        RelativeLayout rvFavorite;
        public holder(@NonNull View itemView) {
            super(itemView);

            rvFavorite = itemView.findViewById(R.id.rvFavorite);
            imglike = itemView.findViewById(R.id.imgstart);
            imgDel = itemView.findViewById(R.id.imgDelete);
            txtTgl = itemView.findViewById(R.id.txtTgl);
            txtNama = itemView.findViewById(R.id.txtnama);
            txtLike = itemView.findViewById(R.id.txtLiked);
            imgProf = itemView.findViewById(R.id.imgProf);
            txtJenis = itemView.findViewById(R.id.txtjenis);
            txtArtis = itemView.findViewById(R.id.txtArtis);

        }
    }



}
