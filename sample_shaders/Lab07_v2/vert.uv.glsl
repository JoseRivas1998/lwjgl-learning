precision mediump float;

struct Camera
{
    vec3 position;
    mat4 mProjView;
};

attribute vec3 vertPosition;
attribute vec2 vertTexCoord;
attribute vec3 vertNormal;

varying vec3 fragPosition;
varying vec2 fragTexCoord;
varying vec3 fragNormal;

uniform mat4 mWorld;
uniform mat3 mNormal;
uniform Camera cam;

void main()
{
    fragPosition = (mWorld * vec4(vertPosition, 1.0)).xyz;
    fragTexCoord = vertTexCoord;
    fragNormal = normalize(mNormal * vertNormal);
    gl_Position = cam.mProjView * mWorld * vec4(vertPosition, 1.0);
}
