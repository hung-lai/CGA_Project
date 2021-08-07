#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec2 texture;
    vec3 normale;
    vec3 toPointLight;
    vec3 toSpotLight;
    vec3 toEckLicht;
} vertexData;

//Material

uniform sampler2D emit;
uniform sampler2D diff;
uniform sampler2D spec;
uniform float shininess;

uniform vec3 cyclePointLightCol;
uniform vec3 cyclePointLightAttParam;

uniform vec3 cycleSpotLightCol;
uniform vec3 cycleSpotLightAttParam;
uniform vec2 cycleSpotLightAngle;
uniform vec3 cycleSpotLightDir;

uniform vec3 eckeLightCol;
uniform vec3 eckeLightAttParam;

uniform vec3 sceneColor;

//fragment shader output
out vec4 color;

vec3 diffSpec(vec3 n, vec3 l, vec3 p, vec3 diff, vec3 spec, float shine){
    vec3 diffuse = diff*max(0.0f, dot(n,l));

    vec3 halfwayDir = normalize(l + p);
    vec3 specular = spec*pow(max(dot(n, halfwayDir),0.0),shine);

    //vec3 reflectDir = reflect(-l,n);
    //float cosb = max(dot(p, reflectDir), 0.0f);
    //vec3 specular = spec*pow(cosb, shine);

    return diffuse + specular;
}

float attenuate(float length, vec3 attParam){
    return 1.0/(attParam.x + attParam.y * length + attParam.z * pow(length,2));
}

vec3 pointLightIntensity(vec3 lightColor, float length, vec3 attP){
    return lightColor * attenuate(length, attP);
}

vec3 spotLightIntensity(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(cycleSpotLightAngle.x);
    float cosGamma = cos(cycleSpotLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,cycleSpotLightAttParam);
}

void main(){

    vec3 n = normalize(vertexData.normale);
    vec3 p = normalize(vertexData.position);
    vec3 diffColor = texture(diff, vertexData.texture).rgb;
    vec3 emitColor = texture(emit, vertexData.texture).rgb;
    vec3 specColor = texture(spec, vertexData.texture).rgb;

    float lpLength = length(vertexData.toPointLight);
    vec3 lpos = normalize(vertexData.toPointLight);

    float spLength = length(vertexData.toSpotLight);
    vec3 ppos = normalize(vertexData.toSpotLight);

    float eckeLength = length(vertexData.toEckLicht);
    vec3 eckpos = normalize(vertexData.toEckLicht);

    //color = vec4(0, (0.5f + abs(vertexData.position.z)), 0, 1.0f);
    //color = vec4(abs(normals.x), abs(normals.y), abs(normals.z), 1.0f);

    //vec3 emitCol = texture(emit, vertexData.texture).rgb;                                                               //texture wird mit daten aufgerufen und nehmen die Farben (rgb) raus

    vec3 pColor = emitColor * sceneColor;

    pColor += diffSpec(n, lpos, p, diffColor, specColor, shininess) * pointLightIntensity(cyclePointLightCol, lpLength, cyclePointLightAttParam);
    pColor += diffSpec(n, ppos, p, diffColor, specColor, shininess) * spotLightIntensity(cycleSpotLightCol, spLength, ppos, cycleSpotLightDir);
    pColor += diffSpec(n, eckpos, p, diffColor, specColor, shininess) * pointLightIntensity(eckeLightCol, eckeLength, eckeLightAttParam);
    color = vec4(pColor , 1.0f);                                                                                        //Farbe wird neu gesetzt mit emit


}
