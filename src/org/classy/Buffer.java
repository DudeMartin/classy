package org.classy;

class Buffer {

    private final byte[] buffer;
    int offset;

    Buffer(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
    }

    int getUnsignedByte() {
        return buffer[offset++] & 0xFF;
    }

    int getUnsignedShort() {
        return (getUnsignedByte() << 8) + getUnsignedByte();
    }

    int getInteger() {
        return (getUnsignedShort() << 16) + getUnsignedShort();
    }

    String getString(int encodedLength) {
        int limit = offset + encodedLength;
        char[] decoded = new char[encodedLength];
        int index = 0;
        while (offset < limit) {
            char c = (char) getUnsignedByte();
            if (c < 0x80) {
                decoded[index++] = c;
            } else if (c < 0xE0 && c > 0xBF) {
                c &= 0x1F;
                decoded[index++] = (char) ((c << 6) + (buffer[offset++] & 0x3F));
            } else {
                c &= 0x0F;
                c = (char) ((c << 6) + (buffer[offset++] & 0x3F));
                decoded[index++] = (char) ((c << 6) + (buffer[offset++] & 0x3F));
            }
        }
        return new String(decoded, 0, index);
    }
}