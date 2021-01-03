package com.tcg.lwjgllearning.graphics.g3d.mesh;

import com.tcg.lwjgllearning.graphics.g3d.materials.Material;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;

public class MaterialMesh extends Mesh {
    
    protected final Material material;
    
    public MaterialMesh(Material material, float[] positionArray, float[] normalArray, int[] indexArray, Vector3 position, Quaternion rotation, Vector3 scale) {
        super(material.shaderProgram, positionArray, normalArray, indexArray, position, rotation, scale);
        this.material = material;
    }

    public MaterialMesh(Material material, float[] positionArray, float[] normalArray, int[] indexArray) {
        this(material, positionArray, normalArray, indexArray,
                Vector3.origin(), new Quaternion(), new Vector3(1, 1, 1));
    }

    @Override
    public void activate() {
        super.activate();
        this.material.activate();
    }

    @Override
    public void deactivate() {
        this.material.deactivate();
        super.deactivate();
    }
}
