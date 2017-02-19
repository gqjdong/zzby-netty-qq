package qq.codec;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import qq.exception.NotEnoughDataException;


public class BufferWrapper {

    protected final ChannelBuffer buffer;

    public static final String UTF8 = "UTF-8";

    public BufferWrapper(ChannelBuffer buffer) {
        super();
        this.buffer = buffer;
    }

    public static BufferWrapper createEmpty() {
        return new BufferWrapper(ChannelBuffers.dynamicBuffer(20));
    }

    public int read2Bytes() {
        return toIntValue(buffer.readByte(), buffer.readByte());
    }

    public int readByte() {
        return toIntValue(buffer.readByte());
    }

    public int read4Bytes() {
        return toIntValue(buffer.readByte(), buffer.readByte(), buffer.readByte(), buffer.readByte());
    }

    public boolean readAble() {
        return this.buffer.readable();
    }

    private BufferWrapper checkLength(int length) throws NotEnoughDataException {
        if (length > this.buffer.readableBytes()) {
            throw new NotEnoughDataException(length, this.buffer.readableBytes());
        }
        return this;
    }

    public int readableBytes() {
        return this.buffer.readableBytes();
    }

    public String getHexDump(int from, int end) {
        if (from > this.buffer.readableBytes()) {
            from = 0;
        }
        if (end > this.buffer.readableBytes()) {
            end = this.buffer.readableBytes();
        }
        return ChannelBuffers.hexDump(this.buffer.copy(from, end));
    }

    public byte[] readBytes(int length) throws NotEnoughDataException {
        // check length
        checkLength(length);
        byte[] result = new byte[length];
        this.buffer.readBytes(result);
        return result;
    }

    public byte[] toBytes() {
        byte[] dst = new byte[this.buffer.readableBytes()];
        this.buffer.readBytes(dst);
        return dst;
    }

    public BufferWrapper readAsBuffer(int length) throws NotEnoughDataException {
        checkLength(length);
        return new BufferWrapper(this.buffer.readBytes(length));
    }

    public ByteBuffer toByteBuffer() {
        return this.buffer.slice().toByteBuffer();
    }

    public String readAllAsString() {
        return this.buffer.readBytes(this.buffer.readableBytes()).toString(Charset.forName(UTF8));
    }

    public String readBytesAsString(int length) throws NotEnoughDataException {
        checkLength(length);
        return this.buffer.readBytes(length).toString(Charset.forName(UTF8));
    }

    public BufferWrapper writeByte(int demical) {
        this.buffer.writeByte(demical);
        return this;
    }

    public BufferWrapper write2Bytes(int demical) {
        this.buffer.writeBytes(to2ByteValue(demical));
        return this;
    }

    public BufferWrapper write4Bytes(int demical) {
        this.buffer.writeBytes(to4ByteValue(demical));
        return this;
    }

    public BufferWrapper writeBytes(byte[] content) {
        this.buffer.writeBytes(content);
        return this;
    }

    public BufferWrapper writeByteBuffer(ByteBuffer content) {
        this.buffer.writeBytes(content);
        return this;
    }

    public BufferWrapper writeString(String infoData) {
        if (infoData == null || infoData.length() == 0) {
            return this;
        }
        return this.writeBytes(convertString2Bytes(infoData));
    }

    public static byte[] convertString2Bytes(String str) {
        try {
            return str.getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            // Impossible
            throw new RuntimeException(e);
        }
    }

    private static byte[] to2ByteValue(int total) {
        return new byte[] { (byte) (total / 256), (byte) (total % 256) };
    }

    private static byte[] to4ByteValue(long total) {
        return new byte[] { (byte) ((total >> 24) & 0xff), (byte) ((total >> 16) & 0xff), (byte) ((total >> 8) & 0xff),
                (byte) (total % 256) };
    }

    /** parse byte(8bit) value to int */
    public static int toIntValue(byte in) {
        // System.out.println(Integer.toBinaryString((int)in));
        if (in < 0) {
            return Integer.parseInt(Integer.toBinaryString(in).substring(24), 2);
        } else {
            return in;
        }
    }

    /** parse two byte to int ,(high*256+low) */
    public static int toIntValue(byte highByte, byte lowByte) {
        return toIntValue(highByte) * 256 + toIntValue(lowByte);
    }

    /** parse two byte to int ,(high*256+low) */
    public static int toIntValue(byte h1, byte h2, byte h3, byte h4) {
        int a = toIntValue(h1) * 256 + toIntValue(h2);
        int b = toIntValue(h3) * 256 + toIntValue(h4);
        return (a << 16) + b;
    }

}
