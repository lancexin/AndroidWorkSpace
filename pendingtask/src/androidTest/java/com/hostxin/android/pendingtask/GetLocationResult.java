package com.hostxin.android.pendingtask;

/**
 * Created by lixin on 17-5-3.
 */

public class GetLocationResult implements Result {

    private Status mStatus;
    private Location mLocation;
    public GetLocationResult(Status status,Location location){

        this.mStatus = status;
        this.mLocation = location;
    }

    public GetLocationResult(Status status){
        this.mStatus = status;
    }

    @Override
    public Status getStatus() {
        return mStatus;
    }

    public Location getLocation() {
        return mLocation;
    }
}
