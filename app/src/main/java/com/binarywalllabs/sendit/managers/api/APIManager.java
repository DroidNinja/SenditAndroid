package com.binarywalllabs.sendit.managers.api;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by Arun on 18-10-2015.
 */
public class APIManager {
    private static APIManager ourInstance = new APIManager();
    private RestService restService;
    private Scheduler defaultSubscribeScheduler;

    public static APIManager getInstance() {
        return ourInstance;
    }

    public APIManager()
    {
        restService = RestService.Factory.create();
    }

    public RestService getRestService() {
        if (restService == null) {
            restService = RestService.Factory.create();
        }
        return restService;
    }

    //For setting mocks during testing
    public void setRestService(RestService restService) {
        this.restService = restService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }

    //User to change scheduler from tests
    public void setDefaultSubscribeScheduler(Scheduler scheduler) {
        this.defaultSubscribeScheduler = scheduler;
    }
}
