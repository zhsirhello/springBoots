package cn.qx;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 多线程处理？具体做法未知
 */
public class Comsumer3 implements Runnable{
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final KafkaConsumer consumer;

    public  Comsumer3(KafkaConsumer comsumer){
        this.consumer=comsumer;
    }

    public void run() {
        try {
            consumer.subscribe(Arrays.asList("hello2"));
            while (!closed.get()) {
                ConsumerRecords<String,String> records = consumer.poll(10000);
                // Handle new records
                for (ConsumerRecord<String, String> record : records){
                    System.out.println(Thread.currentThread().getName());
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                }
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        } finally {
            consumer.close();
        }
    }

    // Shutdown hook which can be called from a separate thread
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }

    public static void main(String[] args){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.33.129:9092");
        props.put("group.id", "test100");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset","earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        ExecutorService executor = Executors.newFixedThreadPool(4);
       //KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        for(int i=0;i<3;i++){
            KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
            executor.execute(new Comsumer3(consumer));
        }
    }
}






