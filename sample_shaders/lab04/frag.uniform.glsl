precision mediump float;

varying vec3 fragNormal;

// light
uniform vec3 ambientLight;
uniform vec3 lightDirection;
uniform vec3 lightIntensity;

uniform vec4 u_color;

void main()
{
    vec3 light = ambientLight + lightIntensity * max(-dot(fragNormal, normalize(lightDirection)), 0.0);
    gl_FragColor = vec4(light, 1.0) * u_color;
}
