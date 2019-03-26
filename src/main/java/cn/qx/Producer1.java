package cn.qx;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Producer1 {
    public static void main(String[] args){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.33.129:9092");
        props.put("acks", "all");
        //如果发送失败重新发送
        props.put("retries", 0);
        //缓存发送
        props.put("batch.size", 16384);
        //生产者延迟发送
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
//        for(int i = 200; i < 300; i++){
//            producer.send(new ProducerRecord<String, String>("hello", "key"+i, "value"+i));
//        }

        for(int i = 300; i < 10000; i++){
            try {
                producer.send(new ProducerRecord<String, String>("hello2", "key"+i, "value"+i)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

//        for(int i = 0; i < 100; i++){
//            producer.send(new ProducerRecord<String, String>("my-topic2", Integer.toString(i), Integer.toString(i)),
//                    new Callback() {
//                        public void onCompletion(RecordMetadata metadata, Exception e) {
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e1) {
//                                e1.printStackTrace();
//                            }
//                            if(e != null)
//                                e.printStackTrace();
//                            System.out.println("The offset of the record we just sent is: " + metadata.offset());
//                        }
//                    });
//        }
        producer.close();
    }
}
