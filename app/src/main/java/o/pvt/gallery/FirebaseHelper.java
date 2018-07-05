package o.pvt.gallery;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;

/**
 * Created by Saad Abdul Qadir on 6/22/2018.
 */

public class FirebaseHelper  {
    StorageReference storageReference;

    public FirebaseHelper(){
        this.storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void uploadImage(Uri uri, String name){
        storageReference.child(name).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                Log.d("DownloadURl",downloadUri.toString());

            }
        });
    }
}
