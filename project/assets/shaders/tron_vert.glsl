#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texture;
layout(location = 2) in vec3 normale;
//uniforms
// translation object to world
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

uniform vec3 cyclePointLightPos;
uniform vec3 carOneSpotFRLightPos;
uniform vec3 carOneSpotFLLightPos;
uniform vec3 carOneSpotBRLightPos;
uniform vec3 carOneSpotBLLightPos;

uniform vec3 carTwoSpotFRLightPos;
uniform vec3 carTwoSpotFLLightPos;
uniform vec3 carTwoSpotBRLightPos;
uniform vec3 carTwoSpotBLLightPos;

uniform vec2 tcMultiplier;          //anlegen von Uniform -> in den Shader laden

uniform vec3 eckeLightPos;
out struct VertexData
{
    vec3 position;
    vec2 texture;
    vec3 normale;
    vec3 toPointLight;
    vec3 toSpotLight1;
        vec3 toSpotLight2;
        vec3 toSpotLight3;
        vec3 toSpotLight4;
        vec3 toSpotLight5;
        vec3 toSpotLight6;
        vec3 toSpotLight7;
        vec3 toSpotLight8;
    vec3 toEckLicht;
} vertexData;



void main(){
    mat4 modelView = view_matrix * model_matrix;
    vec4 pos = modelView * vec4(position, 1.0f);
    //vec4 norm = model_matrix * vec4(normale, 1.0f);
    vec4 norm = inverse(transpose(modelView)) * vec4(normale, 1.0f);

    vec4 lp = view_matrix * vec4(cyclePointLightPos, 1.0f);     //position im Viewspace
    vertexData.toPointLight = (lp - pos).xyz;                   //Richtungsvektor

    vec4 lp2 = view_matrix * vec4(carOneSpotFRLightPos, 1.0f);
    vertexData.toSpotLight1 = (lp2 - pos).xyz;
    vec4 lp3 = view_matrix * vec4(carOneSpotFLLightPos, 1.0f);
    vertexData.toSpotLight2 = (lp3 - pos).xyz;
    vec4 lp4 = view_matrix * vec4(carOneSpotBRLightPos, 1.0f);
    vertexData.toSpotLight3 = (lp4 - pos).xyz;
    vec4 lp5 = view_matrix * vec4(carOneSpotBLLightPos, 1.0f);
    vertexData.toSpotLight4 = (lp5 - pos).xyz;
    vec4 lp6 = view_matrix * vec4(carTwoSpotFRLightPos, 1.0f);
    vertexData.toSpotLight5 = (lp6 - pos).xyz;
    vec4 lp7 = view_matrix * vec4(carTwoSpotFLLightPos, 1.0f);
    vertexData.toSpotLight6 = (lp7 - pos).xyz;
    vec4 lp8 = view_matrix * vec4(carTwoSpotBRLightPos, 1.0f);
    vertexData.toSpotLight7 = (lp8 - pos).xyz;
    vec4 lp9 = view_matrix * vec4(carTwoSpotBLLightPos, 1.0f);
    vertexData.toSpotLight8 = (lp9 - pos).xyz;


    vec4 lp10 = view_matrix * vec4(eckeLightPos, 1.0f);
    vertexData.toEckLicht = (lp10 - pos).xyz;
    //gl_Position = vec4(pos.xy, -pos.z, 1.0f);
    gl_Position = projection_matrix * view_matrix * model_matrix * vec4(position, 1.0f);
    vertexData.position = -pos.xyz;
    vertexData.texture = texture * tcMultiplier;                                                                        //wiederholende Textur, sonst wird die Textur nur einmal in groß angezeigt
    vertexData.normale = norm.xyz;
}