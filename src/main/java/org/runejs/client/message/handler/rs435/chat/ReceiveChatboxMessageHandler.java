package org.runejs.client.message.handler.rs435.chat;

import org.runejs.client.Game;
import org.runejs.client.frame.ChatBox;
import org.runejs.client.language.English;
import org.runejs.client.language.Native;
import org.runejs.client.media.renderable.actor.Player;
import org.runejs.client.message.handler.MessageHandler;
import org.runejs.client.message.inbound.chat.ReceiveChatboxMessageInboundMessage;
import org.runejs.client.util.TextUtils;

/**
 * A {@link MessageHandler} that handles {@link ReceiveChatboxMessageInboundMessage}s.
 * 
 * Responsible for displaying general chat messages and trade/duel requests.
 */
public class ReceiveChatboxMessageHandler implements MessageHandler<ReceiveChatboxMessageInboundMessage> {
    @Override
    public void handle(ReceiveChatboxMessageInboundMessage message) {
        String chatMessage = message.message;

        if(chatMessage.endsWith(Native.tradeRequest)) {
            String username = chatMessage.substring(0, chatMessage.indexOf(Native.colon));
            long l = TextUtils.nameToLong(username);
            boolean bool = Game.ignoreList.containsPlayer(l);
            if(!bool && !Player.inTutorialIsland)
                ChatBox.addChatMessage(username, "wishes to trade with you.", 4);
        } else if(chatMessage.endsWith(Native.duelRequest)) {
            String username = chatMessage.substring(0, chatMessage.indexOf(Native.colon));
            long l = TextUtils.nameToLong(username);
            boolean bool = Game.ignoreList.containsPlayer(l);
            if(!bool && !Player.inTutorialIsland)
                ChatBox.addChatMessage(username, English.suffixWishesToDuelWithYou, 8);
        } else if(chatMessage.endsWith(Native.challengeRequest)) {
            String username = chatMessage.substring(0, chatMessage.indexOf(Native.colon));
            long l = TextUtils.nameToLong(username);
            boolean bool = Game.ignoreList.containsPlayer(l);
            if(!bool && !Player.inTutorialIsland) {
                String challengeMessage = chatMessage.substring(1 + chatMessage.indexOf(Native.colon), -9 + chatMessage.length());
                ChatBox.addChatMessage(username, challengeMessage, 8);
            }
        } else {
            ChatBox.addChatMessage("", chatMessage, 0);
        }
    }
}
