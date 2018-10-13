package model;

import util.Constants;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferStateWrapper {
    private int index;
    private ByteBuffer byteBuffer;
    private int tupleSize;
    private int tupleCount;

    public BufferStateWrapper(int index, ByteBuffer byteBuffer, int tupleSize) {
        this.index = index;
        this.byteBuffer = byteBuffer;
        this.tupleSize = tupleSize;
        tupleCount = 0;
    }

    public void putInt(int data) {
        byteBuffer.putInt(index, data);
        index += Constants.INT_SIZE;
    }

    public void putTuple(Tuple tuple) {
        for (int i = 0; i < tuple.getDataLength(); i++) {
            putInt(tuple.getDataAt(i));
        }
        tupleCount++;
    }

    public boolean writeBuffer(FileChannel fileChannel) {
        putMetaData();
        try{
            fileChannel.write(byteBuffer);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void putMetaData() {
        byteBuffer.putInt(0, tupleSize);
        byteBuffer.putInt(Constants.INT_SIZE, tupleCount);
    }

    public boolean hasSpace() {
        return index + tupleSize * Constants.INT_SIZE < Constants.PAGE_SIZE;
    }
}