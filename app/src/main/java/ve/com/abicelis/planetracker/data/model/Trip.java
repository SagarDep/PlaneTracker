package ve.com.abicelis.planetracker.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.List;

import ve.com.abicelis.planetracker.util.CalendarUtil;

/**
 * Created by abicelis on 26/8/2017.
 */

@Entity(tableName = "trip")
public class Trip implements Comparable<Trip>{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trip_id")
    private long mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "image")
    private byte[] mImage;

    @Ignore
    private TripStatus mStatus;

    @Ignore
    private List<Flight> mFlights;

    public Trip() {}    //Room complains otherwise, I wish I knew why.

    public Trip(long id, String name, byte[] image, TripStatus status, List<Flight> flights) {
        mId = id;
        mName = name;
        mImage = image;
        mStatus = status;
        mFlights = flights;
    }

    public long getId() {return mId;}
    public String getName() {return mName;}
    public byte[] getImage() {return mImage;}
    public TripStatus getStatus() {
        if(mFlights.size() == 0)
            return TripStatus.UPCOMING;

        //TODO check if this comparison takes Timestamps into account
        Calendar lastArrival = mFlights.get(mFlights.size()-1).getArrival();
        if(lastArrival.compareTo(Calendar.getInstance()) >= 0)
            return TripStatus.UPCOMING;
        else
            return TripStatus.PAST;
    }
    public List<Flight> getFlights() {return mFlights;}

    public void setId(long mId) {this.mId = mId;}
    public void setName(String mName) {this.mName = mName;}
    public void setImage(byte[] mImage) {this.mImage = mImage;}
    public void setStatus(TripStatus mStatus) {this.mStatus = mStatus;}
    public void setFlights(List<Flight> mFlights) {this.mFlights = mFlights;}

    @Override
    public String toString() {
        String out =    "Trip ID="      + mId
                + "\n   name="          + (mName != null ? mName : "NULL")
                + "\n   image="         + (mImage != null  ? "Lenght:" + mImage.length : "NULL")
                + "\n   flights=";

        for (Flight f : mFlights)
            out += "\n   Flight="  + f.toString();

        return out;
    }

    @Override
    public int compareTo(@NonNull Trip trip) {
        if (this.mFlights.size() == 0)
            return 1;
        if (trip.mFlights.size() == 0)
            return -1;
        return this.mFlights.get(0).compareTo(trip.mFlights.get(0));
    }
}
