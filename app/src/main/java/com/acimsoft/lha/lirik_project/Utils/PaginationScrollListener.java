package com.acimsoft.lha.lirik_project.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutmanager;

    public PaginationScrollListener(LinearLayoutManager layoutmanager) {
        this.layoutmanager = layoutmanager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutmanager.getChildCount();
        int totalItemCount = layoutmanager.getItemCount();
        int firstVisibleItemPosition = layoutmanager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()){
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= getTotalPageCount()) {
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public  abstract boolean isLoading();
}
