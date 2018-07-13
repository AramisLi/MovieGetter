# FilmGetter-learn 大纲

## 第0章 课程介绍+准备工作
1. 课程介绍
2. 编译环境（设备，Android Studio，Pycharm，Android SDK，各种包等）
3. 安装教程（Git，Kotlin，anko，Python，flask等）
## 第1章 Kotlin基础
1. 基本语法
    1. 包声明（package）
    2. 函数声明/定义(fun，返回值，参数名，默认值)
    3. 可变参数（vararg）
    4. 变量声明（var,val）+可空声明（?,?:）
    5. 注释(//,/**/,link,@params,@return)
2. 数据类型
    1. 基本数据类型（Double，Float，Long，Int，Short，Byte）
    2. 字面量（长整型L，16进制0x，2进制0b，double/float，易读下划线）
    3. ==/===/equels
    4. 类型转换(toInt,as)
    5. 字符串（""，"""）
    6. 字符串模板（$）
3. 条件语句
    1. if else
    2. when
    3. in
4. 循环
    1. for in
    2. while + do while
    3. break/continue
    4. @标记
5. 类、对象、接口、枚举以及面向对象
    1. class，abstract class ，inner class ,open class ,data class 
    2. 类修饰符：public,private,protected,internal
    3. setter,getter
    4. 构造方法+继承/实现（单行构造方法）+overwrite
    5. interfase(与java接口的不同，Kotlin 接口与 Java 8 类似，使用 interface 关键字定义接口，允许方法有默认实现：)
    6. 匿名类（object）
6. 扩展+lambda
    1. extentions
    2. lambda
7. 函数式编程
    1. map
    2. filter
    3. foreach
7. kotlin中的静态，伴生对象（companion object）
8. 委托（by）
9. 协程(选看)
## 第2章 使用Kotlin编写Android项目：FilmGetter
1. 项目结构
2. MVP架构的kotlin实现
3. anko在项目中的使用
4. 爬虫结构（Crawler，Parser，Pipeline）
5. UI实现
## 第3章 为FilmGetter添加一个用户后台（选看）
1. 需求讲解
2. 设计数据库
3. flask实现接口
4. Android逻辑实现
5. 测试（PostMan）