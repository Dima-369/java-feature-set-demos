import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {

    @SuppressWarnings("CommentedOutCode")
    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>();
        a.add(3);
        System.out.println(a); // [3]

        //        avoid manual cast
        //        if (person instanceof Employee emp) {
        //            if (emp.isBasedInOffice()) {
        //                emp.workFromHome();
        //            }
        //        }

        FunctionPassing.demo();
        GenericFunctions.demo();
        Printer.demo();
        Optionals.demo();
        Streams.demo();
        Strings.demo();
        Switch.demo();
        Annotations.demo();
    }
}

@MyAnnotation
class Annotations {

    @SuppressWarnings("unused")
    @MyFieldAnnotation
    int foo = 3;

    public static void demo() {
        System.out.println("--- Annotations ---");

        Annotations a = new Annotations();
        if (a.getClass().isAnnotationPresent(MyAnnotation.class)) {
            System.out.println("Annotations is annotated!");
        }

        for (Method method : Annotations.class.getMethods()) {
            if (method.isAnnotationPresent(RunMe.class)) {
                try {
                    method.invoke(a);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Method method : Annotations.class.getMethods()) {
            if (method.isAnnotationPresent(RunMeMultiple.class)) {
                try {
                    RunMeMultiple annotation = method.getAnnotation(RunMeMultiple.class);
                    for (int i = 0; i < annotation.times(); i++) {
                        method.invoke(a);
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Field field : Annotations.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(MyFieldAnnotation.class)) {
                Object o;
                try {
                    o = field.get(a);
                    if (o instanceof Integer i) {
                        System.out.println(i + 3); // 6
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @RunMe
    public void func() {
        System.out.println("hi from func()");
    }

    @RunMeMultiple(times = 3)
    public void func2() {
        System.out.println("func2() says hi");
    }
}

// target only classes and methods
@Target({ElementType.TYPE, ElementType.METHOD})
// keep annotation around throughout the entire run time
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {

}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface RunMe {

}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface RunMeMultiple {
    // make parameter mandatory
    int times();
    // int times() default 1;
    // pass array
    // int[] times();
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface MyFieldAnnotation {

}

class FunctionPassing {

    public static void demo() {
        System.out.println("--- Function Passing ---");
        // note that this only works when there is one function implementation missing
        doPrint((s) -> System.out.println("wow" + s));

        // the same as above
        Printable foo = (s) -> System.out.println("wow" + s);
        doPrint(foo);
    }

    public static void doPrint(Printable x) {
        x.print("!");
    }
}

// SAM (single abstract method) interfaces
@FunctionalInterface
interface Printable {
    void print(String suffix);
}

class Switch {

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public static void demo() {
        System.out.println("--- Switch ---");
        var b = switch (4) {
            // noinspection DataFlowIssue
            case 4 -> "I am 4";
//            case 1 -> 4;
//            default -> {
//                if (in instanceof String) {
//                    yield 5;
//                }
//                yield 3;
            default -> throw new IllegalStateException("Unexpected value: " + 4);
        };
        System.out.println(b); // I am 4
    }
}

class Strings {

    public static void demo() {
        System.out.println("--- Strings ---");
        String big = "'" + """
                I am a multi line string. Note that I have a newline at the end!
                 """ + "'";
        System.out.println(big);
    }
}

class GenericFunctions {

    @SuppressWarnings("SameParameterValue")
    private static <T, V> void shout(T x, V y) {
        System.out.printf("%s, %s\n", x, y);
    }

    // wildcard, call List with whatever you want
    private static void printList(List<?> list) {
        System.out.println(list);
    }

    // restrict wildcard to Number classes
    private static void printNumberList(List<? extends Number> list) {
        System.out.println(list);
    }

    public static void demo() {
        System.out.println("--- Generic Functions ---");
        shout(1, 2); // 1, 2
        printList(List.of(1, 2, 3)); // [1, 2, 3]
        printNumberList(List.of(1, 2)); // [1, 2]
        // printNumberList(List.of("does not compile!"));
    }
}

// https://www.youtube.com/watch?v=K1iu1kXkVoA
// bounded generic:
// public class Printer<T extends Animal> {
// public class Printer<T extends Animal & Serializable> {
// only one class, but multiple interfaces are allowed
class Printer<T> {
    T x;

    public Printer(T thing) {
        this.x = thing;
    }

    public void print() {
        System.out.println(x);
    }

    public static void demo() {
        System.out.println("--- Generic Printer ---");
        (new Printer<>(1)).print();
        final Printer<String> p = new Printer<>("hello?");
        p.print();
        var p2 = new Printer<>("via var");
        p2.print();
    }
}

class Streams {

    public static void demo() {
        System.out.println("--- Streams ---");
        var b = Stream.of(1, 2, 3).map(x -> x + 1).toList();
        System.out.println(b); // [2, 3, 4]

        List<String> foo = List.of("3", "2", "1");

        System.out.println(foo.stream().max(String::compareTo).get()); // ["1"]
        System.out.println(foo.stream().skip(2).toList()); // ["1"]
        System.out.println(foo.stream().limit(2).toList()); // ["3", "2"]
        System.out.println(foo.stream().filter(s -> s.equals("1")).toList()); // ["1"]
        System.out.println(foo.stream().sorted(Comparator.reverseOrder()).toList()); // ["3", "2", "1"]
    }
}

/*
The class below is the same as this record. The fields will be final and private.
It creates equals(), toString(), hashCode() and public getters like age() - not getAge()
There will not be an implicit constructor without any arguments.
record Cat(int age) {
    // you can add regular and static methods here

    // there can be a compact constructor without the ()
    public Cat {
        if (age < 0) {
            // throw Exception
        }
    }
}

//
Create exactly the same as a class via:
Cat cat = new Cat(3);
var age = cat.age();
*/

@SuppressWarnings("ClassCanBeRecord")
class Cat {

    private final int age;

    Cat(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}

class Optionals {

    @SuppressWarnings("CommentedOutCode")
    public static void demo() {
        System.out.println("--- Optionals ---");

        // the main use is for returning things from a function, not inside it to avoid null pointers
        // do not use .get() - use .orElseThrow() instead

        Optional<Boolean> filled = is5OrNot(5);
        Optional<Boolean> nope = is5OrNot(4);
        System.out.println(filled); // Optional[true]
        System.out.println(nope); // Optional.empty

        System.out.println(filled.isEmpty()); // false
        System.out.println(nope.isPresent()); // false

        System.out.println(nope.orElse(true)); // true
        // System.out.println(nope.orElseGet(() -> true));
        // System.out.println(nope.orElseThrow());

        System.out.println(Optional.of(new Cat(3)).map(Cat::getAge)); // Optional[3]
        System.out.println(Optional.of(new Cat(3)).map(Cat::getAge).orElseThrow()); // 3

        Optional.of(300).ifPresent(System.out::println); // 300
        Optional.of(500).ifPresent((x) -> System.out.println(x + 10)); // 510
        Optional.of(700).ifPresent((x) -> {
            x += 10;
            System.out.println(x + 10);
        }); // 720

        System.out.println(filled.orElse(false)); // true
        System.out.println(nope.orElse(true)); // true
    }

    public static Optional<Boolean> is5OrNot(int x) {
        if (x == 5) {
            return Optional.of(true);
        }
        return Optional.empty();
    }
}
