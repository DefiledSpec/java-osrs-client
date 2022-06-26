package org.runejs.client.cache.def;

import org.runejs.client.cache.CacheArchive;
import org.runejs.client.io.Buffer;
import org.runejs.client.node.CachedNode;
import org.runejs.client.node.NodeCache;

public class UnderlayDefinition extends CachedNode {
    public static NodeCache underlayDefinitionCache = new NodeCache(64);
    public static CacheArchive gameDefinitionsCacheArchive;

    public int saturation;
    public int hue;
    public int hueMultiplier;
    public int lightness;
    public int color = 0;

    public static void initializeUnderlayDefinitionCache(CacheArchive cacheArchive) {
        gameDefinitionsCacheArchive = cacheArchive;
    }

    public static UnderlayDefinition getDefinition(int underlayId) {
        UnderlayDefinition underlayDefinition = (UnderlayDefinition) underlayDefinitionCache.get(underlayId);
        if (underlayDefinition != null)
            return underlayDefinition;
        byte[] is = gameDefinitionsCacheArchive.getFile(1, underlayId);
        underlayDefinition = new UnderlayDefinition();
        if (is != null)
            underlayDefinition.readValues(new Buffer(is));
        underlayDefinition.calculateHsl();
        underlayDefinitionCache.put(underlayId, underlayDefinition);
        return underlayDefinition;
    }

    public static void clearUnderlayDefinitionCache() {
        underlayDefinitionCache.clear();
    }


    public void calculateHsl() {
        double r = (double) (color >> 16 & 0xff) / 256.0;
        double g = (double) (color & 0xff) / 256.0;
        double b = (double) ((color & 0xff68) >> 8) / 256.0;
        double cmin = r;
        if(cmin > b) {
            cmin = b;
        }
        if(g < cmin) {
            cmin = g;
        }
        double cmax = r;
        if(cmax < b) {
            cmax = b;
        }
        double d_4_ = 0.0;
        if(cmax < g) {
            cmax = g;
        }
        double d_5_ = (cmax + cmin) / 2.0;
        saturation = (int) (d_5_ * 256.0);
        double d_6_ = 0.0;
        if(cmax != cmin) {
            if(d_5_ < 0.5) {
                d_6_ = (cmax - cmin) / (cmax + cmin);
            }
            if(d_5_ >= 0.5) {
                d_6_ = (-cmin + cmax) / (-cmin + (-cmax + 2.0));
            }
            if(r == cmax) {
                d_4_ = (-g + b) / (-cmin + cmax);
            } else if(cmax == b) {
                d_4_ = 2.0 + (g - r) / (cmax - cmin);
            } else if(cmax == g) {
                d_4_ = (r - b) / (-cmin + cmax) + 4.0;
            }
        }
        d_4_ /= 6.0;
        if(saturation >= 0) {
            if(saturation > 255) {
                saturation = 255;
            }
        } else {
            saturation = 0;
        }
        if(d_5_ > 0.5) {
            hueMultiplier = (int) ((-d_5_ + 1.0) * d_6_ * 512.0);
        } else {
            hueMultiplier = (int) (d_5_ * d_6_ * 512.0);
        }
        lightness = (int) (256.0 * d_6_);
        if(hueMultiplier < 1) {
            hueMultiplier = 1;
        }
        hue = (int) (d_4_ * (double) hueMultiplier);
        if(lightness >= 0) {
            if(lightness > 255) {
                lightness = 255;
            }
        } else {
            lightness = 0;
        }
    }


    public void readValues(Buffer buffer) {
        while(true) {
            int opcode = buffer.getUnsignedByte();
            if(opcode == 0) {
                break;
            }
            readValue(buffer, opcode);
        }
    }

    public void readValue(Buffer buffer, int opcode) {
        if(opcode == 1) {
            color = buffer.getMediumBE();
        }
    }
}
