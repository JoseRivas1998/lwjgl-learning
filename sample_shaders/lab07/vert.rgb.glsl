precision mediump float;

attribute vec3 vertPosition;
attribute vec3 vertNormal;

varying vec3 fragPosition;
varying vec3 fragNormal;

uniform mat4 mWorld;
uniform mat4 mView;
uniform mat4 mProj;
uniform mat3 mNormal;

void main()
{
    fragPosition = (mWorld * vec4(vertPosition, 1.0)).xyz;
    fragNormal = normalize(mNormal * vertNormal);
    gl_Position = mProj * mView * mWorld * vec4(vertPosition, 1.0);
}
