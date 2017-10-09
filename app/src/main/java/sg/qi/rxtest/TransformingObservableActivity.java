package sg.qi.rxtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hoan on 3/10/17.
 */

public class TransformingObservableActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        windowObservable();
    }
    /*
        http://reactivex.io/documentation/operators/buffer.html
        Source: 10 items of Integer (from 1 to 10), buffer size 2
        Output: 5 lists, each list contains 2 objects ({1, 2}, {3, 4}, {5, 6}, {7, 8}, {9, 10})
     */
    private void bufferObservable() {
        Observable<Integer> bufferObservable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        bufferObservable.buffer(2).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> list) throws Exception {
                Log.d("QILog", "Output: size: " + list.size());
                for (int i = 0; i < list.size(); i++) {
                    Log.d("QILog", "Output: " + list.get(i));
                }
            }
        });
    }

    /*
        http://reactivex.io/documentation/operators/flatmap.html
        Source: Integer from 1 to 8
        Output: After flatMap which transfer the integer to String form "number " + integer (number 1, number 2...)
     */

    private void flatMapObservable() {
        Observable<Integer> flatMapObservable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8);
        flatMapObservable.flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                return Observable.just("number " + integer);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("QILog", "Output: " + s);
            }
        });
    }

    private void groupByObservable(){
        Observable<Integer> groupByObservable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8);
        groupByObservable.groupBy(new Function<Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                if (integer % 2 == 0)
                    return "Even";
                else
                    return "Odd";
            }
        }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                if (stringIntegerGroupedObservable.getKey().equals("Even")) {
                    stringIntegerGroupedObservable.subscribeOn(Schedulers.single()).subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.d("QILog", "Output Even: " + integer);
                        }
                    });
                } else {
                    stringIntegerGroupedObservable.subscribeOn(Schedulers.single()).subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.d("QILog", "Output Odd: " + integer);
                        }
                    });
                }
            }
        });
    }

    /*
        http://reactivex.io/documentation/operators/map.html
        Source: Integer objects
        Output: Integer object after perform function on it (square)
     */

    private void mapObservable() {
        Observable<Integer> mapObservable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8);
        mapObservable.map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer * integer;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
        http://reactivex.io/documentation/operators/scan.html
        Source: Integer objects
        Output: Apply the add function, output will be the total of Integer objects from first position to current position
        takeLast(1): only care about the last emit
     */

    private void scanObservable() {
        Observable<Integer> scanObject = Observable.just(1, 2, 3, 4, 5, 6, 7, 8);
        scanObject.scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                Log.d("QILog", "integer: " + integer);
                Log.d("QILog", "integer2: " + integer2);
                return integer + integer2;
            }
        }).takeLast(1).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
    http://reactivex.io/documentation/operators/window.html
    Source: collection of Integer objects
    Output: observale that will emit set of Integer object (here is 2 integer)
     */

    private void windowObservable(){
        Observable<Integer> windowObservable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9);
        windowObservable.window(2).subscribe(new Consumer<Observable<Integer>>() {
            @Override
            public void accept(Observable<Integer> integerObservable) throws Exception {
                Log.d("QILog", "integerObservable");
                integerObservable.subscribe(new Consumer<Integer>() {
                   @Override
                   public void accept(Integer integer) throws Exception {
                       Log.d("QILog", "Output: " + integer);
                   }
                });
            }
        });
    }
}
