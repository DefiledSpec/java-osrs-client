package org.runejs.client.input;

import org.runejs.client.node.NodeCache;
import org.runejs.client.cache.media.gameInterface.GameInterface;
import org.runejs.client.frame.ChatBox;
import org.runejs.client.frame.ScreenController;
import org.runejs.client.frame.ScreenMode;
import org.runejs.client.frame.console.Console;
import org.runejs.client.io.Buffer;
import org.runejs.client.media.VertexNormal;
import org.runejs.client.media.renderable.GameObject;
import org.runejs.client.media.renderable.Renderable;
import org.runejs.client.media.renderable.actor.Player;
import org.runejs.client.scene.GroundItemTile;
import org.runejs.client.scene.InteractiveObject;
import org.runejs.client.scene.SceneCamera;
import org.runejs.client.scene.util.CollisionMap;
import org.runejs.client.*;

import java.awt.*;
import java.awt.event.*;

public class MouseHandler implements MouseListener, MouseMotionListener, FocusListener, MouseWheelListener {
    public static NodeCache modelCache = new NodeCache(50);
    public static int currentTickSample;
    public static Canvas gameCanvas;
    public static int clickType = 0;
    public static int currentMouseButtonPressed = 0;
    public static volatile int mouseButtonPressed = 0;
    public static volatile int eventMouseButtonPressed = 0;
    public static volatile int eventMouseX = -1;
    public static volatile int eventMouseY = -1;
    public static int mouseX = 0;
    public static int mouseY = 0;
    public static int clickX = 0;
    public static int clickY = 0;
    public static long aLong2561 = 0L;
    public static int cursorY;
    public static int cursorX;
    public static boolean gameScreenClickable;
    private static volatile long lastClick = 0L;
    public boolean mouseWheelDown;
    public int mouseWheelX;
    public int mouseWheelY;



    public static void processMenuClick() {
        if(MovedStatics.activeInterfaceType != 0) {
            return;
        }
        int meta = clickType;
        if(Main.widgetSelected == 1 && clickX >= 516 && clickY >= 160 && clickX <= 765 && clickY <= 205)
            meta = 0;
        if(MovedStatics.menuOpen) {
            if(meta != 1) {
                int x = mouseX;
                int y = mouseY;
                if(MovedStatics.menuScreenArea == 0) {
                    x -= 4;
                    y -= 4;
                }
                if(MovedStatics.menuScreenArea == 1) {
                    y -= 205;
                    x -= 553;
                }
                if(MovedStatics.menuScreenArea == 2) {
                    y -= 357;
                    x -= 17;
                }
                if(-10 + InteractiveObject.menuOffsetX > x || 10 + VertexNormal.menuWidth + InteractiveObject.menuOffsetX < x || y < Main.menuOffsetY + -10 || y > Main.menuOffsetY + CollisionMap.menuHeight + 10) {
                    if(MovedStatics.menuScreenArea == 1)
                        GameInterface.redrawTabArea = true;
                    MovedStatics.menuOpen = false;
                    if(MovedStatics.menuScreenArea == 2)
                        ChatBox.redrawChatbox = true;
                }
            }
            if(meta == 1) {
                int menuX = InteractiveObject.menuOffsetX;
                int menuY = Main.menuOffsetY;
                int dx = VertexNormal.menuWidth;
                int x = clickX;
                int y = clickY;
                if(MovedStatics.menuScreenArea == 0) {
                    x -= 4;
                    y -= 4;
                }
                if(MovedStatics.menuScreenArea == 1) {
                    x -= 553;
                    y -= 205;
                }
                if(MovedStatics.menuScreenArea == 2) {
                    x -= 17;
                    y -= 357;
                }
                int id = -1;
                for(int row = 0; row < MovedStatics.menuActionRow; row++) {
                    int k3 = 31 + menuY + 15 * (MovedStatics.menuActionRow + -1 - row);
                    if(x > menuX && x < dx + menuX && y > -13 + k3 && y < 3 + k3)
                        id = row;
                }
                if(id != -1)
                    GameInterface.processMenuActions(id);
                if(MovedStatics.menuScreenArea == 1)
                    GameInterface.redrawTabArea = true;
                MovedStatics.menuOpen = false;
                if(MovedStatics.menuScreenArea == 2)
                    ChatBox.redrawChatbox = true;
            }
        } else {
            if(meta == 1 && MovedStatics.menuActionRow > 0) {
                int action = MovedStatics.menuActionTypes[MovedStatics.menuActionRow - 1];
                if(
                    action == ActionRowType.INTERACT_WITH_ITEM_ON_V1_WIDGET_OPTION_1.getId()
                        || action == ActionRowType.INTERACT_WITH_ITEM_ON_V1_WIDGET_OPTION_2.getId()
                        || action == ActionRowType.INTERACT_WITH_ITEM_ON_V1_WIDGET_OPTION_3.getId()
                        || action == ActionRowType.INTERACT_WITH_ITEM_ON_V1_WIDGET_OPTION_4.getId()
                        || action == ActionRowType.INTERACT_WITH_ITEM_ON_V1_WIDGET_OPTION_5.getId()
                        || action == ActionRowType.INTERACT_WITH_ITEM_ON_V2_WIDGET_OPTION_1.getId()
                        || action == ActionRowType.INTERACT_WITH_ITEM_ON_V2_WIDGET_OPTION_2.getId()
                        || action == ActionRowType.INTERACT_WITH_ITEM_ON_V2_WIDGET_OPTION_3.getId()
                        || action == ActionRowType.INTERACT_WITH_ITEM_ON_V2_WIDGET_OPTION_4.getId()
                        || action == ActionRowType.DROP_ITEM.getId()
                        || action == ActionRowType.SELECT_ITEM_ON_WIDGET.getId()
                        || action == ActionRowType.EXAMINE_ITEM_ON_V1_WIDGET.getId()
                ) {
                    int item = InteractiveObject.firstMenuOperand[MovedStatics.menuActionRow - 1];
                    int id = Class59.secondMenuOperand[-1 + MovedStatics.menuActionRow];
                    GameInterface gameInterface = GameInterface.getInterface(id);
                    if(gameInterface.itemSwapable || gameInterface.itemDeletesDraged) {
                        Renderable.anInt2869 = clickX;
                        Class40_Sub5_Sub15.lastItemDragged = false;
                        MovedStatics.activeInterfaceType = 2;
                        MovedStatics.modifiedWidgetId = id;
                        MovedStatics.anInt2798 = clickY;
                        GroundItemTile.selectedInventorySlot = item;
                        if(id >> 16 == GameInterface.gameScreenInterfaceId)
                            MovedStatics.activeInterfaceType = 1;
                        if(GameInterface.chatboxInterfaceId == id >> 16)
                            MovedStatics.activeInterfaceType = 3;
                        Buffer.lastItemDragTime = 0;
                        return;
                    }
                }
            }
            if(meta == 1 && (ProducingGraphicsBuffer.oneMouseButton == 1 || Class33.menuHasAddFriend(-1 + MovedStatics.menuActionRow)) && MovedStatics.menuActionRow > 2)
                meta = 2;
            if(meta == 1 && MovedStatics.menuActionRow > 0)
                GameInterface.processMenuActions(MovedStatics.menuActionRow - 1);
            if(meta == 2 && MovedStatics.menuActionRow > 0)
                Class60.determineMenuSize();
        }
    }

    public static void method1015() {
        synchronized (GameObject.frame) {
            currentMouseButtonPressed = mouseButtonPressed;
            mouseX = eventMouseX;
            mouseY = eventMouseY;
            clickType = eventMouseButtonPressed;
            clickX = Class55.eventClickX;
            clickY = MovedStatics.eventClickY;
            aLong2561 = lastClick;
            eventMouseButtonPressed = 0;
        }
    }

    public synchronized void mouseEntered(MouseEvent arg0) {
        if(GameObject.frame != null) {
            LinkedList.anInt1073 = 0;
            eventMouseX = arg0.getX();
            eventMouseY = arg0.getY();
        }
    }

    public synchronized void mouseExited(MouseEvent arg0) {
        if(GameObject.frame != null) {
            LinkedList.anInt1073 = 0;
            eventMouseX = -1;
            eventMouseY = -1;
        }
    }

    public synchronized void focusLost(FocusEvent arg0) {
        if(GameObject.frame != null)
            mouseButtonPressed = 0;
    }

    public synchronized void mouseDragged(MouseEvent mouseEvent) {
        int mouseX = mouseEvent.getX();
        int mouseY = mouseEvent.getY();
        if(GameObject.frame != null) {
            LinkedList.anInt1073 = 0;
        }
        if(mouseWheelDown) {
            mouseY = mouseWheelX - mouseEvent.getX();
            int k = mouseWheelY - mouseEvent.getY();
            mouseWheelDragged(mouseY, -k);
            mouseWheelX = mouseEvent.getX();
            mouseWheelY = mouseEvent.getY();
            return;
        }
        eventMouseX = mouseX;
        eventMouseY = mouseY;
    }

    private void mouseWheelDragged(int i, int j) {
        LinkedList.anInt1073 = 0;

        if(!mouseWheelDown)
            return;
        SceneCamera.cameraVelocityYaw += i * 3;
        SceneCamera.cameraVelocityPitch += j << 1;
    }

    public void focusGained(FocusEvent arg0) {
    }

    public synchronized void mousePressed(MouseEvent event) {
        if(GameObject.frame != null) {
            int mouseX = event.getX();
            int mouseY = event.getY();
            LinkedList.anInt1073 = 0;
            Class55.eventClickX = mouseX;
            MovedStatics.eventClickY = mouseY;
            lastClick = System.currentTimeMillis();
            if(event.getButton() == MouseEvent.BUTTON2) {
                mouseWheelDown = true;
                mouseWheelX = mouseX;
                mouseWheelY = mouseY;
                return;
            }
            if(event.isMetaDown() || event.getButton() == MouseEvent.BUTTON3) {
                eventMouseButtonPressed = 2;
                mouseButtonPressed = 2;
            } else {
                eventMouseButtonPressed = 1;
                mouseButtonPressed = 1;
            }
        }
        if(event.isPopupTrigger())
            event.consume();
    }

    public void mouseWheelMoved(MouseWheelEvent event) {
        int rotation = event.getWheelRotation();
        LinkedList.anInt1073 = 0;

        if(!handleInterfaceScrolling(event)) {
            int currentZoom = Main.playerCamera.getZoom();
            if(currentZoom <= 150 && rotation <= 0 || currentZoom >= 1600 && rotation >= 0) {
                return;
            }
            int diff = rotation * 64;
            SceneCamera.cameraVelocityZoom += diff;
        }
    }

    public void mouseClicked(MouseEvent arg0) {
        if(arg0.isPopupTrigger())
            arg0.consume();
    }

    public boolean handleInterfaceScrolling(MouseWheelEvent event) {
        int rotation = event.getWheelRotation();
        int mouseX = eventMouseX;
        int mouseY = eventMouseY;
        if(ScreenController.isCoordinatesInExtendedChatArea(mouseX, mouseY) && GameInterface.chatboxInterfaceId == -1) {
            if(rotation < 0) {
                if(Main.chatboxInterface.scrollPosition >= 1) {

                    if(ChatBox.inputType == 3) {
                        ChatBox.itemSearchScroll = ChatBox.itemSearchScroll - 30;
                        ChatBox.redrawChatbox = true;
                    } else {
                        ChatBox.chatboxScroll = ChatBox.chatboxScroll + 30;
                        ChatBox.redrawChatbox = true;
                    }
                }
            } else if(ChatBox.inputType == 3) {
                ChatBox.itemSearchScroll = ChatBox.itemSearchScroll + 30;
                ChatBox.redrawChatbox = true;
            } else if(ChatBox.chatboxScroll < 1) {
                ChatBox.itemSearchScroll = 0;
                ChatBox.redrawChatbox = true;
            } else {
                ChatBox.chatboxScroll = ChatBox.chatboxScroll - 30;
                ChatBox.redrawChatbox = true;
            }
            return true;
        } else {
            int positionX = 0;
            int positionY = 0;
            int width = 0;
            int height = 0;
            int offsetX = 0;
            int offsetY = 0;
            int childID = 0;
            /* Tab interface scrolling */
            int tabInterfaceID = Player.tabWidgetIds[Player.currentTabId];
            if(tabInterfaceID != -1) {
                if(ScreenController.frameMode == ScreenMode.FIXED) {
                    offsetX = 765 - 218;
                    offsetY = 503 - 298;
                } else {
                    offsetX= ScreenController.drawWidth - 218;
                    offsetY = ScreenController.drawHeight - 298;
                }
                for(int index = 0; index < GameInterface.cachedInterfaces[tabInterfaceID].length; index++) {
                    if(GameInterface.cachedInterfaces[tabInterfaceID][index].scrollHeight > 0) {
                        childID = index;
                        positionX = GameInterface.cachedInterfaces[tabInterfaceID][index].currentX;
                        positionY = GameInterface.cachedInterfaces[tabInterfaceID][index].currentY;
                        width = GameInterface.cachedInterfaces[tabInterfaceID][index].originalWidth;
                        height = GameInterface.cachedInterfaces[tabInterfaceID][index].originalHeight;
                        break;
                    }
                }
                if(mouseX > offsetX + positionX && mouseY > offsetY + positionY && mouseX < offsetX + positionX + width && mouseY < offsetY + positionY + height) {
                    GameInterface.cachedInterfaces[tabInterfaceID][childID].scrollPosition += rotation * 30;
                    //				client.tabAreaAltered = true;
                    GameInterface.redrawTabArea = true;
                    return true;
                }
            }
            /* Main interface scrolling */
            if(GameInterface.gameScreenInterfaceId != -1) {
                offsetX = 4;
                offsetY = 4;
                int widgetid = GameInterface.gameScreenInterfaceId;
                for(int index = 0; index < GameInterface.cachedInterfaces[widgetid].length; index++) {
                    if(GameInterface.cachedInterfaces[widgetid][index].scrollHeight > 0) {
                        childID = index;
                        positionX = GameInterface.cachedInterfaces[widgetid][index].currentX;
                        positionY = GameInterface.cachedInterfaces[widgetid][index].currentY;
                        width = GameInterface.cachedInterfaces[widgetid][index].originalWidth;
                        height = GameInterface.cachedInterfaces[widgetid][index].originalHeight;
                        break;
                    }
                }
                if(ScreenController.frameMode != ScreenMode.FIXED) {
                    int yScreenOffset = (ScreenController.drawHeight /2) - (334/2) - (184/2);
                    int xScreenOffset = (ScreenController.drawWidth /2) - (512/2) - (234/3);
                    positionX += xScreenOffset;
                    positionY += yScreenOffset;
                    width+= xScreenOffset;
                    height+= yScreenOffset;
                }
                if(mouseX > offsetX + positionX && mouseY > offsetY + positionY && mouseX < offsetX + positionX + width && mouseY < offsetY + positionY + height) {
                    GameInterface.cachedInterfaces[widgetid][childID].scrollPosition += rotation * 30;
                    return true;
                }

            } else if(Console.console.consoleOpen) {
                Console.console.currentScroll -= rotation * 18;
                if(Console.console.currentScroll < 0) {
                    Console.console.currentScroll = 0;
                }
                if(Console.console.currentScroll > Console.console.getMaxScroll() - 308) {
                    Console.console.currentScroll = Console.console.getMaxScroll() - 308;
                }
                return true;
            }
        }
        return false;


    }


    public synchronized void mouseMoved(MouseEvent arg0) {
        if(GameObject.frame != null) {
            LinkedList.anInt1073 = 0;
            eventMouseX = arg0.getX();
            eventMouseY = arg0.getY();
        }
    }

    public synchronized void mouseReleased(MouseEvent arg0) {
        if(GameObject.frame != null) {
            LinkedList.anInt1073 = 0;
            mouseButtonPressed = 0;
            mouseWheelDown = false;
        }
        if(arg0.isPopupTrigger())
            arg0.consume();
    }
}
