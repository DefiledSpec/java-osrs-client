package org.runejs.client.net.codec.runejs435.decoder.updating;

import org.runejs.client.net.PacketBuffer;

public class UpdateDecoderHelpers {

    /**
     * Decodes the remaining bytes in the buffer into a new buffer
     *
     * This is used for the appearance update
     *
     * @param buffer The buffer to decode the remaining bytes from
     * @return A new buffer containing the remaining bytes
     */
    public static PacketBuffer decodeRemainingBytes(PacketBuffer buffer) {
        // 5000 bytes is the length previously used in IncomingPackets
        // we can probably refactor to use a smaller/dynamic length in future
        int REMAINING_BYTES_LENGTH = 5000;

        int remainingBytes = buffer.getSize() - buffer.currentPosition;
        PacketBuffer remainingBuffer = new PacketBuffer(REMAINING_BYTES_LENGTH);
        System.arraycopy(buffer.buffer, buffer.currentPosition, remainingBuffer.buffer, 0, remainingBytes);
        remainingBuffer.currentPosition = 0;

        return remainingBuffer;
    }
}
