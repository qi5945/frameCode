package com.micropole.chebianjie.gridSelected;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.micropole.chebianjie.R;
import com.micropole.librarybase.utils.ImageLoader;

import java.util.ArrayList;

/**
 * Created by wangeason on 15/9/28.
 */
public class GridSelectedAdapter extends RecyclerView.Adapter<GridSelectedAdapter.ViewHolder> {
    private ArrayList<String> imgPaths;
    private Context mContext;
    private LayoutInflater inflater;
    int maxNum;
    private boolean isShowSelect = true;//是否显示添加图片按钮
    private RecyclerView mRecyclerView;
    private View VIEW_FOOTER;
    private View VIEW_HEADER;

    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;

    public OnImgClickListener getOnImgClickListener() {
        return onImgClickListener;
    }

    public void setOnImgClickListener(OnImgClickListener onImgClickListener) {
        this.onImgClickListener = onImgClickListener;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    private OnImgClickListener onImgClickListener;

    public GridSelectedAdapter(Context context, ArrayList<String> imgPaths, int maxNum) {
        this.imgPaths = imgPaths;
        this.mContext = context;
        this.maxNum = maxNum;
        inflater= LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new ViewHolder(VIEW_FOOTER);
        } else if (viewType == TYPE_HEADER) {
            return new ViewHolder(VIEW_HEADER);
        } else {
            return new ViewHolder(getLayout(R.layout.item_selected));
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!isHeaderView(position) && !isFooterView(position)) {
            if (haveHeaderView()) ;
            ImageView imgSelected = (ImageView) holder.itemView.findViewById(R.id.img_selected);
            TextView tvDelete = holder.itemView.findViewById(R.id.tv_delete);
            if(getPosition(position) < imgPaths.size()) {
                ImageLoader.loadToUrl(mContext,imgSelected,imgPaths.get(getPosition(position)));
//                Glide.with(mContext)
//                    .load(imgPaths.get(getPosition(position)))
//                        .into(new SimpleTarget<GlideDrawable>() {
//                            @Override
//                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                                imgSelected.setImageDrawable(resource);
//                            }
//                        });

            }else if(isShowSelect&&getPosition(position) == imgPaths.size()){
                tvDelete.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(R.drawable.btn_add)
                        .into(imgSelected);
            }
            if (isShowSelect&&getPosition(position) != imgPaths.size()){
                tvDelete.setVisibility(View.VISIBLE);
                tvDelete.setOnClickListener(v ->{
                    imgPaths.remove(getPosition(position));
                    deleteData(getPosition(position));
                    if (mDeleteListener != null)
                        mDeleteListener.onDelete();
                });
            }

            imgSelected.setOnClickListener(v -> {
                if (onImgClickListener != null) {
                    onImgClickListener.onClick(holder, getPosition(position));
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        int count = (imgPaths == null ? 0 : imgPaths.size());
        if (VIEW_FOOTER != null) {
            count++;
        }

        if (VIEW_HEADER != null) {
            count++;
        }
        if (count<maxNum&&isShowSelect){
            return count+1;
        }
        else {
            return count;
        }
    }
    private View getLayout(int layoutId) {
        return LayoutInflater.from(mContext).inflate(layoutId, null);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        } else if (isFooterView(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        try {
            if (mRecyclerView == null && mRecyclerView != recyclerView) {
                mRecyclerView = recyclerView;
            }
            ifGridLayoutManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addHeaderView(View headerView) {
        if (haveHeaderView()) {
            throw new IllegalStateException("hearview has already exists!");
        } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            VIEW_HEADER = headerView;
            ifGridLayoutManager();
            notifyItemInserted(0);
        }

    }

    public void addFooterView(View footerView) {
        if (haveFooterView()) {
            throw new IllegalStateException("footerView has already exists!");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(params);
            VIEW_FOOTER = footerView;
            ifGridLayoutManager();
            notifyItemInserted(getItemCount() - 1);
        }
    }

    private void ifGridLayoutManager() {
        if (mRecyclerView == null) return;
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup =
                    ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() :
                            1;
                }
            });
        }
    }

    private int getPosition(int position){
        if (haveHeaderView()){
            return position - 1;
        }
        else {
            return position;
        }
    }
    private boolean haveHeaderView() {
        return VIEW_HEADER != null;
    }

    public boolean haveFooterView() {
        return VIEW_FOOTER != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    private boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
    }

    public void deleteData(int position){
        int notifyItemStart = 0, notifyItemEnd = 0, temp = 0;
        if (haveHeaderView()){
            temp++;
            notifyItemStart = position+1;
        }
        else {
            notifyItemStart = position;
        }
        if (haveFooterView()){
            temp++;
        }
        notifyItemEnd = getItemCount()-position-temp;
        notifyItemRemoved(notifyItemStart);
        notifyItemRangeChanged(notifyItemStart,notifyItemEnd);
    }
    public void addData(int position){
        int notifyItemStart = 0, notifyItemEnd = 0, temp = 0;
        if (haveHeaderView()){
            temp++;
            notifyItemStart = position+1;
        }
        else {
            notifyItemStart = position;
        }
        if (haveFooterView()){
            temp++;
        }
        notifyItemEnd = getItemCount()-position-temp;
        notifyItemInserted(notifyItemStart);
        notifyItemRangeChanged(notifyItemStart,notifyItemEnd);
    }
    public void set(ArrayList<String> imgPaths){
        this.imgPaths = imgPaths;
        GridSelectedAdapter.this.notifyDataSetChanged();
    }
    public void setShowSelect(boolean select){
        this.isShowSelect = select;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private onDeleteListener mDeleteListener;

    public void setOnDeleteListener(onDeleteListener listener){
        this.mDeleteListener = listener;
    }

    public interface onDeleteListener{
        void onDelete();
    }
}
