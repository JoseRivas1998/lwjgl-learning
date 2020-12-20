/*
	The vertex shader runs once for each vertex drawn.
	
	So, for instance, if you draw a triangle it runs three times
	because a triangle has 3 vertices.
*/

// set the precision for floats (lowp, mediump or highp)
precision mediump float;

// declare uniforms
// uniform data is the same for an entire draw call
// (it doesn't change the whole time an object is being drawn)
// Because the color is uniform, boxes cannot be multi-colored.
uniform mat3 mWorld;
uniform vec4 color;

// declare attributes
// attributes are data that describe each individual vertex
// i.e. each vertex has it own unique data for each attribute
attribute vec2 vertPosition;

void main()
{
    // gl_Position sets the vertex position in clip space
    vec3 transformedPosition = mWorld * vec3(vertPosition, 1.0);
    gl_Position = vec4(transformedPosition.x, transformedPosition.y, 0.0, 1.0);
}
