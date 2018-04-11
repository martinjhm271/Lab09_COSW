package co.edu.escuelaing.laboratoriomapas;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2106913 on 4/11/18.
 */

public class LocationLocal implements Parcelable{
    private String name;
    private String description;
    private double latitude;
    private double longitude;


    public LocationLocal(){}
    public LocationLocal(String name,String description,double latitude,double longitude){
        this.description=description;
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }



    protected LocationLocal(Parcel in) {
        description = in.readString();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<LocationLocal> CREATOR = new Creator<LocationLocal>() {
        @Override
        public LocationLocal createFromParcel(Parcel in) {
            return new LocationLocal(in);
        }

        @Override
        public LocationLocal[] newArray(int size) {
            return new LocationLocal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }


}
