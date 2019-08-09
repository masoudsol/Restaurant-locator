package masonemobile.restaurantlocator.modules.adapters;

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

import java.util.List;
import java.util.Objects;

import masonemobile.restaurantlocator.modules.models.Business;
import masonemobile.restaurantlocator.modules.models.Location;
import masonemobile.restaurantlocator.R;
import masonemobile.restaurantlocator.utils.Utilities;

public class GridViewAdapter extends BaseAdapter {

    private Context context;

    private List<Business> businesses;

    public GridViewAdapter(final Context context) {
        this.context = context;
    }

    public void setRestaurantModel(List<Business> businesses) {
        this.businesses = businesses;
    }

    @Override
    public int getCount() {
        return businesses.size();
    }

    @Override
    public Object getItem(int i) {
        return businesses.get(i);
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

        Business business = businesses.get(i);

        ImageView ivBasicImage = gridView.findViewById(R.id.imageview);
        Picasso.with(context).load(business.image_url).into(ivBasicImage);

        String star_rating = String.format(context.getString(R.string.star_rating),"<font color=\'#303F9F\'>"+business.rating+"</font>", "<font color=\'#303F9F\'>"+business.review_count+"</font>");
        ((TextView)gridView.findViewById(R.id.textviewstar)).setText(Html.fromHtml(star_rating,0));

        String dollar = String.format(context.getString(R.string.dollar_category),"<font color=\'#008304\'>"+(business.price==null?"N/A":business.price)+"</font>");
        dollar = Utilities.getInstance().stringArrayToStringCategory(dollar,business.categories," - ");
        Spanned dollarSpanned = Html.fromHtml(dollar,0);
        ((TextView)gridView.findViewById(R.id.textviewdollar)).setText(dollarSpanned);

        ((TextView)gridView.findViewById(R.id.nametextview)).setText(business.name);

        ((TextView)gridView.findViewById(R.id.addresstextview)).setText(buildAddress(business.location));

        ((TextView)gridView.findViewById(R.id.textviewdistance)).setText(String.format(context.getString(R.string.distance),business.distance/1000));

        return gridView;
    }

    private String buildAddress(Location location){
        return location.address1+" "+location.address2;
    }
}
