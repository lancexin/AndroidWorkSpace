package com.hostxin.android.pendingtask;

import android.location.LocationManager;

/**
 * Created by lixin on 17-5-3.
 */

public class GetLocationRequest extends AbstractPendingRequest<GetLocationResult>{


    public GetLocationRequest(PendingTaskManager pendingTaskManager,String id){
        super(pendingTaskManager,id);
    }


    @Override
    public GetLocationResult create(Status status) {
        return new GetLocationResult(status);
    }

    @Override
    public void doRequest() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Location location = new Location();
        location.la = "100";
        location.lo = "101";
        setResult(new GetLocationResult(Status.STATUS_SUCCESS,location));
    }
}
