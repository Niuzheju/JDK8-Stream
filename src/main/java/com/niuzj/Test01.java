package com.niuzj;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//https://blog.csdn.net/chenhao_c_h/article/details/80691284
//https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/

/**
 * 1.Stream不是集合元素, 它不是数据结构并不保存数据，它是有关算法和计算的
 * 2.Stream可以并行化操作, 基于Java7 中引入的 Fork/Join 框架
 * 3.stream只是封装了处理数据的操作,并不会立刻执行操作,只有遇到terminal 操作时才会执行所有的操作
 * 转换操作,惰性执行,有如下方法:
 * map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered
 * terminal操作:
 * forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator
 */
public class Test01 {

    /**
     * 流的获取
     */
    @Test
    public void test01() {
        String[] arr = {"a", "b", "c"};
        Stream<String> stream = Arrays.stream(arr);
        List<String> list = Arrays.asList("a", "b", "c");
        Stream<String> stream1 = list.stream();
        Stream<String> stream2 = Stream.of("a", "b", "c");
        System.out.println(stream);
        System.out.println(stream1);
        System.out.println(stream2);
    }

    /**
     * 遍历集合中的元素
     */
    @Test
    public void test02() {
        String[] arr = {"a", "b", "c"};
        Stream<String> stream = Arrays.stream(arr);
        stream.forEach((t) -> System.out.println(t));
    }

    /**
     * sorted排序
     */
    @Test
    public void test03() {
        String[] arr = {"a", "b", "c"};
        Stream<String> stream = Arrays.stream(arr);
        stream.sorted((t1, t2) -> {

            if (t1 != null && t2 != null) {
                if (t1.hashCode() > t2.hashCode()) {
                    return -1;
                } else if (t1.hashCode() < t2.hashCode()) {
                    return 1;
                }
            }
            return 0;

        }).forEach((t) -> System.out.println(t));
    }

    /**
     * 过滤元素,只留下符合条件的
     * 如下所示,只留下不等于a的字符
     */
    @Test
    public void test04() {
        String[] arr = {"a", "b", "c"};
        Stream<String> stream = Arrays.stream(arr);
        stream.filter((t) -> {
            return !"a".equalsIgnoreCase(t);
        }).forEach((t) -> System.out.println(t));
    }

    /**
     * 截断
     * 只输出前n个元素
     * limit和skip操作出现时,假如前面还有其他操作,会优先执行limit和skip操作再进行其他操作
     * 但是遇到sorted操作时没有效果,不过可以先进行其他操作,再sorted
     * 对一个 parallel 的 Steam 管道来说，如果其元素是有序的，那么 limit 操作的成本会比较大，因为它的返回对象必须是前 n 个也有一样次序的元素。取而代之的策略是取消元素间的次序，或者不要用 parallel Stream
     */
    @Test
    public void test06() {
        String[] arr = {"a", "b", "c"};
        Stream<String> stream = Arrays.stream(arr);
        stream.map((t) -> {
            System.out.println(111111111111111L);
            return t.toUpperCase();
        }).limit(2).forEach((t) -> System.out.println(t));
    }

    @Test
    public void test18() {
        String[] arr = {"a", "b", "c"};
        Stream<String> stream = Arrays.stream(arr);
        List<String> list = stream.limit(2).sorted((t1, t2) -> {
            System.out.println(11111);
            return t1.compareTo(t2);
        }).collect(Collectors.toList());
        System.out.println(list);
    }

    /**
     * 跳过
     * 跳过前n个元素
     * limit和skip操作出现时会优先执行limit和skip操作再进行其他操作
     * 但是遇到sorted操作时没有效果
     */
    @Test
    public void test07() {
        String[] arr = {"a", "b", "c", "d"};
        Stream<String> stream = Arrays.stream(arr);
        stream.sorted((t1, t2) -> {
            System.out.println(11111);
            return t1.compareTo(t2);
        }).skip(2).forEach(System.out::println);
    }

    /**
     * 去重
     */
    @Test
    public void test08() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("c");
        Stream<String> stream = list.stream();
        stream.distinct().forEach((t) -> System.out.println(t));
    }

    /**
     * 对数据的统计,用于数值类型
     */
    @Test
    public void test09() {
        Stream<Integer> stream = Stream.of(12, 10, 30, 66);
        IntSummaryStatistics intSummaryStatistics = stream.mapToInt((t) -> {
            return t;
        }).summaryStatistics();
        System.out.println("平均数" + intSummaryStatistics.getAverage());
        System.out.println("数据个数" + intSummaryStatistics.getCount());
        System.out.println("最大值" + intSummaryStatistics.getMax());
        System.out.println("最小值" + intSummaryStatistics.getMin());
        System.out.println("求和" + intSummaryStatistics.getSum());
    }

    /**
     * 传入一个Function,这个Function会应用到集合中的每一个元素上
     */
    @Test
    public void test10() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("c");
        Stream<String> stream = list.stream();
        stream.map((t) -> {
            return t.toString().toUpperCase();
        }).forEach((t) -> System.out.println(t));

        System.out.println(list);
    }

    /**
     * flatMap:流中的元素为集合时, flatMap可以把每一个集合的元素取出来放到一起并存放一个新的流中
     * collect:把流中的元素转换为集合
     */
    @Test
    public void test11() {
        Stream<List<Integer>> listStream = Stream.of(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        Stream<Integer> integerStream = listStream.flatMap((mapper) -> {
            return mapper.stream();
        });
        List<Integer> list = integerStream.collect(Collectors.toList());
        System.out.println(list.size());
    }

    /**
     * findFirst查找到第一个元素,返回的是Optional对象,通过Optional对象的get方法获取该元素
     */
    @Test
    public void test12() {
        String[] arr = {"a", "b", "c"};
        Stream<String> stream = Arrays.stream(arr);
        String s = stream.findFirst().get();
        System.out.println(s);
    }

    //数值类型的流, 一共有3种IntStream、LongStream、DoubleStream
    @Test
    public void test13() {
        //使用可变参数初始化
        IntStream.of(1, 2, 3).forEach(System.out::println);
        System.out.println("...");
        //把1到9作为初始化数据
        IntStream.range(1, 10).forEach(System.out::println);
        System.out.println("...");
        //把1到10作为初始化数据
        IntStream.rangeClosed(1, 10).forEach(System.out::println);
    }

    //流转换为其他数据结构
    @Test
    public void test14() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 5);
//        转换为数组,此时IntFunction的apply方法参数为流中元素的长度
//        Integer[] array = stream.toArray(size -> new Integer[size]);
//        System.out.println(Arrays.toString(array));
        //转换为list
//        List<Integer> list = stream.collect(Collectors.toList());
//        System.out.println(list);
        //转换为set
//        Set<Integer> set = stream.collect(Collectors.toSet());
//        System.out.println(set);
        //转换为其他集合
        Stack<Integer> stack = stream.collect(Collectors.toCollection(Stack::new));
        System.out.println(stack);
    }

    /**
     * peek, intermediate操作可以多次对元素遍历并返回新的流
     * IntStream 为并行流
     */
    @Test
    public void test15() {
        IntStream stream = Arrays.stream(new int[]{89, 10, 38});
        int sum = stream.peek((t) -> System.out.println(t + "a")).filter((t) -> t > 10).peek(System.out::println).sum();
        System.out.println(sum);
    }

    /**
     * Optional
     * ifPresent方法,值非空时才会执行传入的函数
     */
    @Test
    public void test16() {
        Optional.ofNullable("").ifPresent(System.out::println);
    }

    /**
     * reduce组合元素
     * 进行字符串拼接,数字比较大小,求和等操作
     */
    @Test
    public void test17() {
        String s = Stream.of("A", "B", "C", "D").filter((t) -> t.compareTo("Z") > 0).reduce("", (t, u) -> {
            return t.concat(u);
        });
        System.out.println(s);
        Double d = Stream.of(20.0, 12.2, 4.3).reduce(Double.MAX_VALUE, (t, u) -> {
            return Double.min(t, u);
        });
        System.out.println(d);
        //有初始值
        Integer sum = Stream.of(12, 20, 30).reduce(0, (t, u) -> {
            return t + u;
        });
        System.out.println(sum);
        //无初始值,返回Optional
        Integer sum1 = Stream.of(12, 20, 30).reduce((t, u) -> {
            return t + u;
        }).get();
        System.out.println(sum1);
    }

    /**
     * 输入流转换为Stream
     *
     * @throws FileNotFoundException
     */
    @Test
    public void test19() throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader("E:\\home\\logs\\admin\\default.log"));
        Stream<String> lines = reader.lines();
        int i = lines.mapToInt((t) -> t.length()).max().getAsInt();
        System.out.println("文件中最长的一行的长度为" + i);
    }

    /**
     * match
     * allMatch 所有元素都满足条件才返回true
     * anyMatch 只有一个满足条件就返回true
     * noneMatch 所有元素都不满足条件才true
     */
    @Test
    public void test20() {
        Stream<Integer> stream = Stream.of(20, 50, 10, 50, 33);
        boolean b;
//        b = stream.allMatch((t) -> t > 10);
//        b = stream.anyMatch((t) -> t == 20);
        b = stream.noneMatch((t) -> t == 100);
        System.out.println(b);
    }

    /**
     * 自己生成流
     * Supplier函数生成流的元素,生成的是无限流,流的大小需要使用limit限制, 否则流中的数据永远遍历不完
     */
    @Test
    public void test21() {
        Random random = new Random();
        Stream.generate(() -> random.nextInt()).limit(10).forEach((t) -> System.out.println(t));
    }

    /**
     * 自定义Supplier实现
     */
    @Test
    public void test22() {
        class MySupplier implements Supplier<String> {
            private int index;

            private Random random = new Random();

            @Override
            public String get() {
                return random.nextInt() + "_" + (index++);
            }
        }
        Stream.generate(new MySupplier()).limit(10).forEach((t) -> System.out.println(t));
    }

    /**
     * iterate
     * 给定一个种子,然后以特定方式生成元素,也是无限流
     */
    @Test
    public void test23() {
        Stream.iterate(1, (t) -> t * 3).limit(10).forEach(System.out::println);
    }

    /**
     * 分组,根据传入的函数返回的值进行分组
     */
    @Test
    public void test24() {
        Map<Integer, List<String>> map = Stream.of("as", "df", "asa", "112", "112").collect(Collectors.groupingBy((t) -> t.hashCode()));
        System.out.println(map);
    }

    /**
     * 根据传入函数所指定的条件把元素分为两个部分
     */
    @Test
    public void test25(){
        Map<Boolean, List<String>> map = Stream.of("as", "df", "asa", "112", "112").collect(Collectors.partitioningBy((t) -> t.length() > 2));
        System.out.println(map);
    }

    /**
     * Stream特性
     * 不是数据结构
     * 它没有内部存储，它只是用操作管道从 source（数据结构、数组、generator function、IO channel）抓取数据。
     * 它也绝不修改自己所封装的底层数据结构的数据。例如 Stream 的 filter 操作会产生一个不包含被过滤元素的新 Stream，而不是从 source 删除那些元素。
     * 所有 Stream 的操作必须以 lambda 表达式为参数
     * 不支持索引访问
     * 你可以请求第一个元素，但无法请求第二个，第三个，或最后一个。不过请参阅下一项。
     * 很容易生成数组或者 List
     * 惰性化
     * 很多 Stream 操作是向后延迟的，一直到它弄清楚了最后需要多少数据才会开始。
     * Intermediate 操作永远是惰性化的。
     * 并行能力
     * 当一个 Stream 是并行化的，就不需要再写多线程代码，所有对它的操作会自动并行进行的。
     * 可以是无限的
     * 集合有固定大小，Stream 则不必。limit(n) 和 findFirst() 这类的 short-circuiting 操作可以对无限的 Stream 进行运算并很快完成。
     */


}
