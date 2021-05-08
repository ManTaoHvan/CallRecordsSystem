package reducer;

import kv.key.ComDimension;
import kv.value.CountDurationValue;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

//ComDimension, Text,来自于map,ComDimension总维度
public class CountDurationReducer extends Reducer<ComDimension, Text, ComDimension, CountDurationValue>{
    //countDurationValue有两个属性 callSum 和 callDurationSum
    CountDurationValue countDurationValue = new CountDurationValue();
    @Override
    //相同的key合并
    protected void reduce(ComDimension key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        int     callSum = 0;
        int callDurationSum = 0;
        for(Text t : values){
            //通话次数
            callSum++;
            //包装，通话时间总和
            callDurationSum += Integer.valueOf(t.toString());
        }
        countDurationValue.setCallSum(String.valueOf(callSum));
        countDurationValue.setCallDurationSum(String.valueOf(callDurationSum));
        //序列化，countDurationValue是自定义对象，所以要序列化
        context.write(key, countDurationValue);
    }
}
