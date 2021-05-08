package runner;
import kv.key.ComDimension;
import kv.value.CountDurationValue;
import mapper.CountDurationMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import outputformat.MysqlOutputFormat;
import reducer.CountDurationReducer;
import java.io.IOException;

public class CountDurationRunner implements Tool{
    private Configuration conf = null;
    @Override
    public void setConf(Configuration conf) {
        this.conf = HBaseConfiguration.create(conf);
    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }

    private void initHBaseInputConfig(Job job) {
        Connection connection = null;
        Admin admin = null;
        try {
            String tableName = "ns_ct:calllog";
            connection = ConnectionFactory.createConnection(job.getConfiguration());
            admin = connection.getAdmin();
            if(!admin.tableExists(TableName.valueOf(tableName))) {
                throw new RuntimeException("无法找到目标表.");
            }
            Scan scan = new Scan();
            //可以优化
            //初始化Mapper
            TableMapReduceUtil.initTableMapperJob(
                    tableName,
                    scan,
                    //map类
                    CountDurationMapper.class,
                    //输入
                    ComDimension.class,
                    //输出
                    Text.class,
                    job,
                    true);


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(admin != null){
                    admin.close();
                }
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void initReducerOutputConfig(Job job) {
        //reduce类
        job.setReducerClass(CountDurationReducer.class);
        //输入
        job.setOutputKeyClass(ComDimension.class);
        //输出
        job.setOutputValueClass(CountDurationValue.class);
        //输出格式
        job.setOutputFormatClass(MysqlOutputFormat.class);
    }

    @Override
    public int run(String[] args) throws Exception {
        //得到conf
        Configuration conf = this.getConf();
        //实例化Job
        Job job = Job.getInstance(conf);
        //本类
        job.setJarByClass(CountDurationRunner.class);
        //map类
        initHBaseInputConfig(job);
        //reduce类
        initReducerOutputConfig(job);
        return job.waitForCompletion(true) ? 0 : 1;
    }



    public static void main(String[] args) {
        try {
            int status = ToolRunner.run(new CountDurationRunner(), args);
            System.exit(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
