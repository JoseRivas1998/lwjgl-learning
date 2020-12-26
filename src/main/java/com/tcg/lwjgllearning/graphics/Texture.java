package com.tcg.lwjgllearning.graphics;

import com.tcg.lwjgllearning.utils.Disposable;
import com.tcg.lwjgllearning.utils.FileUtils;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class Texture implements Disposable {

    public final int width;
    public final int height;

    private final int textureId;

    public Texture(String filePath) {
        this(filePath, false);
    }

    public Texture(String filePath, boolean flipped) {
        ByteBuffer buf = null;
        try {
            final PNGDecoder decoder = new PNGDecoder(FileUtils.getResourceAsStream(filePath));
            this.width = decoder.getWidth();
            this.height = decoder.getHeight();

            buf = MemoryUtil.memAlloc(4 * this.width * this.height);
            if (flipped) {
                decoder.decodeFlipped(buf, this.width * 4, PNGDecoder.Format.RGBA);
            } else {
                decoder.decode(buf, this.width * 4, PNGDecoder.Format.RGBA);
            }
            buf.flip();

            this.textureId = glGenTextures();
            this.bind();
            this.createGLTexture(buf);
            this.unbind();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read image file.", e);
        } finally {
            if (buf != null) {
                MemoryUtil.memFree(buf);
            }
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.textureId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void createGLTexture(ByteBuffer buf) {
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
    }

    @Override
    public void dispose() {
        glDeleteTextures(this.textureId);
    }
}
