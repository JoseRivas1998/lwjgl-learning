precision mediump float;

uniform mat4 mWorld;
uniform mat4 mView;
uniform mat4 mProj;
uniform mat3 mNormal;

attribute vec3 vertPosition;
attribute vec2 vertTexCoord;
attribute vec3 vertNormal;

varying vec2 fragTexCoord;
varying vec3 fragNormal;

void main()
{
	fragTexCoord = vertTexCoord;
	fragNormal = normalize(mNormal * vertNormal);
	gl_Position = mProj * mView * mWorld * vec4(vertPosition, 1.0);
}