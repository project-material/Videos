package com.projectmaterial.videos.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.projectmaterial.videos.app.VideoApplication;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.worker.VideoFetchWorker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollectionViewModel extends AndroidViewModel {

    private final VideoApplication videoApplication;

    public CollectionViewModel(@NonNull Application application) {
        super(application);
        this.videoApplication = (VideoApplication) application;
    }

    /**
     * Retrieves a list of videos that belong to the specified collection.
     *
     * @param collection the collection path.
     * @param videoList the list of videos to filter from.
     * @return a list of videos that belong to the specified collection.
     */
    private List<Video> retrieveCollectionVideos(@NonNull String collection, @NonNull List<Video> videoList) {
        List<Video> collectionVideoList = new ArrayList<>();
        for (Video video : videoList) {
            String videoData = video.getData();
            if (videoData != null) {
                File videoFile = new File(videoData);
                File videoCollection = videoFile.getParentFile();
                if (videoCollection != null && collection.equals(videoCollection.getAbsolutePath())) {
                    collectionVideoList.add(video);
                }
            }
        }
        return collectionVideoList;
    }

    /**
     * Retrieves a LiveData containing a list of videos that belong to the specified collection.
     *
     * @param collection the collection path.
     * @return LiveData containing the list of videos in the collection.
     */
    public LiveData<List<Video>> getCollectionsLiveData(@NonNull String collection) {
        return Transformations.map(VideoFetchWorker.getVideosLiveData(), videoList -> {
            if (videoList == null || collection == null) {
                return new ArrayList<>();
            }
            return retrieveCollectionVideos(collection, videoList);
        });
    }
}



/* package com.projectmaterial.videos.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.projectmaterial.videos.app.VideoApplication;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.worker.VideoFetchWorker;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollectionViewModel extends AndroidViewModel {
   
    private VideoApplication mVideoApplication;
    
    public CollectionViewModel(@NonNull Application application) {
        super(application);
        mVideoApplication = (VideoApplication) application;
    }
    
    public List<Video> retrieveCollectionVideos(@NonNull String collection, @NonNull List<Video> videoList) {
        if (collection == null || videoList == null) {
            return new ArrayList<>();
        }
        
        List<Video> collectionVideoList = new ArrayList<>();
        for (Video video : videoList) {
            String videoData = video.getData();
            if (videoData != null) {
                File videoFile = new File(videoData);
                File videoCollection = videoFile.getParentFile();
                
                if (videoCollection != null && videoCollection.getAbsolutePath().equals(collection)) {
                    collectionVideoList.add(video);
                }
            }
        }
        
        return collectionVideoList;
    }
    
    public LiveData<List<Video>> retrieveCollectionVideosLiveData(@NonNull String collection) {
        MutableLiveData<List<Video>> videosLiveData = new MutableLiveData<>();
        
        if (collection != null) {
            VideoFetchWorker.getVideosLiveData().observeForever(
                (videoList) -> {
                    if (videoList != null) {
                        List<Video> collectionVideoList = retrieveCollectionVideos(collection, videoList);
                        videosLiveData.postValue(collectionVideoList);
                    }
                }
            );
        }
        
        return videosLiveData;
    }
    
}
*/