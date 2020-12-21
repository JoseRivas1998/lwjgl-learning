/*
	The fragment shader runs once per approximately pixel-sized part of
	each object drawn.

	There are millions of pixels on your screen; this runs MANY times
*/

precision mediump float;

// the "varying" data is given value in the vertex shader.
// each fragment is part of a triangle.
// each triangle has 3 vertices; they each assign value to the "varying" data.
// each fragment's "varying" data is interpolated between these three vertices.
varying vec4 fragColor;

void main()
{
    // set the color of the fragment.
    // we can (and almost always will) do more here, but
    // at minimum we must decide what color the fragment is.
    gl_FragColor = fragColor;
}
