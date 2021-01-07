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
	vec3 diffuse; // diffuse reflection color
	vec3 specular; // specular reflection color
	vec3 ambient; // ambient reflection color
	float shininess; // "shininess / glossiness" constant
};

struct Camera
{
	vec3 position;
	mat4 mProjView;
};

// lights
uniform vec3 ambientLight;
uniform DirectionalLight directionalLights[#DIRECTIONAL_LIGHT_COUNT];
uniform PointLight pointLights[#POINT_LIGHT_COUNT];

// model parameters
uniform Material material;

// camera
uniform Camera cam;

// surface parameters
varying vec3 fragPosition;
varying vec3 fragNormal;

void main()
{
	vec3 light = ambientLight; // will be sum of all ambient light
	vec3 diffuse = vec3(0.0, 0.0, 0.0); // will me sum of all diffuse light
	vec3 specular = vec3(0.0, 0.0, 0.0); // will be sum of all specular light
	vec3 ldir; // direction surface toward light source
	float dist; // squared distance from point light to surface
	vec3 cdir = normalize(cam.position - fragPosition); // direction from surface to camera
	vec3 rdir; // direction of perfectly reflected light
	float nDot;

	for (int i = 0; i < #DIRECTIONAL_LIGHT_COUNT; i++)
	{
		DirectionalLight dlight = directionalLights[i];

        ldir = -dlight.direction;
		
		light += dlight.ambient;

		nDot = dot(ldir, fragNormal);
		if (nDot > 0.0)
		{
			diffuse += dlight.diffuse * nDot;

			rdir = 2.0 * nDot * fragNormal - ldir ;
			specular += dlight.specular * pow( max( dot(rdir, cdir), 0.0), material.shininess);
		}
	}

	for (int i = 0; i < #POINT_LIGHT_COUNT; i++)
	{
		PointLight plight = pointLights[i];

		ldir = plight.position - fragPosition;
		dist = dot(ldir, ldir) + 1.0;
		ldir = normalize(ldir);
		
		light += plight.ambient;

		nDot = dot(ldir, fragNormal);
		if (nDot > 0.0)
		{
			// diffuse
			diffuse += plight.diffuse * nDot / dist;

			// specular
			rdir = 2.0 * nDot * fragNormal - ldir;
			specular += plight.specular * pow( max( dot(rdir, cdir), 0.0), material.shininess) / dist;
		}
	}

	light = light * material.ambient + diffuse * material.diffuse + specular * material.specular;

	gl_FragColor = vec4(light, 1.0);
}