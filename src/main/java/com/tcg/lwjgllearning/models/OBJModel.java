package com.tcg.lwjgllearning.models;

import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.Texture;
import com.tcg.lwjgllearning.graphics.g3d.materials.Material;
import com.tcg.lwjgllearning.graphics.g3d.materials.ScalarPhongMaterial;
import com.tcg.lwjgllearning.graphics.g3d.mesh.MaterialMesh;
import com.tcg.lwjgllearning.graphics.g3d.mesh.TexturedMesh;
import com.tcg.lwjgllearning.graphics.g3d.mesh.UniformColorMesh;
import com.tcg.lwjgllearning.math.Vector2;
import com.tcg.lwjgllearning.math.Vector3;
import com.tcg.lwjgllearning.utils.FileUtils;
import com.tcg.lwjgllearning.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OBJModel extends Model {

    public OBJModel(String filePath) {
        final List<String> lines = FileUtils.readFileLines(filePath);
        final List<Vector3> objVertices = new ArrayList<>();
        final List<Vector2> objTextureCoordinates = new ArrayList<>();
        final List<Vector3> objNormals = new ArrayList<>();

        final List<Float> vertices = new ArrayList<>();
        final List<Float> textureCoordinates = new ArrayList<>();
        final List<Float> normals = new ArrayList<>();
        final List<Integer> index = new ArrayList<>();

        this.parseObjFile(lines, objVertices, objTextureCoordinates, objNormals,
                vertices, textureCoordinates, normals, index);

        this.vertices = ListUtils.floatListToArray(vertices);
        this.normals = ListUtils.floatListToArray(normals);
        this.textureCoordinates = ListUtils.floatListToArray(textureCoordinates);
        this.indices = ListUtils.intListToArray(index);
    }

    private void parseObjFile(List<String> lines, List<Vector3> objVertices, List<Vector2> objTextureCoordinates,
                              List<Vector3> objNormals, List<Float> vertices, List<Float> textureCoordinates,
                              List<Float> normals, List<Integer> index) {
        int i = 0;

        for (String line : lines) {
            try (final Scanner lineScanner = new Scanner(line)) {
                final String firstToken = lineScanner.next();
                switch (firstToken) {
                    case "v":
                        objVertices.add(this.parseVertex(lineScanner));
                        break;
                    case "vt":
                        objTextureCoordinates.add(this.parseTextureCoordinate(lineScanner));
                        break;
                    case "vn":
                        objNormals.add(this.parseVertex(lineScanner));
                        break;
                    case "f":
                        i = this.parseFace(objVertices, objTextureCoordinates, objNormals, vertices,
                                textureCoordinates, normals, index, i, lineScanner);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public static UniformColorMesh uniformOBJ(String filepath, ShaderProgram program, Color color) {
        return uniformOBJ(new OBJModel(filepath), program, color);
    }

    public static UniformColorMesh uniformOBJ(OBJModel objModel, ShaderProgram program, Color color) {
        return new UniformColorMesh(program, objModel.vertices, objModel.normals, objModel.indices, color);
    }

    public static TexturedMesh textureOBJ(String filepath, ShaderProgram program, Texture texture) {
        return textureOBJ(new OBJModel(filepath), program, texture);
    }

    public static TexturedMesh textureOBJ(OBJModel objModel, ShaderProgram program, Texture texture) {
        return new TexturedMesh(program, objModel.vertices, objModel.normals, objModel.indices,
                objModel.textureCoordinates, texture);
    }

    public static TexturedMesh texturedOBJ(OBJModel objModel, ScalarPhongMaterial material, Texture texture) {
        return new TexturedMesh(material, objModel.vertices, objModel.normals, objModel.indices,
                objModel.textureCoordinates, texture);
    }

    public static MaterialMesh meshOBJ(String filepath, Material material) {
        return meshOBJ(new OBJModel(filepath), material);
    }

    public static MaterialMesh meshOBJ(OBJModel objModel, Material material) {
        return new MaterialMesh(material, objModel.vertices, objModel.normals, objModel.indices);
    }

    private int parseFace(List<Vector3> objVertices, List<Vector2> objTextureCoordinates, List<Vector3> objNormals,
                          List<Float> vertices, List<Float> textureCoordinates, List<Float> normals,
                          List<Integer> index, final int i, Scanner lineScanner) {
        final String[] faceLine = lineScanner.nextLine().trim().split("\\s+");
        for (String indices : faceLine) {
            this.parseFaceVertex(objVertices, objTextureCoordinates, objNormals, vertices,
                    textureCoordinates, normals, indices);
        }
        final int numberOfTriangles = faceLine.length - 2;
        for (int triangle = 0; triangle < numberOfTriangles; triangle++) {
            index.add(i);
            index.add(i + triangle + 1);
            index.add(i + triangle + 2);
        }
        return i + numberOfTriangles + 2;
    }

    private void parseFaceVertex(List<Vector3> objVertices, List<Vector2> objTextureCoordinates,
                                 List<Vector3> objNormals, List<Float> vertices, List<Float> textureCoordinates,
                                 List<Float> normals, String indices) {
        try (final Scanner indicesScanner = new Scanner(indices)) {
            indicesScanner.useDelimiter("/");
            int j;
            if (indicesScanner.hasNext()) {
                j = indicesScanner.nextInt() - 1;
                final Vector3 vertex = objVertices.get(j);
                vertices.add(vertex.x);
                vertices.add(vertex.y);
                vertices.add(vertex.z);
            }
            if (indicesScanner.hasNext()) {
                j = indicesScanner.nextInt() - 1;
                final Vector2 textureCoordinate = objTextureCoordinates.get(j);
                textureCoordinates.add(textureCoordinate.x);
                textureCoordinates.add(textureCoordinate.y);
            }
            if (indicesScanner.hasNext()) {
                j = indicesScanner.nextInt() - 1;
                final Vector3 normal = objNormals.get(j);
                normals.add(normal.x);
                normals.add(normal.y);
                normals.add(normal.z);
            }
        }
    }

    private Vector2 parseTextureCoordinate(Scanner lineScanner) {
        final float u = lineScanner.nextFloat();
        final float v = lineScanner.nextFloat();
        return new Vector2(u, v);
    }

    private Vector3 parseVertex(Scanner lineScanner) {
        final float x = lineScanner.nextFloat();
        final float y = lineScanner.nextFloat();
        final float z = lineScanner.nextFloat();
        return new Vector3(x, y, z);
    }

}
