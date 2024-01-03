package org.SCAU.SerializerDeserializer;

import org.SCAU.model.socialMediaStocks;
import org.SCAU.model.socialMediaStocks2;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class socialStockSerializerDeserializer implements SerializationSchema<socialMediaStocks2>, DeserializationSchema<socialMediaStocks2> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(socialMediaStocks2 stock) {
        try {
            return mapper.writeValueAsBytes(stock);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public socialMediaStocks2 deserialize(byte[] bytes) throws IOException {
//        System.out.println("bytes: " + new String(bytes));
        return mapper.readValue(bytes, socialMediaStocks2.class);
    }

    @Override
    public boolean isEndOfStream(socialMediaStocks2 secEvent) {
        return false;
    }

    @Override
    public TypeInformation<socialMediaStocks2> getProducedType() {
        return TypeExtractor.getForClass(socialMediaStocks2.class);
    }
}
