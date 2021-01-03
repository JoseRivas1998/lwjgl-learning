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
	float diffuse;
	float specular;
	float ambient;
	float shininess;
};

struct Camera
{
	vec3 position;
	mat4 mProjView;
};

// lights
uniform vec3 ambientLight;
uniform DirectionalLight directionalLights[16];
uniform PointLight pointLights[16];

// model parameters
uniform Material material;

// camera
uniform Camera cam;

// surface parameters
varying vec3 fragPosition;
varying vec3 fragNormal;

// texture parameters
varying vec2 fragTexCoord;
uniform sampler2D sampler;

void main()
{
	vec4 texel = texture2D(sampler, fragTexCoord);

	vec3 light = ambientLight; // will be sum of all ambient light
	vec3 diffuse = vec3(0.0, 0.0, 0.0); // will me sum of all diffuse light
	vec3 specular = vec3(0.0, 0.0, 0.0); // will be sum of all specular light
    vec3 ddir; // direction from surface to directional light source
	vec3 pdir; // direction surface to point light
	float pdist; // squared distance from point light to surface
	vec3 cdir; // direction from surface to camera
	vec3 rdir; // direction of perfectly reflected light
	float nDot;

	for (int i = 0; i < 16; i++)
	{
		// get lights
		DirectionalLight dlight = directionalLights[i];
		PointLight plight = pointLights[i];

        // get direction toward directional light source
        ddir = -dlight.direction;

		// get direction and squared distance toward point light
		pdir = plight.position - fragPosition;
		pdist = dot(pdir, pdir) + 1.0;
		pdir = normalize(pdir);
        

		// get direction from surface to camera
		cdir = normalize(cam.position - fragPosition);

		// ambient light contributions
		light += dlight.ambient + plight.ambient;

		// directional light
		nDot = dot(ddir, fragNormal);
		if (nDot > 0.0)
		{
			// diffuse
			diffuse += dlight.diffuse * nDot;

			// specular
			rdir = 2.0 * nDot * fragNormal - ddir ;
			specular += dlight.specular * pow( max( dot(rdir, cdir), 0.0), material.shininess);
		}

		// point light
		nDot = dot(pdir, fragNormal);
		if (nDot > 0.0)
		{
			// diffuse
			diffuse += plight.diffuse * nDot / pdist;

			// specular
			rdir = 2.0 * nDot * fragNormal - pdir;
			specular += plight.specular * pow( max( dot(rdir, cdir), 0.0), material.shininess) / pdist;
		}
	}

	light = light * material.ambient + diffuse * material.diffuse + specular * material.specular;
	
	gl_FragColor = vec4(texel.rgb * light, texel.a);
}