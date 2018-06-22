package com.aramis.library.aramis;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * ArBus
 * Created by Aramis on 2017/5/17.
 */

public class ArBus {
    private ArBus() {
    }

    private static volatile ArBus mInstance;

    public static ArBus getDefault() {
        if (mInstance == null) {
            synchronized (ArBus.class) {
                if (mInstance == null) {
                    mInstance = new ArBus();
                }
            }
        }
        return mInstance;
    }

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public void post(Object event) {
        bus.onNext(event);
    }

    public <T> Observable<T> take(final Class<T> eventType) {
        return bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return eventType.isInstance(o);
            }
        }).cast(eventType);
    }
}
