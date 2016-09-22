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
        return getByte() & 0xFF;
    }

    int getUnsignedShort() {
        return (getUnsignedByte() << 8) + getUnsignedByte();
    }

    int getShort() {
        return (short) getUnsignedShort();
    }

    int getInteger() {
        return (getUnsignedShort() << 16) + getUnsignedShort();
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

    void ensureSpace(int needed) {
        int target = offset + needed;
        if (target >= buffer.length) {
            int newCapacity;
            for (newCapacity = buffer.length; newCapacity <= target; newCapacity *= 2);
            byte[] newBuffer = new byte[newCapacity];
            System.arraycopy(buffer, 0, newBuffer, 0, offset);
            buffer = newBuffer;
        }
    }

    void putByte(int value) {
        ensureSpace(1);
        directPut(value);
    }

    void putShort(int value) {
        ensureSpace(2);
        directPut(value >>> 8);
        directPut(value);
    }

    void putInteger(int value) {
        ensureSpace(4);
        directPut(value >>> 24);
        directPut(value >>> 16);
        directPut(value >>> 8);
        directPut(value);
    }

    void putLong(long value) {
        ensureSpace(8);
        buffer[offset++] = (byte) (value >>> 56);
        buffer[offset++] = (byte) (value >>> 48);
        buffer[offset++] = (byte) (value >>> 40);
        buffer[offset++] = (byte) (value >>> 32);
        buffer[offset++] = (byte) (value >>> 24);
        buffer[offset++] = (byte) (value >>> 16);
        buffer[offset++] = (byte) (value >>> 8);
        buffer[offset++] = (byte) value;
    }

    void putString(String value) {
        int length = value.length();
        int spaceNeeded = 2;
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c < 0x80) {
                spaceNeeded += 1;
            } else if (c < 0xE0 && c > 0xBF) {
                spaceNeeded += 2;
            } else {
                spaceNeeded += 3;
            }
        }
        ensureSpace(spaceNeeded);
        directPut(length >>> 8);
        directPut(length);
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c < 0x80) {
                directPut(c);
            } else if (c < 0xE0 && c > 0xBF) {
                directPut(0xC0 | c >> 6 & 0x1F);
                directPut(0x80 | c & 0x3F);
            } else {
                directPut(0xE0 | c >> 12 & 0x0F);
                directPut(0x80 | c >> 6 & 0x3F);
                directPut(0x80 | c & 0x3F);
            }
        }
    }

    void putBytes(byte[] bytes, int start, int length) {
        ensureSpace(length);
        System.arraycopy(bytes, start, buffer, offset, length);
        offset += length;
    }

    private void directPut(int value) {
        buffer[offset++] = (byte) value;
    }
}