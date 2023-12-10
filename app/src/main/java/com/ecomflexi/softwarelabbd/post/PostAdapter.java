package com.ecomflexi.softwarelabbd.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecomflexi.softwarelabbd.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> postList;
    private List<Post> filteredList;
    private Context context;
    private String allorSell;

    public PostAdapter(Context context, List<Post> postList, List<Post> filteredList, String allorSell) {
        this.context = context;
        this.postList = postList;
        this.filteredList = filteredList;
        this.allorSell = allorSell;
    }

    public void setFilteredList(List<Post> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = filteredList.get(position);

        holder.titleTextView.setText("Name: " + post.getTitle());
        holder.detailsTextView.setText("Details: " + post.getDetails());
        holder.tvCode.setText("Code: " + post.getCode());
        holder.tvPrice.setText("Price: " + post.getPrice());
        holder.tvStockOrSell.setText("Status: " + post.getStockOrSell());
        holder.totalcount.setText("Total: " + post.getTotalcount());
        holder.tvcategory.setText("Category: " + post.getCategory());

        if (post.getImage1().equals("null")) {
            holder.imageView1.setVisibility(View.GONE);
        } else {
            holder.imageView1.setVisibility(View.VISIBLE);
            Picasso.get().load(HomeWeb.IMAGE_URL + post.getImage1()).placeholder(R.drawable.progressbar_ic).error(R.drawable.loading_error).into(holder.imageView1);
        }
        if (post.getImage2().equals("null")) {
            holder.imageView2.setVisibility(View.GONE);
        } else {
            holder.imageView2.setVisibility(View.VISIBLE);
            Picasso.get().load(HomeWeb.IMAGE_URL + post.getImage2()).placeholder(R.drawable.progressbar_ic).error(R.drawable.loading_error).into(holder.imageView2);
        }
        if (post.getImage3().equals("null")) {
            holder.imageView3.setVisibility(View.GONE);
        } else {
            holder.imageView3.setVisibility(View.VISIBLE);
            Picasso.get().load(HomeWeb.IMAGE_URL + post.getImage3()).placeholder(R.drawable.progressbar_ic).error(R.drawable.loading_error).into(holder.imageView3);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allorSell.equals("/get_posts.php")) {
                    Intent intent = new Intent(context, UpdatePostActivity.class);
                    intent.putExtra("post_id", post.getId());
                    intent.putExtra("title", post.getTitle());
                    intent.putExtra("details", post.getDetails());
                    intent.putExtra("Code", post.getCode());
                    intent.putExtra("Price", post.getPrice());
                    intent.putExtra("StockOrSell", post.getStockOrSell());
                    intent.putExtra("totalcount", post.getTotalcount());
                    intent.putExtra("category", post.getCategory());
                    intent.putExtra("image1", HomeWeb.IMAGE_URL + post.getImage1());
                    intent.putExtra("image2", HomeWeb.IMAGE_URL + post.getImage2());
                    intent.putExtra("image3", HomeWeb.IMAGE_URL + post.getImage3());
                    intent.putExtra("postOrSell", "/delete_post.php");
                    intent.putExtra("allorSell", allorSell);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    Intent intent = new Intent(context, UpdatePostActivity.class);
                    intent.putExtra("post_id", post.getId());
                    intent.putExtra("title", post.getTitle());
                    intent.putExtra("details", post.getDetails());
                    intent.putExtra("Code", post.getCode());
                    intent.putExtra("Price", post.getPrice());
                    intent.putExtra("StockOrSell", post.getStockOrSell());
                    intent.putExtra("totalcount", post.getTotalcount());
                    intent.putExtra("category", post.getCategory());
                    intent.putExtra("image1", HomeWeb.IMAGE_URL + post.getImage1());
                    intent.putExtra("image2", HomeWeb.IMAGE_URL + post.getImage2());
                    intent.putExtra("image3", HomeWeb.IMAGE_URL + post.getImage3());
                    intent.putExtra("postOrSell", "/sell_delete.php");
                    intent.putExtra("allorSell", allorSell);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, detailsTextView, tvCode, tvPrice, tvStockOrSell, totalcount , tvcategory;
        public ImageView imageView1, imageView2, imageView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            detailsTextView = itemView.findViewById(R.id.detailsTextView);
            tvCode = itemView.findViewById(R.id.codeTextView);
            tvPrice = itemView.findViewById(R.id.priceTextView);
            tvStockOrSell = itemView.findViewById(R.id.StockOrSellTextView);
            tvcategory = itemView.findViewById(R.id.categoryTextView);
            imageView1 = itemView.findViewById(R.id.imageView1);
            imageView2 = itemView.findViewById(R.id.imageView2);
            imageView3 = itemView.findViewById(R.id.imageView3);
            totalcount = itemView.findViewById(R.id.totalcountTextView);
        }
    }
}
