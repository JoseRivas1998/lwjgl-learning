/*
	The vertex shader runs once for each vertex drawn.
	
	So, for instance, if you draw a triangle it runs three times
	because a triangle has 3 vertices.
*/

// set the precision for floats (lowp, mediump or highp)
precision mediump float;

// declare attributes
// attributes are data that describe each individual vertex
// i.e. each vertex has it own unique data for each attribute
attribute vec3 vertPosition;
attribute vec3 vertColor;

// declare varying
// varying data is given value in the vertex shader for use in the fragment shader
// it is "fragment specific"
varying vec3 fragColor;

void main()
{
    fragColor = vertColor;
    gl_Position = vec4(vertPosition, 1.0);
}
