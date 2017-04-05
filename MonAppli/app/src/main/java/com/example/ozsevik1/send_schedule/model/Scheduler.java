package com.example.ozsevik1.send_schedule.model;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;

/**
 * Created by ozcan on 24/03/2017.
 */

public interface Scheduler {

    public void schedule(FirebaseJobDispatcher dispatcher);
}
