package masonemobile.restaurantlocator.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import masonemobile.restaurantlocator.Models.RestaurantModel;
import masonemobile.restaurantlocator.R;
import masonemobile.restaurantlocator.Utils.Utilities;

public class GridViewAdapter extends BaseAdapter {

    private Context context;

    private RestaurantModel restaurantModel;

    public GridViewAdapter(final Context context) {
        this.context = context;
    }

    public void setRestaurantModel(RestaurantModel restaurantModel) {
        this.restaurantModel = restaurantModel;
    }

    @Override
    public int getCount() {
        return restaurantModel==null?0:restaurantModel.getBusinesses().size();
    }

    @Override
    public Object getItem(int i) {
        return restaurantModel.getBusinesses().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View gridView = view;

        if (view == null){
            gridView = ((LayoutInflater) Objects.requireNonNull(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_grid_cell,null);
        }

        RestaurantModel.Business business = restaurantModel.getBusinesses().get(i);

        ImageView ivBasicImage = gridView.findViewById(R.id.imageview);
        Picasso.with(context).load(business.getImage_url()).into(ivBasicImage);

        String star_rating = String.format(context.getString(R.string.star_rating),"<font color=\'#303F9F\'>"+business.getRating()+"</font>", "<font color=\'#303F9F\'>"+business.getReview_count()+"</font>");
        ((TextView)gridView.findViewById(R.id.textviewstar)).setText(Html.fromHtml(star_rating,0));

        String dollar = String.format(context.getString(R.string.dollar_category),"<font color=\'#008304\'>"+(business.getPrice()==null?"N/A":business.getPrice())+"</font>");
        dollar = Utilities.getInstance().stringArrayToStringCategory(dollar,business.getCategories()," - ");
        Spanned dollarSpanned = Html.fromHtml(dollar,0);
        ((TextView)gridView.findViewById(R.id.textviewdollar)).setText(dollarSpanned);

        ((TextView)gridView.findViewById(R.id.nametextview)).setText(business.getName());

        ((TextView)gridView.findViewById(R.id.addresstextview)).setText(buildAddress(business.getLocation()));

        ((TextView)gridView.findViewById(R.id.textviewdistance)).setText(String.format(context.getString(R.string.distance),business.getDistance()/1000));

        return gridView;
    }

    private String buildAddress(RestaurantModel.Business.Location location){
        return location.getAddress1()+" "+location.getAddress2();
    }
}
