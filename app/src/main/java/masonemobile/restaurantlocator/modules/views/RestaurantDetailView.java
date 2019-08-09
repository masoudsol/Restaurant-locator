package masonemobile.restaurantlocator.modules.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import masonemobile.restaurantlocator.modules.models.Business;
import masonemobile.restaurantlocator.R;
import masonemobile.restaurantlocator.modules.viewmodels.DetailViewModel;
import masonemobile.restaurantlocator.utils.Utilities;

public class RestaurantDetailView extends AppCompatActivity {
    Business business;
    DetailViewModel detailViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resto_detail_view);

        detailViewModel = new DetailViewModel();

        business = detailViewModel.getBusinuess();

        ImageView ivBasicImage = findViewById(R.id.imageviewdetail);
        Picasso.with(this).load(business.image_url).into(ivBasicImage);

        String star_rating = String.format(getString(R.string.star_rating),"<font color=\'#3F51B5\'>"+business.rating+"</font>", "<font color=\'#3F51B5\'>"+business.review_count+"</font>");
        ((TextView)findViewById(R.id.textviewstardetail)).setText(Html.fromHtml(star_rating,0));

        String dollar = String.format(getString(R.string.dollar_category),"<font color=\'#008304\'>"+(business.price==null?"N/A":business.price)+"</font>");
        dollar = Utilities.getInstance().stringArrayToStringCategory(dollar,business.categories," - ");
        Spanned dollarSpanned = Html.fromHtml(dollar,0);
        ((TextView)findViewById(R.id.textviewdollardetail)).setText(dollarSpanned);

        ((TextView)findViewById(R.id.nametextviewdetail)).setText(business.name);

        String address = Utilities.getInstance().stringArrayToString("",business.location.display_address,", ");
        ((TextView)findViewById(R.id.addresstextviewdetail)).setText(address);

        ((TextView)findViewById(R.id.textviewdistancedetail)).setText(String.format(getString(R.string.distance),business.distance/1000));

        ((TextView)findViewById(R.id.closed)).setText(business.is_closed?getString(R.string.closed):getString(R.string.open));

        String transaction = Utilities.getInstance().stringArrayToString("",business.transactions,", ");
        ((TextView)findViewById(R.id.transaction)).setText(transaction);

        ((TextView)findViewById(R.id.phone)).setText(business.display_phone);

    }
}
