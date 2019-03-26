package cn.qx;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Properties;

/**
 * 订阅指定分区,要与assign方法配合使用
 */
public class Comsumer4 {
    public static void main(String[] args){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.33.129:9092");
        props.put("group.id", "test30");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset","earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        String topic = "hello";
        TopicPartition partition0 = new TopicPartition(topic, 0);
        TopicPartition partition1 = new TopicPartition(topic, 1);
        //consumer.subscribe(Arrays.asList(topic));
        consumer.assign(Arrays.asList(partition1));
        //控制消费者的未知，即offset，此方法是最新的offset
        //consumer.seekToEnd(Arrays.asList(partition0,partition1));
        //consumer.seek(partition1,100);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            //System.out.println(records.partitions().size());
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }
}
