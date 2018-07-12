package o.pvt.gallery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by saq on 7/8/18.
 */

public class CameraEventReciver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("capture hitt","hitt");
        List<String> images = AppModel.getInstance().getAllShownImagesPath( context);
        FirebaseHelper firebaseHelper = new FirebaseHelper();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for(String image: images){

            try {

                Uri img = Uri.fromFile(new File(image));
                String name = new File(image).getName();

                String date =sdf.format(new Date(new File(image).lastModified()));
//                String formattedDate = sdf.parse(date).toString();
                String currDate = sdf.format(Calendar.getInstance().getTime());

                if(date.equals(currDate))
                    firebaseHelper.uploadImage(img, name, date);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
