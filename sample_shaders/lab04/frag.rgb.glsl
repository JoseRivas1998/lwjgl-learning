// We will do this together in class (watch the Zoom recording if you missed it!).
precision mediump float;

varying vec4 fragColor;
varying vec3 fragNormal;

// light
uniform vec3 ambientLight;
uniform vec3 lightDirection;
uniform vec3 lightIntensity;

void main()
{
    vec3 light = ambientLight + lightIntensity * max(-dot(fragNormal, normalize(lightDirection)), 0.0);
    gl_FragColor = vec4(light, 1.0) * fragColor;
}
