package org.classy;

class Buffer {

    byte[] buffer;
    int offset;

    Buffer(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
    }

    Buffer(int initialCapacity) {
        this(new byte[initialCapacity], 0);
    }

    int getByte() {
        return buffer[offset++];
    }

    int getUnsignedByte() {
        return buffer[offset++] & 0xFF;
    }

    int getUnsignedShort() {
        offset += 2;
        return ((buffer[offset - 2] & 0xFF) << 8) | (buffer[offset - 1] & 0xFF);
    }

    int getShort() {
        return (short) getUnsignedShort();
    }

    int getInteger() {
        offset += 4;
        return ((buffer[offset - 4] & 0xFF) << 24)
                | ((buffer[offset - 3] & 0xFF) << 16)
                | ((buffer[offset - 2] & 0xFF) << 8)
                | (buffer[offset - 1] & 0xFF);
    }

    String getString(int encodedLength) {
        char[] decoded = new char[encodedLength];
        int index = 0;
        while (encodedLength-- > 0) {
            char c = (char) getUnsignedByte();
            if (c < 0x80) {
                decoded[index++] = c;
            } else if (c < 0xE0 && c > 0xBF) {
                c &= 0x1F;
                decoded[index++] = (char) ((c << 6) + (getByte() & 0x3F));
            } else {
                c &= 0x0F;
                c = (char) ((c << 6) + (getByte() & 0x3F));
                decoded[index++] = (char) ((c << 6) + (getByte() & 0x3F));
            }
        }
        return new String(decoded, 0, index);
    }

    byte[] getBytes(int amount) {
        byte[] bytes = new byte[amount];
        System.arraycopy(buffer, offset, bytes, 0, amount);
        offset += amount;
        return bytes;
    }
}