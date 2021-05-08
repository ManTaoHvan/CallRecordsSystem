package kv.key;

import kv.base.BaseDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

    //总维度
public class ComDimension extends BaseDimension {
    //"联系"人维度
    private ContactDimension contactDimension = new ContactDimension();
    //"时间"维度
    private DateDimension dateDimension = new DateDimension();

    //构造方法
    public ComDimension(){

    }
    //得到联系人维度
    public ContactDimension getContactDimension() {
        return contactDimension;
    }

    //set联系人维度，内存地址赋给别的对象
    public void setContactDimension(ContactDimension contactDimension) {
        this.contactDimension = contactDimension;
    }

    public DateDimension getDateDimension() {
        return dateDimension;
    }

    public void setDateDimension(DateDimension dateDimension) {
        this.dateDimension = dateDimension;
    }

    @Override
    public int compareTo(BaseDimension o) {
        //向下转型
        ComDimension anotherComDimension = (ComDimension) o;
        //dateDimension调用自己的compareTo方法
        int result = this.dateDimension.compareTo(anotherComDimension.dateDimension);
        if(result != 0) {
            return result;
        }
        result = this.contactDimension.compareTo(anotherComDimension.contactDimension);
        return result;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        //
        contactDimension.write(out);
        dateDimension.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        contactDimension.readFields(in);
        dateDimension.readFields(in);
    }
}
