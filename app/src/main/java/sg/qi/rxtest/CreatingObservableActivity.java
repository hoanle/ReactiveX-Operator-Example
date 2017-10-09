package sg.qi.rxtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

public class CreatingObservableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createObservable();
    }

    /*
        http://reactivex.io/documentation/operators/create.html
        Source: 100
        Output: 100
     */
    private void createObservable() {
        Observable<Integer> createObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(100);
            }
        });
        createObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
        http://reactivex.io/documentation/operators/defer.html
        Source: 100
        Output: 100
     */
    private void deferObservable() {
        Observable<Integer> deferObservable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(100);
            }
        });

        deferObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
        http://reactivex.io/documentation/operators/empty-never-throw.html
        Source: Observable but defined empty/never
        Output: Nothing
     */
    private void emptyNeverObservable() {
        Observable<Integer> emptyObservable = Observable.empty();
        emptyObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
        Observable<Integer> neverObservable = Observable.never();
        neverObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
        http://reactivex.io/documentation/operators/from.html
        Source: Array of Integer or ArrayList of Integer  or Map
        Output: members of array/list/map
     */

    private void fromObservable() {
        //From Array
        Observable<Integer> arrayObservable = Observable.fromArray(new Integer[] {1, 2, 3, 4, 5, 6});

        arrayObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });

        //From List
        List<Integer> integerList = Arrays.asList(new Integer[] {1, 2, 3, 4, 5, 6});
        Observable<Integer> listObservable = Observable.fromIterable(integerList);
        listObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });

        //From Map
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "Zero");
        map.put(1, "One");
        map.put(2, "Two");
        Observable<Map.Entry<Integer, String>> mapObservable = Observable.fromIterable(map.entrySet());
        mapObservable.subscribe(new Consumer<Map.Entry<Integer, String>>() {
            @Override
            public void accept(Map.Entry<Integer, String> integerStringEntry) throws Exception {
                Log.d("QILog", "Output: " + integerStringEntry.getKey() + "::" + integerStringEntry.getValue());
            }
        });
    }

    /*
        http://reactivex.io/documentation/operators/interval.html
        Source:
        Output: Increasing Long value every interval second
     */
    private void intervalObservable(){
        Observable<Long> intervalObservable = Observable.interval(1, TimeUnit.SECONDS);
        intervalObservable.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d("QILog", "Output: " + System.currentTimeMillis());
                Log.d("QILog", "Output: " + aLong);
            }
        });
    }

    /*
        http://reactivex.io/documentation/operators/interval.html
        Source: some Integer
        Output: each integer (in turn)
     */
    private void justObservable() {
        Observable<Integer> justObservable = Observable.just(1, 2, 3, 4, 5, 6);
        justObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }
    /*
        http://reactivex.io/documentation/operators/range.html
        Source: start(2) and count integer (3)
        Output: 2 3 4
     */
    private void rangeObservable() {
        Observable<Integer> rangeObservable = Observable.range(2, 3);
        rangeObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
        http://reactivex.io/documentation/operators/repeat.html
        Source: one Integer 1
        Output: five object 1
     */
    private void repeatObservable() {
        Observable<Integer> repeatObservable = Observable.just(1);
        repeatObservable.repeat(5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }
    /*
        http://reactivex.io/documentation/operators/start.html
        Source: Observable of items 3 2 3 4, and Observable that emit only 12
        Output: Array Integer first, then items later (12 --> 5 2 3 4)
     */

    private void startObservable() {
        Observable<Integer> startObservable = Observable.just(3, 2, 3, 4);
        startObservable.startWith(new Observable<Integer>() {
            @Override
            protected void subscribeActual(Observer<? super Integer> observer) {
                observer.onNext(12);
                observer.onComplete();
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
        http://reactivex.io/documentation/operators/timer.html
        Source: an Observable, time of delay 4 seconds, count of repea 5
        Output: Every 4 seconds emit 1 time, repeat for 5 times
     */
    private void timerObservable() {
        Observable<Long> timerObservable = Observable.timer(4, TimeUnit.SECONDS).repeat(5);
        timerObservable.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d("QILog", "Output: " + System.currentTimeMillis());
                Log.d("QILog", "Output: "+ aLong);
            }
        });
    }


}
