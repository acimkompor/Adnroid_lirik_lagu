package com.acimsoft.lha.lirik_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSearchNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<DataModel> mList;
    private Context context;

    private boolean isLoadingAdded = false;
    private SelectedItem selectedItem;

    public AdapterSearchNew(Context context, SelectedItem selectedItem) {
        mList = new ArrayList<>();
        this.context = context;
        this.selectedItem = selectedItem;
    }

    public List<DataModel> getAllData(){
        return mList;
    }

    public void setAllData(List<DataModel> mList){
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;

            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent,false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_search, parent, false);
        viewHolder = new DataVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final DataModel dm = mList.get(position);

        switch (getItemViewType(position)){
            case ITEM:
                final DataVH dataVH = (DataVH) holder;

                String found_like = dm.getFound_like();
                dataVH.txtJenis.setText(dm.getJenis());
                dataVH.txtJudul.setText(dm.getJudul());
                dataVH.txtArtis.setText(dm.getVocal());
                dataVH.txtLike.setText(dm.getLiked());
                dataVH.txtView.setText(dm.getViewer());

                Glide.with(context)
                        .load(dm.getProfil())
                        .centerCrop()
                        .into(dataVH.imgLogo);
                if (found_like.equals("1"))
                    dataVH.imglike.setImageResource(R.drawable.ic_thumbpx);
                else
                    dataVH.imglike.setImageResource(R.drawable.ic_thumb_orange);

                break;

            case LOADING:

                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public interface SelectedItem{
        void selectedItem(DataModel dataModel);
    }



    /*
   View Holders
   _________________________________________________________________________________________________
    */

    private class DataVH extends RecyclerView.ViewHolder {

        ImageView imgLogo, imglike;
        TextView txtJenis, txtView, txtLike, txtArtis, txtJudul;

        public DataVH(View itemView) {
            super(itemView);

            imgLogo = itemView.findViewById(R.id.imgLogo);
            imglike = itemView.findViewById(R.id.imgLike);
            txtView = itemView.findViewById(R.id.txtView);
            txtJenis = itemView.findViewById(R.id.txtJenis);
            txtLike = itemView.findViewById(R.id.txtLike);
            txtJudul = itemView.findViewById(R.id.txtJudul);
            txtArtis = itemView.findViewById(R.id.txtArtis);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedItem.selectedItem(mList.get(getAdapterPosition()));
                }
            });
        }
    }

    private class LoadingVH extends RecyclerView.ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }



    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(DataModel dm){
        mList.add(dm);
        notifyItemInserted(mList.size() -1);
    }

    public void addAll(List<DataModel> dm){
        for (DataModel dataModel : dm){
            add(dataModel);
        }
    }

    public void remove(DataModel dm){
        int position = mList.indexOf(dm);
        if (position > -1){
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear(){
        isLoadingAdded = false;
        while (getItemCount() > 0){
            remove(getItem(0));
        }
    }

    public boolean isEmpty(){
        return getItemCount() == 0;
    }

    public void addLoadingFooter(){
        isLoadingAdded = true;
        add(new DataModel());
    }

    public void removeLoadingFooter(){
        isLoadingAdded = false;

        int position = mList.size() - 1;
        DataModel dm = getItem(position);

        if (dm != null){
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private DataModel getItem(int i) {
        return mList.get(i);
    }
}
