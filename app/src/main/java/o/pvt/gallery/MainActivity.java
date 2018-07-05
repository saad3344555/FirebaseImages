package o.pvt.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> images;
    GridView gallery;
    public static final int PERMISSION_ALL = 143;
    private ArrayList<String> imageNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gallery = (GridView) findViewById(R.id.galleryGridView);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty())
                    Toast.makeText(
                            getApplicationContext(),
                            "position " + position + " " + images.get(position),
                            Toast.LENGTH_SHORT).show();


            }
        });

        requestPermissions();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    gallery.setAdapter(new ImageAdapter(this));

                } else {
                    finish();
                }
        }
    }

    void requestPermissions() {
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else {
            gallery.setAdapter(new ImageAdapter(this));
            FirebaseHelper firebaseHelper = new FirebaseHelper();
            for(String image: images){
                Uri img = Uri.fromFile(new File(image));
                String name = new File(image).getName();
                firebaseHelper.uploadImage(img, name);
            }
        }
    }

    boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private class ImageAdapter extends BaseAdapter {

        /**
         * The context.
         */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext the local context
         */
        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_XY);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(images.get(position))
                    .placeholder(R.mipmap.ic_launcher).centerCrop()
                    .into(picturesView);

            return picturesView;
        }

        /**
         * Getting All Images Path.
         *
         * @param activity the activity
         * @return ArrayList with images Path
         */
        private ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data;
            ArrayList<String> listOfAllImages = new ArrayList<>();
            imageNames = new ArrayList<>();
            String absolutePathOfImage = null;
            String imageName = null;

            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }
            return listOfAllImages;
        }
    }
}

