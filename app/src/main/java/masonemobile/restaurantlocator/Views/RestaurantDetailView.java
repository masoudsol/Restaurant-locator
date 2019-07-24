package masonemobile.restaurantlocator.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import masonemobile.restaurantlocator.Models.RestaurantModel;
import masonemobile.restaurantlocator.R;
import masonemobile.restaurantlocator.Utils.Utilities;

public class RestaurantDetailView extends AppCompatActivity {
    RestaurantModel.Business business;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resto_detail_view);

        business = (RestaurantModel.Business) getIntent().getSerializableExtra(Utilities.getInstance().BUNDLEID);

        ImageView ivBasicImage = findViewById(R.id.imageviewdetail);
        Picasso.with(this).load(business.getImage_url()).into(ivBasicImage);

        String star_rating = String.format(getString(R.string.star_rating),"<font color=\'#3F51B5\'>"+business.getRating()+"</font>", "<font color=\'#3F51B5\'>"+business.getReview_count()+"</font>");
        ((TextView)findViewById(R.id.textviewstardetail)).setText(Html.fromHtml(star_rating,0));

        String dollar = String.format(getString(R.string.dollar_category),"<font color=\'#008304\'>"+(business.getPrice()==null?"N/A":business.getPrice())+"</font>");
        dollar = Utilities.getInstance().stringArrayToStringCategory(dollar,business.getCategories()," - ");
        Spanned dollarSpanned = Html.fromHtml(dollar,0);
        ((TextView)findViewById(R.id.textviewdollardetail)).setText(dollarSpanned);

        ((TextView)findViewById(R.id.nametextviewdetail)).setText(business.getName());

        String address = Utilities.getInstance().stringArrayToString("",business.getLocation().getDisplay_address(),", ");
        ((TextView)findViewById(R.id.addresstextviewdetail)).setText(address);

        ((TextView)findViewById(R.id.textviewdistancedetail)).setText(String.format(getString(R.string.distance),business.getDistance()/1000));

        ((TextView)findViewById(R.id.closed)).setText(business.isIs_closed()?getString(R.string.closed):getString(R.string.open));

        String transaction = Utilities.getInstance().stringArrayToString("",business.getTransactions(),", ");
        ((TextView)findViewById(R.id.transaction)).setText(transaction);

        ((TextView)findViewById(R.id.phone)).setText(business.getDisplay_phone());

    }
}
