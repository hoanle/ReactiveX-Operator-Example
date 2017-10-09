package sg.qi.rxtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by hoan on 5/10/17.
 */

public class FilteringObservableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        takeLastObservable();
    }
    /*
    http://reactivex.io/documentation/operators/debounce.html
    Source: Collection of Integer object ( 0 --> 19)
    Output: only Integer that emits with interval > debounce time will be accepted
     */
    private void debounceObservable(){
        final long SLEEP_TIME_VALID = 1500;
        final long SLEEP_TIME_INVALID = 500;
        final long DEBOUNCE_TIME = 1000;
        // if SLEEP_TIME < DEBOUNCE_TIME: only last item is accepted

        Observable<Integer> debounceObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                int i = 0;

                do {
                    try {
                        //The interval of emitting is long enough
                        Thread.sleep(SLEEP_TIME_VALID);
                        e.onNext(i++);
                    } catch (Exception exception){
                        exception.printStackTrace();
                        e.onError(exception);
                    }
                } while (i < 10);

                do {
                    try {
                        //The interval of emitting is not long enough, only final item is accepted
                        Thread.sleep(SLEEP_TIME_INVALID);
                        e.onNext(i++);
                    } catch (Exception exception){
                        exception.printStackTrace();
                        e.onError(exception);
                    }
                } while (i < 20);
                e.onComplete();
            }
        });
        debounceObservable.subscribeOn(Schedulers.computation()).debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }
    /*
    http://reactivex.io/documentation/operators/distinct.html
      Source: A collection of Integer objects, some duplicate
      Output: All duplicates are removed
     */

    private void distinctObservable() {
        Observable<Integer> distinctObservable = Observable.just(1,1, 2, 3,3, 4, 5, 6, 6);
        distinctObservable.distinct().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: "+ integer);
            }
        });
    }

    /*
    http://reactivex.io/documentation/operators/elementat.html
    Source: A collection of Integer objects
    Output: Integer at a position
     */

    private void elementAtObservable() {
        Observable<Integer> elementAtObservable = Observable.just(0, 1, 2, 3, 4, 5);
        elementAtObservable.elementAt(4).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: "+ integer);
            }
        });
    }

    /*
    http://reactivex.io/documentation/operators/filter.html
    Source: A collection of Integer objects
    Output: Integer that pass the filter, here is an Odd number filter (1, 3, 5, 5, 7, 11)
    */
    private void filterObservable() {
        Observable<Integer> filterObservable = Observable.just(1, 2, 3, 4, 4, 5, 5, 7, 11);
        filterObservable.filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer % 2 == 1;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: "+ integer);
            }
        });
    }

    /*
    http://reactivex.io/documentation/operators/first.html
    Source: Collection of Integer objects
    Output: first item that meets condition (after applying filter)
     */
    private void firstObservable() {
        Observable<Integer> firstObservable = Observable.just(1, 4, 6, 13, 8, 5, 12);
        firstObservable.filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer > 5;
            }
        }).firstElement().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
    http://reactivex.io/documentation/operators/ignoreelements.html
       Source: collections of Integers
       Output: notify the complete, don't care about the stream value
     */
    private void ignoreObservable() {
        Observable<Integer> ignoreObservable = Observable.just(1, 2, 4, 12, 30, 5, 6);
        ignoreObservable.ignoreElements().doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.d("QILog", "Output " + "ignoreElements completed");
            }
        }).subscribe();
    }

    /*
       http://reactivex.io/documentation/operators/last.html
       Source: collections of Integers
       Output: The last integer that satisfies the condition of the filter (filter is optional)
     */

    private void lastObservable() {
        Observable<Integer> lastObservable = Observable.just(1, 2, 4, 12, 30, 5, 6);
        lastObservable.filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer > 10;
            }
        }).lastElement().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output " + integer);
            }
        });
    }

    /*
    http://reactivex.io/documentation/operators/sample.html
    Source: Collection of integer
    Output:
     */
    private void sampleObservable() {
        final long SLEEP_TIME = 500;
        final long INTERVAL = 2000;

        Observable<Integer> sampleObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                int i = 0;

                do {
                    try {
                        //Integer will be emitted after every SLEEP_TIME seconds
                        Thread.sleep(SLEEP_TIME);
                        e.onNext(i++);
                    } catch (Exception exception){
                        exception.printStackTrace();
                        e.onError(exception);
                    }
                } while (i < 10);

                e.onComplete();
            }
        });
        sampleObservable.subscribeOn(Schedulers.computation())
                .sample(INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        //In INTERVAL second, there might be more than 1 integer imitted, but only the last one is accepted here
                        Log.d("QILog", "Output " + integer);
                    }
                });
    }

     /*
        http://reactivex.io/documentation/operators/skip.html
        Source: Collection of integer
        Output: Skip n first integer and then emit
     */
     private void skipObservable() {
         Observable<Integer> skipObservable = Observable.just(1, 2, 3, 4, 5, 6);
         skipObservable.skip(2)
                 .subscribe(new Consumer<Integer>() {
                     @Override
                     public void accept(Integer integer) throws Exception {
                         Log.d("QILog", "Output " + integer);
                     }
                 });
     }

     /*
     http://reactivex.io/documentation/operators/skiplast.html
     Source: Collection of integer
     Output: Skip n last integer and then emit
     */
     private void skipLastObservable() {
         Observable<Integer> skipLastObservable = Observable.just(1, 2, 3, 4, 5, 6);
         skipLastObservable.skipLast(2)
                 .subscribe(new Consumer<Integer>() {
                     @Override
                     public void accept(Integer integer) throws Exception {
                         Log.d("QILog", "Output " + integer);
                     }
                 });
     }

     /*
     http://reactivex.io/documentation/operators/take.html
     Source: Collection of integers
     Output: Only first n integers will be emitted
      */
     private void takeObservable() {
         Observable<Integer> takeObservable = Observable.just(1, 2, 3, 4, 5, 6, 7);
         takeObservable.take(2)
                 .subscribe(new Consumer<Integer>() {
                     @Override
                     public void accept(Integer integer) throws Exception {
                         Log.d("QILog", "Output " + integer);
                     }
                 });
     }

    /*
     http://reactivex.io/documentation/operators/takelast.html
     Source: Collection of integers
     Output: Only last n integers will be emitted
     */
    private void takeLastObservable() {
        Observable<Integer> takeLastObservable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8);
        takeLastObservable.takeLast(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("QILog", "Output " + integer);
                    }
                });
    }

}
