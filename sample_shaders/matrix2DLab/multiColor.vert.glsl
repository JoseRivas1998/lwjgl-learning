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
uniform mat3 mWorld;

// declare attributes
// attributes are data that describe each individual vertex
// i.e. each vertex has it own unique data for each attribute
attribute vec2 vertPosition;
attribute vec4 vertColor;

// declare varying
// varying data is given value in the vertex shader for use in the fragment shader
// it is "fragment specific"
// fragments get multiple inputs for their varying data (i.e. 3 vertex inputs in a triangle)
// and interpolate between these inputs
varying vec4 fragColor;

void main()
{
    fragColor = vertColor;

    // gl_Position sets the vertex position in clip space
    // fragment positions are interpolated.
    vec3 transformedPosition = mWorld * vec3(vertPosition, 1.0);
    gl_Position = vec4(transformedPosition.x, transformedPosition.y, 0.0, 1.0);
}
