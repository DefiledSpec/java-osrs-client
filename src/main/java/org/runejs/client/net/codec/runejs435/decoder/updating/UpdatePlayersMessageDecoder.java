package org.runejs.client.net.codec.runejs435.decoder.updating;

import java.util.ArrayList;
import java.util.List;

import org.runejs.client.message.inbound.updating.movement.LocalPlayerMovementUpdate;
import org.runejs.client.message.inbound.updating.movement.ActorGroupMovementUpdate;
import org.runejs.client.message.inbound.updating.movement.MovementUpdate;
import org.runejs.client.message.inbound.updating.registration.ActorGroupRegistrationUpdate;
import org.runejs.client.message.inbound.updating.UpdatePlayersInboundMessage;
import org.runejs.client.message.inbound.updating.movement.LocalPlayerMovementUpdate.LocalPlayerMapRegionChangeUpdate;
import org.runejs.client.message.inbound.updating.movement.ActorGroupMovementUpdate.ActorMovementUpdate;
import org.runejs.client.message.inbound.updating.registration.ActorRegistration;
import org.runejs.client.net.PacketBuffer;
import org.runejs.client.net.codec.MessageDecoder;

/**
 * Decodes the {@link UpdatePlayersInboundMessage}.
 * 
 * This message is sent by the server to update the local player and
 * other surrounding players.
 */
public class UpdatePlayersMessageDecoder implements MessageDecoder<UpdatePlayersInboundMessage> {

    /**
     * Decodes a {@link PacketBuffer} into a {@link UpdatePlayersInboundMessage},
     * which can then be processed by the game.
     */
    @Override
    public UpdatePlayersInboundMessage decode(PacketBuffer buffer) {
        buffer.initBitAccess();
        LocalPlayerMovementUpdate localPlayerMovement = decodeLocalPlayerMovementUpdate(buffer);
        ActorGroupMovementUpdate otherPlayersMovement = decodeOtherPlayersMovementUpdate(buffer);
        ActorGroupRegistrationUpdate<ActorRegistration> registerNewPlayers = decodeRegisterNewPlayersUpdate(buffer);
        buffer.finishBitAccess();

        // the remaining bytes in the buffer are the appearance update
        PacketBuffer appearanceUpdate = UpdateDecoderHelpers.decodeRemainingBytes(buffer);

        return new UpdatePlayersInboundMessage(
                localPlayerMovement,
                otherPlayersMovement,
                registerNewPlayers,
                appearanceUpdate);
    }

    /**
     * Decodes the local player movement update.
     * 
     * @param buffer The buffer.
     * @return The local player movement update.
     */
    private LocalPlayerMovementUpdate decodeLocalPlayerMovementUpdate(PacketBuffer buffer) {
        boolean updateRequired = buffer.getBits(1) == 1;

        // No update required
        if (!updateRequired) {
            return null;
        }

        int movementType = buffer.getBits(2);

        // No movement
        if (movementType == 0) {
            return new LocalPlayerMovementUpdate(null, null);
        }
        
        // Walking
        if (movementType == 1) {
            int walkDirection = buffer.getBits(3);
            boolean runUpdateBlock = buffer.getBits(1) == 1;

            return new LocalPlayerMovementUpdate(
                    new MovementUpdate(walkDirection, null, runUpdateBlock),
                    null);
        }

        // Running
        if (movementType == 2) {
            int walkDirection = buffer.getBits(3);
            int runDirection = buffer.getBits(3);
            boolean runUpdateBlock = buffer.getBits(1) == 1;

            return new LocalPlayerMovementUpdate(
                    new MovementUpdate(walkDirection, runDirection, runUpdateBlock),
                    null);
        }

        // Map region changed
        if (movementType == 3) {
            boolean teleporting = buffer.getBits(1) == 1;
            int worldLevel = buffer.getBits(2);
            boolean runUpdateBlock = buffer.getBits(1) == 1;
            int localChunkX = buffer.getBits(7);
            int localChunkY = buffer.getBits(7);

            return new LocalPlayerMovementUpdate(
                    null,
                    new LocalPlayerMapRegionChangeUpdate(teleporting, worldLevel, runUpdateBlock, localChunkX,
                            localChunkY));
        }

        throw new IllegalStateException("Invalid movement type: " + movementType);
    }

    /**
     * Decodes the movement details of other players into an {@link ActorGroupMovementUpdate}.
     * 
     * @param buffer The buffer.
     * @return The other players movement update.
     */
    private ActorGroupMovementUpdate decodeOtherPlayersMovementUpdate(PacketBuffer buffer) {
        int trackedPlayerCount = buffer.getBits(8);

        List<ActorMovementUpdate> updates = new ArrayList<ActorMovementUpdate>();

        for (int i = 0; trackedPlayerCount > i; i++) {
            boolean updateRequired = buffer.getBits(1) == 1;

            // No update required
            if (!updateRequired) {
                updates.add(null);
                continue;
            }

            int movementType = buffer.getBits(2);

            // No movement
            if (movementType == 0) {
                updates.add(new ActorMovementUpdate(null, false));
                continue;
            }

            // deregister
            if (movementType == 3) {
                updates.add(new ActorMovementUpdate(null, true));
                continue;
            }

            // walking
            if (movementType == 1) {
                int walkDirection = buffer.getBits(3);
                boolean runUpdateBlock = buffer.getBits(1) == 1;

                updates.add(new ActorMovementUpdate(
                    new MovementUpdate(walkDirection, null, runUpdateBlock), false));
                continue;
            }

            // running
            if (movementType == 2) {
                int walkDirection = buffer.getBits(3);
                int runDirection = buffer.getBits(3);
                boolean runUpdateBlock = buffer.getBits(1) == 1;

                updates.add(new ActorMovementUpdate(
                    new MovementUpdate(walkDirection, runDirection, runUpdateBlock), false));
                continue;
            }

            throw new IllegalStateException("Invalid movement type: " + movementType);
        }

        return new ActorGroupMovementUpdate(trackedPlayerCount, updates.toArray(new ActorMovementUpdate[updates.size()]));
    }

    /**
     * Decodes the registration details of new players into a {@link ActorGroupRegistrationUpdate}.
     * 
     * @param buffer The buffer.
     * @return The register new players update.
     */
    private ActorGroupRegistrationUpdate<ActorRegistration> decodeRegisterNewPlayersUpdate(PacketBuffer buffer) {
        List<ActorRegistration> updates = new ArrayList<ActorRegistration>();

        while (buffer.getRemainingBits(buffer.getSize()) >= 11) {
            int newPlayerIndex = buffer.getBits(11);

            if (newPlayerIndex == 2047)
                break;

            int offsetX = buffer.getBits(5);
            int offsetY = buffer.getBits(5);
            if (offsetX > 15)
                offsetX -= 32;
            if (offsetY > 15)
                offsetY -= 32;
            int initialFaceDirection = buffer.getBits(3);
            boolean updateRequired = buffer.getBits(1) == 1;
            boolean discardWalkingQueue = buffer.getBits(1) == 1;

            updates.add(new ActorRegistration(newPlayerIndex, offsetX, offsetY, initialFaceDirection,
                    updateRequired, discardWalkingQueue));
        }

        return new ActorGroupRegistrationUpdate<ActorRegistration>(updates.toArray(new ActorRegistration[updates.size()]));
    }

}
