package o.pvt.gallery;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Build;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;

/**
 * Created by Saad Abdul Qadir on 6/22/2018.
 */

public class FirebaseHelper  {
    StorageReference storageReference;
    DatabaseReference databaseReference;

    public FirebaseHelper(){
        this.storageReference = FirebaseStorage.getInstance().getReference();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadImage(Uri uri, final String name, final String date){
        storageReference.child(name).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                DetailsModel dm = new DetailsModel();
                dm.setDateModified(date);
                dm.setImageName(name);
                dm.setImageUrl(downloadUri.toString());
                Log.d("DownloadUrl",downloadUri.toString());
                uploadDetails(dm);

            }
        });
    }

    private void uploadDetails(DetailsModel dm){

        String target  = Build.DEVICE +Build.MODEL;

            try{
                databaseReference.child(target).child(dm.getImageName().replace('.',' ')).setValue(dm);
            }catch (Exception e){
                e.printStackTrace();
                databaseReference.child(target).child(dm.getImageName().replace('.',' ')).setValue(dm);
            }
    }
}
