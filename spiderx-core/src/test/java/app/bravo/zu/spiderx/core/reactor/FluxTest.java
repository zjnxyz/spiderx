package app.bravo.zu.spiderx.core.reactor;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class FluxTest {

    public static void main(String[] args) {

        test5();
    }


    private static void test1() {
        Flux.range(1, 10000)
                .publishOn(Schedulers.parallel()).subscribe(System.out::println);
    }

    private static void test2() {
        Flux.just(1, 2, 3).map(t -> {
            if (t == 2){
                throw new RuntimeException("got 2");
            }
            return t;
        }).doOnError(Throwable::printStackTrace).onErrorResume(err -> Mono.just(resErr(err))).subscribe(System.out::println);
    }

    private static void test3() {
        Hooks.onOperatorDebug();

        Flux.interval(Duration.ofMillis(250)).map(t -> {
            if (t < 3) return "tick " + t;
            throw new RuntimeException("boom");
        }).elapsed().retry(1).subscribe(System.out::println, System.err::println);

        try {
            Thread.sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Flux.<String>error(new IllegalArgumentException())
                .retryWhen(companion -> companion.take(3)).subscribe(System.out::println, System.err::println);
    }

    private static void test4() {
        AtomicInteger ai = new AtomicInteger();
        Function<Flux<String>, Flux<String>> filterAndMap = f -> {
            if (ai.incrementAndGet() == 1) {
                return f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);
            }
            return f.filter(color -> !color.equals("purple"))
                    .map(String::toUpperCase);
        };

        Flux<String> composedFlux =
                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                        .doOnNext(System.out::println)
                        .transform(filterAndMap);

        composedFlux.subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :"+d));
        composedFlux.subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: "+d));
    }

    private static void test5() {
        Flux<Integer> source = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("subscribed to source"));

        ConnectableFlux<Integer> co = source.publish();

        co.subscribe(System.out::println, e -> {}, () -> {});
        co.subscribe(System.out::println, e -> {}, () -> {});

        System.out.println("done subscribing");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("will now connect");

        co.connect();
    }


    private static Integer resErr(Throwable err) {
        err.printStackTrace();
        return 0;
    }
}
