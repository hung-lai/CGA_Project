#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec2 texture;
    vec3 normale;
    //vec3 toPointLight;
    vec3 toSpotLight1;
    vec3 toSpotLight2;
    vec3 toSpotLight3;
    vec3 toSpotLight4;
    vec3 toSpotLight5;
    vec3 toSpotLight6;
    vec3 toSpotLight7;
    vec3 toSpotLight8;
    vec3 toEckLicht;
    vec3 toSpotLight9;
    vec3 toSpotLight10;
    vec3 toSpotLight11;
    vec3 toSpotLight12;
    vec3 toSpotLight13;
    vec3 toSpotLight14;

} vertexData;

//Material

uniform sampler2D emit;
uniform sampler2D diff;
uniform sampler2D spec;
uniform float shininess;

//uniform vec3 cyclePointLightCol;
//uniform vec3 cyclePointLightAttParam;

uniform vec3 carOneSpotFRLightCol;
uniform vec3 carOneSpotFRLightAttParam;
uniform vec2 carOneSpotFRLightAngle;
uniform vec3 carOneSpotFRLightDir;

uniform vec3 carOneSpotFLLightCol;
uniform vec3 carOneSpotFLLightAttParam;
uniform vec2 carOneSpotFLLightAngle;
uniform vec3 carOneSpotFLLightDir;

uniform vec3 carOneSpotBRLightCol;
uniform vec3 carOneSpotBRLightAttParam;
uniform vec2 carOneSpotBRLightAngle;
uniform vec3 carOneSpotBRLightDir;

uniform vec3 carOneSpotBLLightCol;
uniform vec3 carOneSpotBLLightAttParam;
uniform vec2 carOneSpotBLLightAngle;
uniform vec3 carOneSpotBLLightDir;

uniform vec3 carTwoSpotFRLightCol;
uniform vec3 carTwoSpotFRLightAttParam;
uniform vec2 carTwoSpotFRLightAngle;
uniform vec3 carTwoSpotFRLightDir;

uniform vec3 carTwoSpotFLLightCol;
uniform vec3 carTwoSpotFLLightAttParam;
uniform vec2 carTwoSpotFLLightAngle;
uniform vec3 carTwoSpotFLLightDir;

uniform vec3 carTwoSpotBRLightCol;
uniform vec3 carTwoSpotBRLightAttParam;
uniform vec2 carTwoSpotBRLightAngle;
uniform vec3 carTwoSpotBRLightDir;

uniform vec3 carTwoSpotBLLightCol;
uniform vec3 carTwoSpotBLLightAttParam;
uniform vec2 carTwoSpotBLLightAngle;
uniform vec3 carTwoSpotBLLightDir;

uniform vec3 streetSpotLightCol;
uniform vec3 streetSpotLightAttParam;
uniform vec2 streetSpotLightAngle;
uniform vec3 streetSpotLightDir;

uniform vec3 street2SpotLightCol;
uniform vec3 street2SpotLightAttParam;
uniform vec2 street2SpotLightAngle;
uniform vec3 street2SpotLightDir;

uniform vec3 street3SpotLightCol;
uniform vec3 street3SpotLightAttParam;
uniform vec2 street3SpotLightAngle;
uniform vec3 street3SpotLightDir;

uniform vec3 street4SpotLightCol;
uniform vec3 street4SpotLightAttParam;
uniform vec2 street4SpotLightAngle;
uniform vec3 street4SpotLightDir;

uniform vec3 street5SpotLightCol;
uniform vec3 street5SpotLightAttParam;
uniform vec2 street5SpotLightAngle;
uniform vec3 street5SpotLightDir;

uniform vec3 street6SpotLightCol;
uniform vec3 street6SpotLightAttParam;
uniform vec2 street6SpotLightAngle;
uniform vec3 street6SpotLightDir;

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

//vec3 pointLightIntensity(vec3 lightColor, float length, vec3 attP){
//    return lightColor * attenuate(length, attP);
//}

vec3 spotLightIntensityCarOneFR(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(carOneSpotFRLightAngle.x);
    float cosGamma = cos(carOneSpotFRLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,carOneSpotFRLightAttParam);
}
vec3 spotLightIntensityCarOneFL(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(carOneSpotFLLightAngle.x);
    float cosGamma = cos(carOneSpotFLLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,carOneSpotFLLightAttParam);
}
vec3 spotLightIntensityCarOneBR(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(carOneSpotBRLightAngle.x);
    float cosGamma = cos(carOneSpotBRLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,carOneSpotBRLightAttParam);
}
vec3 spotLightIntensityCarOneBL(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(carOneSpotBLLightAngle.x);
    float cosGamma = cos(carOneSpotBLLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,carOneSpotBLLightAttParam);
}
vec3 spotLightIntensityCarTwoFR(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(carTwoSpotFRLightAngle.x);
    float cosGamma = cos(carTwoSpotFRLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,carTwoSpotFRLightAttParam);
}
vec3 spotLightIntensityCarTwoFL(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(carTwoSpotFLLightAngle.x);
    float cosGamma = cos(carTwoSpotFLLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,carTwoSpotFLLightAttParam);
}
vec3 spotLightIntensityCarTwoBR(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(carTwoSpotBRLightAngle.x);
    float cosGamma = cos(carTwoSpotBRLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,carTwoSpotBRLightAttParam);
}
vec3 spotLightIntensityCarTwoBL(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(carTwoSpotBLLightAngle.x);
    float cosGamma = cos(carTwoSpotBLLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,carTwoSpotBLLightAttParam);
}

vec3 spotLightIntensitystreetSpot(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(streetSpotLightAngle.x);
    float cosGamma = cos(streetSpotLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,streetSpotLightAttParam);
}
vec3 spotLightIntensitystreet2Spot(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(street2SpotLightAngle.x);
    float cosGamma = cos(street2SpotLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,streetSpotLightAttParam);
}
vec3 spotLightIntensitystreet3Spot(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(street3SpotLightAngle.x);
    float cosGamma = cos(street3SpotLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,streetSpotLightAttParam);
}
vec3 spotLightIntensitystreet4Spot(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(street4SpotLightAngle.x);
    float cosGamma = cos(street4SpotLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,streetSpotLightAttParam);
}
vec3 spotLightIntensitystreet5Spot(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(street5SpotLightAngle.x);
    float cosGamma = cos(street5SpotLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,streetSpotLightAttParam);
}
vec3 spotLightIntensitystreet6Spot(vec3 spotLightCol, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(street6SpotLightAngle.x);
    float cosGamma = cos(street6SpotLightAngle.y);

    float intensity = (cosTheta-cosGamma)/(cosPhi-cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightCol * cintensity * attenuate(length,streetSpotLightAttParam);
}


void main(){

    vec3 n = normalize(vertexData.normale);
    vec3 p = normalize(vertexData.position);
    vec3 diffColor = texture(diff, vertexData.texture).rgb;
    vec3 emitColor = texture(emit, vertexData.texture).rgb;
    vec3 specColor = texture(spec, vertexData.texture).rgb;

    //float lpLength = length(vertexData.toPointLight);
    //vec3 lpos = normalize(vertexData.toPointLight);

    float spLength1 = length(vertexData.toSpotLight1);
    vec3 ppos1 = normalize(vertexData.toSpotLight1);

    float spLength2 = length(vertexData.toSpotLight2);
    vec3 ppos2 = normalize(vertexData.toSpotLight2);

    float spLength3 = length(vertexData.toSpotLight3);
    vec3 ppos3 = normalize(vertexData.toSpotLight3);

    float spLength4 = length(vertexData.toSpotLight4);
    vec3 ppos4 = normalize(vertexData.toSpotLight4);

    float spLength5 = length(vertexData.toSpotLight5);
    vec3 ppos5 = normalize(vertexData.toSpotLight5);

    float spLength6 = length(vertexData.toSpotLight6);
    vec3 ppos6 = normalize(vertexData.toSpotLight6);

    float spLength7 = length(vertexData.toSpotLight7);
    vec3 ppos7 = normalize(vertexData.toSpotLight7);

    float spLength8 = length(vertexData.toSpotLight8);
    vec3 ppos8 = normalize(vertexData.toSpotLight8);

    float spLength9 = length(vertexData.toSpotLight9);
    vec3 ppos9 = normalize(vertexData.toSpotLight9);

    float spLength10 = length(vertexData.toSpotLight10);
    vec3 ppos10 = normalize(vertexData.toSpotLight10);

    float spLength11 = length(vertexData.toSpotLight11);
    vec3 ppos11 = normalize(vertexData.toSpotLight11);

    float spLength12 = length(vertexData.toSpotLight12);
    vec3 ppos12 = normalize(vertexData.toSpotLight12);

    float spLength13 = length(vertexData.toSpotLight13);
    vec3 ppos13 = normalize(vertexData.toSpotLight13);

    float spLength14 = length(vertexData.toSpotLight14);
    vec3 ppos14 = normalize(vertexData.toSpotLight14);

    float eckeLength = length(vertexData.toEckLicht);
    vec3 eckpos = normalize(vertexData.toEckLicht);


    //color = vec4(0, (0.5f + abs(vertexData.position.z)), 0, 1.0f);
    //color = vec4(abs(normals.x), abs(normals.y), abs(normals.z), 1.0f);

    //vec3 emitCol = texture(emit, vertexData.texture).rgb;                                                               //texture wird mit daten aufgerufen und nehmen die Farben (rgb) raus

    vec3 pColor = emitColor * sceneColor;

    //pColor += diffSpec(n, lpos, p, diffColor, specColor, shininess) * pointLightIntensity(cyclePointLightCol, lpLength, cyclePointLightAttParam);
    //pColor += diffSpec(n, eckpos, p, diffColor, specColor, shininess) * pointLightIntensity(eckeLightCol, eckeLength, eckeLightAttParam);
    pColor += diffSpec(n, ppos1, p, diffColor, specColor, shininess) * spotLightIntensityCarOneFR(carOneSpotFRLightCol, spLength1, ppos1, carOneSpotFRLightDir);
    pColor += diffSpec(n, ppos2, p, diffColor, specColor, shininess) * spotLightIntensityCarOneFL(carOneSpotFLLightCol, spLength2, ppos2, carOneSpotFLLightDir);
    pColor += diffSpec(n, ppos3, p, diffColor, specColor, shininess) * spotLightIntensityCarOneBR(carOneSpotBRLightCol, spLength3, ppos3, carOneSpotBRLightDir);
    pColor += diffSpec(n, ppos4, p, diffColor, specColor, shininess) * spotLightIntensityCarOneBL(carOneSpotBLLightCol, spLength4, ppos4, carOneSpotBLLightDir);

    pColor += diffSpec(n, ppos5, p, diffColor, specColor, shininess) * spotLightIntensityCarTwoFR(carTwoSpotFRLightCol, spLength5, ppos5, carTwoSpotFRLightDir);
    pColor += diffSpec(n, ppos6, p, diffColor, specColor, shininess) * spotLightIntensityCarTwoFL(carTwoSpotFLLightCol, spLength6, ppos6, carTwoSpotFLLightDir);
    pColor += diffSpec(n, ppos7, p, diffColor, specColor, shininess) * spotLightIntensityCarTwoBR(carTwoSpotBRLightCol, spLength7, ppos7, carTwoSpotBRLightDir);
    pColor += diffSpec(n, ppos8, p, diffColor, specColor, shininess) * spotLightIntensityCarTwoBL(carTwoSpotBLLightCol, spLength8, ppos8, carTwoSpotBLLightDir);

    pColor += diffSpec(n, ppos9, p, diffColor, specColor, shininess) * spotLightIntensitystreetSpot(streetSpotLightCol, spLength9, ppos9, streetSpotLightDir);
    pColor += diffSpec(n, ppos10, p, diffColor, specColor, shininess) * spotLightIntensitystreet2Spot(street2SpotLightCol, spLength10, ppos10, street2SpotLightDir);
    pColor += diffSpec(n, ppos11, p, diffColor, specColor, shininess) * spotLightIntensitystreet3Spot(street3SpotLightCol, spLength11, ppos11, street3SpotLightDir);
    pColor += diffSpec(n, ppos12, p, diffColor, specColor, shininess) * spotLightIntensitystreet4Spot(street4SpotLightCol, spLength12, ppos12, street4SpotLightDir);
    pColor += diffSpec(n, ppos13, p, diffColor, specColor, shininess) * spotLightIntensitystreet5Spot(street5SpotLightCol, spLength13, ppos13, street5SpotLightDir);
    pColor += diffSpec(n, ppos14, p, diffColor, specColor, shininess) * spotLightIntensitystreet6Spot(street6SpotLightCol, spLength14, ppos14, street6SpotLightDir);

    color = vec4(pColor , 1.0f);                                                                                        //Farbe wird neu gesetzt mit emit


}
