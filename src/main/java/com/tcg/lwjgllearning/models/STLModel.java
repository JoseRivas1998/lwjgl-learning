package com.tcg.lwjgllearning.models;

import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.g3d.RGBMesh;
import com.tcg.lwjgllearning.graphics.g3d.UniformColorMesh;
import com.tcg.lwjgllearning.utils.ListUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class STLModel extends Model {

    public STLModel(String path) {
        try (final FileInputStream inputStream = new FileInputStream(path)) {
            final byte[] fileBytes = inputStream.readAllBytes();
            final ByteBuffer byteBuffer = ByteBuffer.wrap(fileBytes);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byte[] first5Bytes = new byte[5];
            byteBuffer.get(0, first5Bytes);
            final String first5Characters = new String(first5Bytes, StandardCharsets.UTF_8);
            if (first5Characters.equals("solid")) {
                this.readAsciiFile(fileBytes);
            } else {
                this.parseBinarySTL(byteBuffer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to read stl file.", e);
        }
    }

    public static UniformColorMesh buildStlMesh(String path, ShaderProgram shaderProgram, Color color) {
        final STLModel model = new STLModel(path);
        return new UniformColorMesh(shaderProgram, model.vertices, model.normals, model.indices, color);
    }

    private void readAsciiFile(byte[] fileBytes) {
        final Scanner fileScanner = new Scanner(new ByteArrayInputStream(fileBytes), StandardCharsets.UTF_8);
        boolean done = false;
        int triangleIndex = 0;
        final List<Float> vertexList = new ArrayList<>();
        final List<Float> normalList = new ArrayList<>();
        final List<Integer> indexList = new ArrayList<>();
        while (!done && fileScanner.hasNextLine()) {
            final String line = fileScanner.nextLine();
            final Scanner lineScanner = new Scanner(line);
            final String keyWord = lineScanner.next();
            done = keyWord.equals("endsolid");

            if (keyWord.equals("facet")) {
                this.readNextFacet(fileScanner, triangleIndex, vertexList, normalList, indexList, lineScanner);
                triangleIndex++;
            }

        }
        this.vertices = ListUtils.floatListToArray(vertexList);
        this.normals = ListUtils.floatListToArray(normalList);
        this.indices = ListUtils.intListToArray(indexList);
    }

    private void readNextFacet(Scanner fileScanner, int triangleIndex, List<Float> vertexList, List<Float> normalList, List<Integer> indexList, Scanner lineScanner) {
        lineScanner.next();
        final float normalX = lineScanner.nextFloat();
        final float normalY = lineScanner.nextFloat();
        final float normalZ = lineScanner.nextFloat();
        fileScanner.nextLine();
        this.readTriangleVertices(fileScanner, vertexList, normalList, normalX, normalY, normalZ);
        fileScanner.nextLine();
        fileScanner.nextLine();
        final int triangleStart = triangleIndex * VERTICES_PER_TRIANGLE;
        this.addTriangleIndices(indexList, triangleStart);
    }

    private void addTriangleIndices(List<Integer> indexList, int triangleStart) {
        indexList.add(triangleStart);
        indexList.add(triangleStart + 1);
        indexList.add(triangleStart + 2);
    }

    private void readTriangleVertices(Scanner fileScanner, List<Float> vertexList, List<Float> normalList, float normalX, float normalY, float normalZ) {
        for (int i = 0; i < 3; i++) {
            this.readNextVertex(fileScanner, vertexList, normalList, normalX, normalY, normalZ);
        }
    }

    private void readNextVertex(Scanner fileScanner, List<Float> vertexList, List<Float> normalList, float normalX, float normalY, float normalZ) {
        final Scanner vertexScanner = new Scanner(fileScanner.nextLine());
        vertexScanner.next();
        normalList.add(normalX);
        normalList.add(normalY);
        normalList.add(normalZ);
        vertexList.add(vertexScanner.nextFloat());
        vertexList.add(vertexScanner.nextFloat());
        vertexList.add(vertexScanner.nextFloat());
    }


    private void parseBinarySTL(ByteBuffer byteBuffer) {
        byteBuffer.position(80);
        final int numberOfTriangles = byteBuffer.getInt();
        this.vertices = new float[numberOfTriangles * FLOATS_PER_TRIANGLE];
        this.normals = new float[numberOfTriangles * FLOATS_PER_TRIANGLE];
        this.indices = new int[(numberOfTriangles * FLOATS_PER_TRIANGLE) / VERTICES_PER_TRIANGLE];
        this.parseAllTriangles(byteBuffer, numberOfTriangles);
    }

    private void parseAllTriangles(ByteBuffer byteBuffer, int numberOfTriangles) {
        for (int triangleIndex = 0; triangleIndex < numberOfTriangles; triangleIndex++) {
            this.parseNextTriangle(byteBuffer, triangleIndex);
        }
    }

    private void parseNextTriangle(ByteBuffer byteBuffer, int triangleIndex) {
        final float[] normalArray = this.parseTriangleNormal(byteBuffer);
        final float[] vertices = this.parseTriangleVertices(byteBuffer);
        final int triangleVerticesStart = triangleIndex * FLOATS_PER_TRIANGLE;
        System.arraycopy(vertices, 0, this.vertices, triangleVerticesStart, FLOATS_PER_TRIANGLE);
        System.arraycopy(normalArray, 0, this.normals, triangleVerticesStart, FLOATS_PER_TRIANGLE);
        final int triangleIndexStart = triangleIndex * VERTICES_PER_TRIANGLE;
        final int[] indexArray = {triangleIndexStart, triangleIndexStart + 1, triangleIndexStart + 2};
        System.arraycopy(indexArray, 0, this.indices, triangleIndexStart, VERTICES_PER_TRIANGLE);
        byteBuffer.getChar();
    }

    private float[] parseTriangleVertices(ByteBuffer byteBuffer) {
        return new float[]{
                byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(),
                byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(),
                byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat()
        };
    }

    private float[] parseTriangleNormal(ByteBuffer byteBuffer) {
        final float normalX = byteBuffer.getFloat();
        final float normalY = byteBuffer.getFloat();
        final float normalZ = byteBuffer.getFloat();
        return new float[]{
                normalX, normalY, normalZ,
                normalX, normalY, normalZ,
                normalX, normalY, normalZ
        };
    }

}
