package org.classy;

class Buffer {

    byte[] buffer;
    int offset;
    private final StringBuilder cachedBuilder = new StringBuilder();

    Buffer(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
    }

    Buffer(int initialCapacity) {
        buffer = new byte[initialCapacity];
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
        cachedBuilder.setLength(0);
        int currentByte;
        char partialCharacter = 0;
        int state = 0;
        while (encodedLength-- > 0) {
            currentByte = getByte();
            switch (state) {
                case 0:
                    currentByte &= 0xFF;
                    if (currentByte < 0x80) {
                        cachedBuilder.append((char) currentByte);
                    } else if (currentByte < 0xE0 && currentByte > 0xBF) {
                        partialCharacter = (char) (currentByte & 0x1F);
                        state = 1;
                    } else {
                        partialCharacter = (char) (currentByte & 0xF);
                        state = 2;
                    }
                    break;
                case 1:
                    cachedBuilder.append((char) ((partialCharacter << 6) | (currentByte & 0x3F)));
                    state = 0;
                    break;
                case 2:
                    partialCharacter = (char) ((partialCharacter << 6) | (currentByte & 0x3F));
                    state = 1;
                    break;
            }
        }
        return cachedBuilder.toString();
    }

    byte[] getBytes(int amount) {
        byte[] bytes = new byte[amount];
        System.arraycopy(buffer, offset, bytes, 0, amount);
        offset += amount;
        return bytes;
    }
}