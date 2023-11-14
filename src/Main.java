import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

//Java Features We Love. [Moved by Java]
// https://www.youtube.com/watch?v=oNiOUiAS70w

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

        GenericFunctions.demo();
        Printer.demo();
        Optionals.demo();
        Streams.demo();
        Strings.demo();
        Switch.demo();
    }
}

class Switch {

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public static void demo() {
        System.out.println("--- Switch ---");
        var b = switch (4) {
            //noinspection DataFlowIssue
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
        System.out.println("-- Strings ---");
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
        System.out.println("-- Generic Functions ---");
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
        System.out.println("-- Generic Printer ---");
        (new Printer<>(1)).print();
        final Printer<String> p = new Printer<>("hello?");
        p.print();
        var p2 = new Printer<>("via var");
        p2.print();
    }
}

class Streams {

    public static void demo() {
        System.out.println("-- Streams ---");
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

class Optionals {

    public static void demo() {
        System.out.println("-- Optionals ---");
        Optional<Boolean> filled = is5OrNot(5);
        Optional<Boolean> nope = is5OrNot(4);
        System.out.println(filled); // Optional[true]
        System.out.println(nope); // Optional.empty

        System.out.println(filled.isEmpty()); // false
        System.out.println(nope.isPresent()); // false

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