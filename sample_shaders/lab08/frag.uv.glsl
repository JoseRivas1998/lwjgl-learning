precision mediump float;

// data type definitions
struct DirectionalLight
{
    vec3 direction; // xyz direction of light
    vec3 ambient; // rgb contribution to scene ambient light
    vec3 diffuse; // rgb intensity of diffuse
    vec3 specular; // rgb intensity of specular
};

struct PointLight
{
    vec3 position; // xyz position of source
    vec3 ambient; // rgb contribution to scene ambient light
    vec3 diffuse; // rgb intensity of diffuse
    vec3 specular; // rgb intensity of specular
};

struct Material
{
    float diffuse; // diffuse reflection constant
    float specular; // specular reflection constant
    float ambient; // ambient reflection constant
    float shininess; // shininess constant
};

struct Camera
{
    vec3 position;
    mat4 mProjView;
};

struct DiffuseSpecularIlluminationDouble {
    vec3 diffuseIllumination;
    vec3 specularIllumination;
};

// lights
uniform vec3 ambientLight;
uniform DirectionalLight directionalLights[16];
uniform PointLight pointLights[16];

// material
uniform Material material;

// camera
uniform Camera cam;

// surface
varying vec3 fragPosition;
varying vec3 fragNormal;

// texture
varying vec2 fragTexCoord;
uniform sampler2D sampler;

vec3 directionalLightDiffuseIllumination(DirectionalLight light, float slDotN) {
    return material.diffuse * slDotN * light.diffuse;
}

vec3 directionalLightSpecularIllumination(DirectionalLight light, vec3 sl, vec3 v) {
    vec3 illumination = vec3(0);
    vec3 rl = reflect(sl, fragNormal);
    float rlDotV = dot(rl, v);
    if (rlDotV > 0.0) {
        illumination = material.specular * pow(rlDotV, material.shininess) * light.specular;
    }
    return illumination;
}

DiffuseSpecularIlluminationDouble directionalIllumination(DirectionalLight light, vec3 v) {
    DiffuseSpecularIlluminationDouble illumination = DiffuseSpecularIlluminationDouble(vec3(0), vec3(0));

    vec3 sl = -light.direction;
    float slDotN = dot(sl, fragNormal);
    if (slDotN > 0.0) {
        illumination.diffuseIllumination = directionalLightDiffuseIllumination(light, slDotN);
        illumination.specularIllumination = directionalLightSpecularIllumination(light, sl, v);
    }

    return illumination;
}

vec3 pointLightDiffuseIllumination(PointLight light, float slDotN, float squaredDistance) {
    return material.diffuse * ((slDotN * light.diffuse) / squaredDistance);
}

vec3 pointLightSpecularIllumination(PointLight light, vec3 sl, vec3 v, float squaredDistance) {
    vec3 illumination = vec3(0);

    vec3 rl = reflect(sl, fragNormal);
    float rlDotV = dot(rl, v);
    if (rlDotV > 0.0) {
        illumination = material.specular * ((pow(rlDotV, material.shininess) * light.specular) / squaredDistance);
    }

    return illumination;
}

DiffuseSpecularIlluminationDouble pointIllumination(PointLight light, vec3 v) {
    DiffuseSpecularIlluminationDouble illumination = DiffuseSpecularIlluminationDouble(vec3(0), vec3(0));

    vec3 sl = normalize(light.position - fragPosition);
    float slDotN = dot(sl, fragNormal);
    vec3 diff = fragPosition - light.position;
    float squaredDistance = max(dot(diff, diff), 1.0);
    if (slDotN > 0.0) {
        illumination.diffuseIllumination = pointLightDiffuseIllumination(light, slDotN, squaredDistance);
        illumination.specularIllumination = pointLightSpecularIllumination(light, sl, v, squaredDistance);
    }

    return illumination;
}

void main()
{
    vec4 texel = texture2D(sampler, fragTexCoord);

    // TODO calculate illumination (I in the cheatsheet)
    vec3 ambientLightSum = ambientLight;

    DirectionalLight directional_light;
    PointLight point_light;

    vec3 v = normalize(cam.position - fragPosition);

    vec3 diffuseIllumination = vec3(0);
    vec3 specularIllumination = vec3(0);

    DiffuseSpecularIlluminationDouble directionalIlluminationPerLight;
    DiffuseSpecularIlluminationDouble pointIlluminationPerLight;

    for (int i = 0; i < 16; ++i) {
        directional_light = directionalLights[i];
        point_light = pointLights[i];
        ambientLightSum += directional_light.ambient + point_light.ambient;

        directionalIlluminationPerLight = directionalIllumination(directional_light, v);
        diffuseIllumination += directionalIlluminationPerLight.diffuseIllumination;
        specularIllumination += directionalIlluminationPerLight.specularIllumination;

        pointIlluminationPerLight = pointIllumination(point_light, v);
        diffuseIllumination += pointIlluminationPerLight.diffuseIllumination;
        specularIllumination += pointIlluminationPerLight.specularIllumination;

    }

    vec3 ambientIllumination = material.ambient * ambientLightSum;

    vec3 illumination = ambientIllumination + diffuseIllumination + specularIllumination;

    // TODO calculate gl_FragColor using illumination and texel color
    //gl_FragColor = ...
    gl_FragColor = vec4(texel.rgb * illumination, texel.a);
}
