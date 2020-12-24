precision mediump float;

uniform sampler2D texture;

uniform vec3 ambientLight;
uniform vec3 lightDirection;
uniform vec3 lightIntensity;

varying vec3 fragNormal;
varying vec2 fragTexCoord;

void main()
{
	vec4 texel = texture2D(texture, fragTexCoord);
	vec3 light = ambientLight + lightIntensity * max( -dot( fragNormal,normalize(lightDirection) ), 0.0);
	
	gl_FragColor = vec4(texel.rgb * light, texel.a);
}