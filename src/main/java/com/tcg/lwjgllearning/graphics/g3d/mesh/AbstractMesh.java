package com.tcg.lwjgllearning.graphics.g3d.mesh;

import java.util.HashMap;

import com.tcg.lwjgllearning.graphics.Drawable;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Transform3D;
import com.tcg.lwjgllearning.math.Vector3;
import com.tcg.lwjgllearning.utils.Disposable;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public abstract class AbstractMesh extends Transform3D implements Disposable, Drawable {
    
    protected HashMap<AbstractAttribute, Integer> attributeBufferMap;

    public abstract void draw();
    protected abstract void activate();
    protected abstract void deactivate();

    protected AbstractMesh(Vector3 position, Quaternion rotation, Vector3 scale) {
        super(position, rotation, scale);
    }

    protected void activateAttribute2f(AbstractAttribute attribute, int attributeLocation) {
        glBindBuffer(GL_ARRAY_BUFFER, this.attributeBufferMap.get(attribute));
        glVertexAttribPointer(attributeLocation, 2, GL_FLOAT, false, 0, 0);
    }

    protected void activateAttribute3f(AbstractAttribute attribute, int attributeLocation) {
        glBindBuffer(GL_ARRAY_BUFFER, this.attributeBufferMap.get(attribute));
        glVertexAttribPointer(attributeLocation, 3, GL_FLOAT, false, 0, 0);
    }
    
    protected void activateAttribute4f(AbstractAttribute attribute, int attributeLocation) {
        glBindBuffer(GL_ARRAY_BUFFER, this.attributeBufferMap.get(attribute));
        glVertexAttribPointer(attributeLocation, 4, GL_FLOAT, false, 0, 0);
    }
    
    public interface AbstractAttribute {

    }
}
