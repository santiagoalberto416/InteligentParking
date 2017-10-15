package com.example.macbook.smartparking;

/**
 * Created by santiagoalbertokirk on 14/10/17.
 */

import android.app.Dialog;
import android.content.Context;

import java.util.List;


public final class OptionSpotParking extends Dialog {

    private Context mContext;

    public OptionSpotParking(Context context, int position) {
        super(context);
        setContentView(R.layout.select_spot_layout);
        this.mContext = context;
    }

}
