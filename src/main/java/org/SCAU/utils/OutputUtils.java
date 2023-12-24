package org.SCAU.utils;

import org.SCAU.model.StockEventNew;

import org.apache.flink.streaming.api.operators.Output;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.runtime.streamrecord.LatencyMarker;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;
import org.apache.flink.streaming.runtime.watermarkstatus.WatermarkStatus;
import org.apache.flink.util.OutputTag;

public class OutputUtils {

    public static class OutputRecord implements Output<StockEventNew> {

        private StockEventNew event;

        public void setEvent(StockEventNew event) {
            this.event = event;
        }



        @Override
        public void emitWatermark(Watermark mark) {

        }

        @Override
        public void emitWatermarkStatus(WatermarkStatus watermarkStatus) {

        }

        @Override
        public <X> void collect(OutputTag<X> outputTag, StreamRecord<X> record) {

        }

        @Override
        public void emitLatencyMarker(LatencyMarker latencyMarker) {

        }

        @Override
        public void collect(StockEventNew record) {

        }

        @Override
        public void close() {

        }

        // 其他接口方法
    }

}