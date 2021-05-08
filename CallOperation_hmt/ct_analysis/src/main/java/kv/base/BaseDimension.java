package kv.base;

import org.apache.hadoop.io.WritableComparable;

//抽象类，不用实现方法
public abstract class BaseDimension implements WritableComparable<BaseDimension>{}
