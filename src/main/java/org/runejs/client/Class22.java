package org.runejs.client;

import org.runejs.client.cache.CacheArchive;
import org.runejs.client.cache.def.GameObjectDefinition;
import org.runejs.client.cache.def.IdentityKit;
import org.runejs.client.cache.def.VarPlayerDefinition;
import org.runejs.client.cache.media.AnimationSequence;
import org.runejs.client.cache.media.IndexedImage;
import org.runejs.client.frame.ChatBox;
import org.runejs.client.input.MouseHandler;
import org.runejs.client.io.Buffer;
import org.runejs.client.language.English;
import org.runejs.client.media.Rasterizer3D;
import org.runejs.client.media.renderable.GameObject;
import org.runejs.client.media.renderable.actor.Actor;
import org.runejs.client.media.renderable.actor.Npc;
import org.runejs.client.media.renderable.actor.Player;
import org.runejs.client.scene.Scene;
import org.runejs.client.scene.util.CollisionMap;

public abstract class Class22 {
    public static IndexedImage[] aClass40_Sub5_Sub14_Sub2Array535;
    public static int anInt537 = 0;
    public static boolean membersWorld = false;
    public static boolean accountFlagged = false;
    public static int anInt545;
    public static boolean[] aBooleanArray548 = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true, false, false};

    public static void method299(byte arg0, int arg1) {
        int[] is = MovedStatics.minimapImage.pixels;
        int i = is.length;
        for(int i_0_ = 0; i > i_0_; i_0_++)
            is[i_0_] = 0;
        for(int i_1_ = 1; i_1_ < 103; i_1_++) {
            int i_2_ = 24628 + (-(512 * i_1_) + 52736) * 4;
            for(int i_3_ = 1; i_3_ < 103; i_3_++) {
                if((0x18 & Scene.tileFlags[arg1][i_3_][i_1_]) == 0)
                    Npc.currentScene.method96(is, i_2_, 512, arg1, i_3_, i_1_);
                if(arg1 < 3 && (Scene.tileFlags[1 + arg1][i_3_][i_1_] & 0x8) != 0)
                    Npc.currentScene.method96(is, i_2_, 512, 1 + arg1, i_3_, i_1_);
                i_2_ += 4;
            }
        }
        MovedStatics.minimapImage.method723();
        int i_4_ = (-10 + (int) (Math.random() * 20.0) + 238 << 8) + (228 + (int) (Math.random() * 20.0) << 16) + 238 + (int) (20.0 * Math.random()) + -10;
        int i_5_ = -10 + (int) (20.0 * Math.random()) + 238 << 16;
        for(int i_6_ = 1; i_6_ < 103; i_6_++) {
            for(int i_7_ = 1; i_7_ < 103; i_7_++) {
                if((Scene.tileFlags[arg1][i_7_][i_6_] & 0x18) == 0)
                    Actor.method781(1850, arg1, i_7_, i_4_, i_5_, i_6_);
                if(arg1 < 3 && (0x8 & Scene.tileFlags[1 + arg1][i_7_][i_6_]) != 0)
                    Actor.method781(1850, 1 + arg1, i_7_, i_4_, i_5_, i_6_);
            }
        }
        GameObject.minimapHintCount = 0;
        if(arg0 < 24)
            Player.trackedPlayerAppearanceCache = null;
        for(int i_8_ = 0; i_8_ < 104; i_8_++) {
            for(int i_9_ = 0; i_9_ < 104; i_9_++) {
                int i_10_ = Npc.currentScene.getFloorDecorationHash(Player.worldLevel, i_8_, i_9_);
                if(i_10_ != 0) {
                    i_10_ = 0x7fff & i_10_ >> 14;
                    int i_11_ = GameObjectDefinition.getDefinition(i_10_).icon;
                    if(i_11_ >= 0) {
                        int i_12_ = i_9_;
                        int i_13_ = i_8_;
                        if(i_11_ != 22 && i_11_ != 29 && i_11_ != 34 && i_11_ != 36 && i_11_ != 46 && i_11_ != 47 && i_11_ != 48) {
                            int[][] is_14_ = Landscape.currentCollisionMap[Player.worldLevel].clippingData;
                            for(int i_15_ = 0; i_15_ < 10; i_15_++) {
                                int i_16_ = (int) (Math.random() * 4.0);
                                if(i_16_ == 0 && i_13_ > 0 && i_13_ > -3 + i_8_ && (is_14_[-1 + i_13_][i_12_] & 0x1280108) == 0)
                                    i_13_--;
                                if(i_16_ == 1 && i_13_ < 103 && i_13_ < i_8_ + 3 && (is_14_[i_13_ + 1][i_12_] & 0x1280180) == 0)
                                    i_13_++;
                                if(i_16_ == 2 && i_12_ > 0 && i_12_ > -3 + i_9_ && (is_14_[i_13_][i_12_ - 1] & 0x1280102) == 0)
                                    i_12_--;
                                if(i_16_ == 3 && i_12_ < 103 && 3 + i_9_ > i_12_ && (0x1280120 & is_14_[i_13_][1 + i_12_]) == 0)
                                    i_12_++;
                            }
                        }
                        MouseHandler.minimapHint[GameObject.minimapHintCount] = Class8.aClass40_Sub5_Sub14_Sub4Array296[i_11_];
                        Actor.minimapHintX[GameObject.minimapHintCount] = i_13_;
                        LinkedList.minimapHintY[GameObject.minimapHintCount] = i_12_;
                        GameObject.minimapHintCount++;
                    }
                }
            }
        }
    }

    public static void method305() {
//        if(ScreenController.frameMode == ScreenMode.FIXED){

            RSCanvas.chatBoxImageProducer.prepareRasterizer();
//        }
        Class44.chatboxBackgroundImage.drawImage(0, 0);
        MovedStatics.chatboxLineOffsets = Rasterizer3D.setLineOffsets(MovedStatics.chatboxLineOffsets);
    }

    public static String method307(Buffer arg0, int arg1, int arg2) {
        try {
            if(arg1 != -1)
                aBooleanArray548 = null;
            int length = arg0.getSmart();
            if(length > arg2)
                length = arg2;
            byte[] chars = new byte[length];
            arg0.currentPosition += IdentityKit.aHuffmanEncoding_2590.method1023(arg0.buffer, length, 0, chars, arg0.currentPosition, -1);
            return new String(chars);
        } catch(Exception exception) {
            return English.cabbage;
        }
    }

    public static void method308() {
        RSString.method56(false, null, 0);
    }

    public static void method309(int varPlayerIndex) {
        do {
            AnimationSequence.anInt2480 = MovedStatics.pulseCycle;
            HuffmanEncoding.method1030((byte) 127);
            int varPlayerType = VarPlayerDefinition.getDefinition(varPlayerIndex).type;
            if(varPlayerType != 0) {
                int varPlayerValue = VarPlayerDefinition.varPlayers[varPlayerIndex];
                if(varPlayerType == 1) {
                    if(varPlayerValue == 1) {
                        Rasterizer3D.method711(0.9);
                        ((TextureStore) Rasterizer3D.textureStore).method424(0.9);
                    }
                    if(varPlayerValue == 2) {
                        Rasterizer3D.method711(0.8);
                        ((TextureStore) Rasterizer3D.textureStore).method424(0.8);
                    }
                    if(varPlayerValue == 3) {
                        Rasterizer3D.method711(0.7);
                        ((TextureStore) Rasterizer3D.textureStore).method424(0.7);
                    }
                    if(varPlayerValue == 4) {
                        Rasterizer3D.method711(0.6);
                        ((TextureStore) Rasterizer3D.textureStore).method424(0.6);
                    }
                    GameObject.clearImageCache();
                    MovedStatics.clearScreen = true;
                }
                if(varPlayerType == 3) {
                    int i_22_ = 0;
                    if(varPlayerValue == 0)
                        i_22_ = 255;
                    if(varPlayerValue == 1)
                        i_22_ = 192;
                    if(varPlayerValue == 2)
                        i_22_ = 128;
                    if(varPlayerValue == 3)
                        i_22_ = 64;
                    if(varPlayerValue == 4)
                        i_22_ = 0;
                    if(i_22_ != RSCanvas.musicVolume) {
                        if(RSCanvas.musicVolume != 0 || MouseHandler.currentSongId == -1) {
                            if(i_22_ == 0) {
                                Class33.method402(false);
                                TextureStore.songTimeout = 0;
                            } else
                                MovedStatics.method456(i_22_);
                        } else {
                            Class33.method414(false, 0, MouseHandler.currentSongId, i_22_, 0, CacheArchive.musicCacheArchive);
                            TextureStore.songTimeout = 0;
                        }
                        RSCanvas.musicVolume = i_22_;
                    }
                }
                if(varPlayerType == 9)
                    Class43.bankInsertMode = varPlayerValue;
                if(varPlayerType == 10) {
                    if(varPlayerValue == 0)
                        RSCanvas.anInt65 = 127;
                    if(varPlayerValue == 1)
                        RSCanvas.anInt65 = 96;
                    if(varPlayerValue == 2)
                        RSCanvas.anInt65 = 64;
                    if(varPlayerValue == 3)
                        RSCanvas.anInt65 = 32;
                    if(varPlayerValue == 4)
                        RSCanvas.anInt65 = 0;
                }
                if(varPlayerType == 8) {
                    ChatBox.redrawChatbox = true;
                    CollisionMap.anInt165 = varPlayerValue;
                }
                if(varPlayerType == 4) {
                    if(varPlayerValue == 0)
                        MovedStatics.anInt200 = 127;
                    if(varPlayerValue == 1)
                        MovedStatics.anInt200 = 96;
                    if(varPlayerValue == 2)
                        MovedStatics.anInt200 = 64;
                    if(varPlayerValue == 3)
                        MovedStatics.anInt200 = 32;
                    if(varPlayerValue == 4)
                        MovedStatics.anInt200 = 0;
                }
                if(varPlayerType == 6)
                    MovedStatics.anInt2280 = varPlayerValue;
                if(varPlayerType != 5)
                    break;
                ProducingGraphicsBuffer.oneMouseButton = varPlayerValue;
            }

            break;
        } while(false);
    }

    public abstract void method300(byte[] is, boolean bool, int i, int i_17_);

    public abstract void method301(int i, int i_18_);

    public abstract void method302(int i);

    public abstract void method303(byte i);

    public abstract void method304(byte i, int i_19_, int i_20_);

    public abstract void method306(byte i);
}
