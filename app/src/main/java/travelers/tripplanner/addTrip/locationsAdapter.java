package travelers.tripplanner.addTrip;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import travelers.tripplanner.R;

public class locationsAdapter extends ArrayAdapter<String> {
    private Activity mActivity;
    private ArrayList<String> name, type, address, imageURl;
    private ArrayList<Double> rating;

    public locationsAdapter(@NonNull Activity activity, ArrayList<String> name, ArrayList<String> type, ArrayList<String> address, ArrayList<String> imageURl, ArrayList<Double> rating) {
        super(activity, R.layout.place_item, name);
        this.mActivity = activity;
        this.name = name;
        this.type = type;
        this.address = address;
        this.imageURl = imageURl;
        this.rating = rating;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View mView = convertView;
        if(mView==null){
            LayoutInflater inflater = mActivity.getLayoutInflater();
            mView = inflater.inflate(R.layout.place_item, null, true);
        }

        TextView nametv = mView.findViewById(R.id.name);
        TextView typetv = mView.findViewById(R.id.type);
        TextView addressstv = mView.findViewById(R.id.addresss);
        RatingBar rating = mView.findViewById(R.id.rating);
        ImageView imagePlace = mView.findViewById(R.id.imagePlace);

        nametv.setText(name.get(position));
        typetv.setText(type.get(position));
        addressstv.setText(address.get(position));
        rating.setNumStars(this.rating.get(position).intValue());
        imagePlace.setImageResource(R.drawable.ic_account);

        Picasso.with(mActivity).load(imageURl.get(position)).into(imagePlace);

        return mView;
    }
}
