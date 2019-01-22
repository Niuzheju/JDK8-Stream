package com.niuzj;

import org.junit.Test;

import java.util.*;
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
     * limit和skip操作出现时会优先执行limit和skip操作再进行其他操作
     * 但是遇到sorted操作时没有效果
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

}
