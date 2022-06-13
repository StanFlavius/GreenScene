package com.example.greenscene.Functionalities.PastEventDetails;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.R;
import com.example.greenscene.Repo.ImageRepo;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class PastEventDetailsFragment extends Fragment {
    private String currentEventId;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView startTextView;
    private RecyclerView recyclerView;
    private Button addGalleryButton;
    private Button addCameraButton;
    private Uri imageUri;
    private Bitmap photoTaken;

    private PastEventDetailsViewModel mViewModel;
    private FirebaseAuth fAuth;
    private ImageRepo imageRepo;

    public static PastEventDetailsFragment newInstance() {
        return new PastEventDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.past_event_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PastEventDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null) {
            PastEventDetailsFragmentArgs args = PastEventDetailsFragmentArgs.fromBundle(getArguments());
            this.currentEventId = args.getEventId();
        }

        fAuth = FirebaseAuth.getInstance();
        imageRepo = ImageRepo.getInstance();

        titleTextView = view.findViewById(R.id.pastDetailsTitle);
        descriptionTextView = view.findViewById(R.id.pastDetailsDescription);
        startTextView = view.findViewById(R.id.pastDetailsStart);
        recyclerView = view.findViewById(R.id.pastDetailsGallery);
        addGalleryButton = view.findViewById(R.id.addGaleryButton);
        addCameraButton = view.findViewById(R.id.addCameraButton);

        mViewModel = ViewModelProviders.of(this).get(PastEventDetailsViewModel.class);
        mViewModel.init(currentEventId);

        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        PastEventDetailsAdapter adapter = new PastEventDetailsAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        mViewModel.getCurrentEvent().observe((LifecycleOwner) requireContext(), new Observer<PredictHQResult>() {
            @Override
            public void onChanged(PredictHQResult predictHQResult) {
                Event currentEvent = predictHQResult.getEvents().get(0);
                titleTextView.setText(currentEvent.getTitle());
                if(currentEvent.getDescription().length()>3){
                    descriptionTextView.setText(currentEvent.getDescription());
                } else {
                    descriptionTextView.setText("There is no description available for this event.");
                }

                String rawDate = currentEvent.getStart();
                String formatedDate = rawDate.split("T")[0];
                String formatedHour = rawDate.split("T")[1].substring(0,6);

                startTextView.setText(formatedHour + " / " + formatedDate);

                mViewModel.initImagesURLs(currentEventId);

                mViewModel.getImageURLs().observe((LifecycleOwner) getContext(), new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> resultURLs) {
                        adapter.updateData(resultURLs, new PastEventDetailsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }
                        });
                    }
                });
            }
        });

        addGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        addCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent();
                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imageRepo.uploadImageByUri(imageUri, this.currentEventId, fAuth.getUid(), getFileExtension(imageUri));
            return;
        }

        if(requestCode == 3 && resultCode == Activity.RESULT_OK && data != null) {
            photoTaken = (Bitmap) data.getExtras().get("data");
            imageRepo.uploadImageByBitmap(photoTaken, this.currentEventId, fAuth.getUid(), "jpeg");
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}