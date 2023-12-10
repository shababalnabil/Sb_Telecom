package com.ecomflexi.softwarelabbd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.ecomflexi.softwarelabbd.Config;
import com.ecomflexi.softwarelabbd.R;
import com.ecomflexi.softwarelabbd.activities.ActivityCart;
import com.ecomflexi.softwarelabbd.models.Cart;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.List;
import java.util.Locale;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {

    private Context context;
    private List<Cart> arrayCart;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name;
        TextView product_quantity;
        TextView product_price;
        ImageView product_image;

        public ViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.product_name);
            product_quantity = view.findViewById(R.id.product_quantity);
            product_price = view.findViewById(R.id.product_price);
            product_image = view.findViewById(R.id.product_image);
        }

    }

    public AdapterCart(Context context, List<Cart> arrayCart) {
        this.context = context;
        this.arrayCart = arrayCart;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.product_name.setText(ActivityCart.product_name.get(position));

        if (Config.ENABLE_DECIMAL_ROUNDING) {
            double _single_item = ActivityCart.sub_total_price.get(position) / ActivityCart.product_quantity.get(position);
            String single_item_price = String.format(Locale.GERMAN, "%1$,.0f", _single_item);

            holder.product_quantity.setText(single_item_price + " " + ActivityCart.currency_code.get(position) + " x " + ActivityCart.product_quantity.get(position));

            String price = String.format(Locale.GERMAN, "%1$,.0f", ActivityCart.sub_total_price.get(position));
            holder.product_price.setText(price + " " + ActivityCart.currency_code.get(position));
        } else {
            double _single_item = ActivityCart.sub_total_price.get(position) / ActivityCart.product_quantity.get(position);

            holder.product_quantity.setText(_single_item + " " + ActivityCart.currency_code.get(position) + " x " + ActivityCart.product_quantity.get(position));

            holder.product_price.setText(ActivityCart.sub_total_price.get(position) + " " + ActivityCart.currency_code.get(position));
        }

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(8)
                .oval(false)
                .build();

        Picasso.get()
                .load(Config.ADMIN_PANEL_URL + "/upload/product/" + ActivityCart.product_image.get(position))
                .placeholder(R.drawable.ic_loading)
                .resize(250, 250)
                .centerCrop()
                .transform(transformation)
                .into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return ActivityCart.product_id.size();
    }

}
