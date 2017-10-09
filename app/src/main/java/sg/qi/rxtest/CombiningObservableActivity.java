package sg.qi.rxtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by hoan on 6/10/17.
 */

public class CombiningObservableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        zipObservable();
    }

    /*
        NOT AVAILABLE FOR RXJAVA2
        http://reactivex.io/documentation/operators/and-then-when.html
        Source: Two sets of Integer objects
        Output:
     */

    private void andThenWhenObservable() {

    }

    /*
    http://reactivex.io/documentation/operators/combinelatest.html
    Source: Two set of items
    Output: Merge latest emitted items
     */
    private void combineLatestObservable() {
        Observable<Integer> integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                int i = 0;

                do {
                    try {
                        //Emit integer every 1 second
                        Thread.sleep(1000);
                        e.onNext(i);
                        i++;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        e.onError(exception);
                    }

                } while (i < 5);
                e.onComplete();
            }
        });
        Observable<Character> characterObservable = Observable.create(new ObservableOnSubscribe<Character>() {
            @Override
            public void subscribe(ObservableEmitter<Character> e) throws Exception {
                int i = 0;
                char[] chars = new char[] {'a', 'b', 'c', 'd', 'e'};
                do {
                    try {
                        //Emit character every 1 second
                        Thread.sleep(700);
                        e.onNext(chars[i]);
                        i++;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        e.onError(exception);
                    }

                } while (i < 5);
                e.onComplete();
            }
        });
        Observable.combineLatest(integerObservable.subscribeOn(Schedulers.computation()), characterObservable.subscribeOn(Schedulers.computation()), new BiFunction<Integer, Character, String>() {
            @Override
            public String apply(Integer integer, Character character) throws Exception {
                return integer + character.toString();
            }
        }).subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("QILog", "Output: " + s);
            }
        });
    }

    /*
        NOT AVAILABLE
        http://reactivex.io/documentation/operators/join.html
        Source: Two set of items
        Output:
    */
    private void joinObservable() {

    }

    /*
        http://reactivex.io/documentation/operators/merge.html
        Source: Two set of items
        Output: Merge two set
     */

    private void mergeObservable() {
        Observable<Integer> integerObservable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                int i = 0;

                do {
                    try {
                        //Emit integer every 1 second
                        Thread.sleep(1000);
                        e.onNext(i);
                        i++;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        e.onError(exception);
                    }

                } while (i < 5);
                e.onComplete();
            }
        });
        Observable<Integer> integerObservable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                int i = 0;
                int[] chars = new int[] {10, 12, 23, 34, 55};
                do {
                    try {
                        //Emit character every 2 seconds
                        Thread.sleep(2000);
                        e.onNext(chars[i]);
                        i++;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        e.onError(exception);
                    }

                } while (i < 5);
                e.onComplete();
            }
        });
        Observable.merge(integerObservable1.subscribeOn(Schedulers.computation()), integerObservable2.subscribeOn(Schedulers.computation())).subscribeOn(Schedulers.computation()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
    http://reactivex.io/documentation/operators/startwith.html
    Source: A set of items
    Output: special item is emitted first, then the rest of the set
    */
    private void startWithObservable() {
        Observable<Integer> startWithObservable = Observable.just(1, 2, 3, 4, 5);
        startWithObservable.startWith(-1).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
    http://reactivex.io/documentation/operators/switch.html
    Source:
    Output:
     */
    private void switchObservable() {
        final Observable<Integer> integerObservable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                int i = 0;
                int[] ints = new int[]{1, 2, -1, 4, 5};
                do {
                    try {
                        //Emit integer every 1 second
                        e.onNext(ints[i]);
                        i++;
                        long start = System.currentTimeMillis();
                        while (System.currentTimeMillis() - start < 1000){

                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        e.onError(exception);
                    }

                } while (i < 5);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        final Observable<Integer> integerObservable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                int i = 0;
                int[] chars = new int[]{10, 12, 23, 34, 55};
                do {
                    try {
                        e.onNext(chars[i]);
                        i++;
                        //Emit character every 3 seconds
                        long start = System.currentTimeMillis();
                        while (System.currentTimeMillis() - start < 3000){

                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        e.onError(exception);
                    }

                } while (i < 5);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable<Observable<Integer>> a = Observable.create(new ObservableOnSubscribe<Observable<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<Observable<Integer>> e) throws Exception {
                e.onNext(integerObservable1);
                long start = System.currentTimeMillis();
                //Run the first source for 2 second and then run the second source
                while (System.currentTimeMillis() - start < 2000){

                }
                e.onNext(integerObservable2);
            }
        });
        Observable.switchOnNext(a.subscribeOn(Schedulers.io()), 1).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("QILog", "Output: " + integer);
            }
        });
    }

    /*
    http://reactivex.io/documentation/operators/zip.html
    Source: two sets of integers & String
    Ourput: a set of strings which are from the zip of two set
     */
    private void zipObservable() {
        Observable<Integer> integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                int i = 0;

                do {
                    try {
                        //Emit integer every 1 second
                        Thread.sleep(1000);
                        e.onNext(i);
                        i++;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        e.onError(exception);
                    }

                } while (i < 5);
                e.onComplete();
            }
        });
        Observable<Character> characterObservable = Observable.create(new ObservableOnSubscribe<Character>() {
            @Override
            public void subscribe(ObservableEmitter<Character> e) throws Exception {
                int i = 0;
                //This array has more member than the integar array
                char[] chars = new char[] {'a', 'b', 'c', 'd', 'e' , 'f', 'g'};
                do {
                    try {
                        //Emit character every 1 second
                        Thread.sleep(700);
                        e.onNext(chars[i]);
                        i++;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        e.onError(exception);
                    }

                } while (i < 5);
                e.onComplete();
            }
        });

        Observable.zip(integerObservable, characterObservable, new BiFunction<Integer, Character, String>() {
            @Override
            public String apply(Integer integer, Character character) throws Exception {
                return integer + character.toString();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                //Output will ignore f g since there are no corresponding integer value for them
                Log.d("QILog", "Output: " + s);
            }
        });
    }
}
