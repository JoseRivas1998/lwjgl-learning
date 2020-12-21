precision mediump float;

uniform mat4 mWorld;
uniform mat4 mView;
uniform mat4 mProj;
uniform mat3 mNormal;
uniform vec4 u_color;

attribute vec3 vertPosition;
attribute vec3 vertColor;
attribute vec3 vertNormal;

varying vec3 fragNormal;

void main()
{
    fragNormal = normalize(mNormal * vertNormal);
    gl_Position = mProj * mView * mWorld * vec4(vertPosition, 1.0);
}
